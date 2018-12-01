package com.example.android.intouch_android.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationAPIClient;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.callback.BaseCallback;
import com.auth0.android.jwt.DecodeException;
import com.auth0.android.jwt.JWT;
import com.auth0.android.result.Credentials;
import com.example.android.intouch_android.R;
import com.example.android.intouch_android.api.AuthService;
import com.example.android.intouch_android.api.AuthServiceProvider;
import com.example.android.intouch_android.api.InTouchService;
import com.example.android.intouch_android.api.InTouchServiceProvider;
import com.example.android.intouch_android.api.Webservice;
import com.example.android.intouch_android.api.WebserviceProvider;
import com.example.android.intouch_android.database.LocalDatabase;
import com.example.android.intouch_android.model.User;
import com.example.android.intouch_android.model.container.ApiException;
import com.example.android.intouch_android.model.container.ApiExceptionType;
import com.example.android.intouch_android.model.container.HTTPCode;
import com.example.android.intouch_android.model.container.Status;
import com.example.android.intouch_android.model.container.UpdateTokenRequest;
import com.example.android.intouch_android.utils.AppExecutors;
import com.example.android.intouch_android.utils.AppState;

import java.util.Date;

import io.reactivex.Single;

public class UserRepository {
    private final String LOG_TAG = this.getClass().getSimpleName();
    private final Context mContext;
    private final Webservice mWebservice;
    private final AuthService mAuthService;
    private final InTouchService mInTouchService;
    private final LocalDatabase mDB;
    private final AppExecutors mExecutors;
    private final AppState mAppState;

    public UserRepository(Context context) {
        mContext = context;
        mWebservice = WebserviceProvider.getInstance();
        mAuthService = AuthServiceProvider.getInstance();
        mInTouchService = InTouchServiceProvider.getInstance();
        mDB = LocalDatabase.getInstance(context);
        mExecutors = AppExecutors.getInstance();
        mAppState = AppState.getInstance();
    }

    public void clearUser() {
        mAppState.setUser(null);
        mExecutors.diskIO().execute(() -> mDB.userDao().deleteAllUsers());
    }

    public void saveUser(User user) {
        mAppState.setUser(user);
        mExecutors.diskIO().execute(() -> {
            mDB.beginTransaction();
            try {
                mDB.userDao().deleteAllUsers();
                mDB.userDao().saveUser(user);
                mDB.setTransactionSuccessful();
            } finally {
                mDB.endTransaction();
            }
        });
    }

    public Single<Status> saveUserWithObservable(User user) {
        return Single.create(subscriber -> {
            mAppState.setUser(user);
            mExecutors.diskIO().execute(() -> {
                mDB.beginTransaction();
                try {
                    mDB.userDao().deleteAllUsers();
                    mDB.userDao().saveUser(user);
                    mDB.setTransactionSuccessful();
                    subscriber.onSuccess(Status.SUCCESS);
                }
                catch (Exception e) {
                    subscriber.onError(e);
                } finally {
                    mDB.endTransaction();
                }
            });
        });
    }

    public User getUserFromAppState() { return mAppState.getUser(); }

    public LiveData<User> getUserFromDB() {
        return Transformations.map(
                mDB.userDao().getUsers(),
                users -> {
                    if (users.size() != 1) {
                        return null;
                    }
                    else {
                        return users.get(0);
                    }
                }
        );
    }

    /* ************************************************************ */
    /*                       SEND LETTER FLOW                       */
    /* ************************************************************ */

    public Single<User> getUser() {
        User user = mAppState.getUser();
        if (user == null) {
            return Single.error(new ApiException(
                    ApiExceptionType.GET_USER, "No user stored in app state"
            ));
        }
        else if (user.isTemporaryUser()) {
            return registerTemporaryUser(user);
        }
        else {
            return Single.just(user);
        }
    }

    public Single<String> getAccessToken(@NonNull User user) {
        // TODO: If user is placeholder user, we have no way of verifying whether access token has
        // expired, so maybe we should just re-fetch the access token each time.
        if (user.isPlaceholderUser()) {
            return getAccessTokenForPlaceholderUser(user);
        }
        else {
            if (isAccessTokenExpired()) {
                return renewAccessToken(user);
            }
            else {
                return Single.just(user.getAccessToken());
            }
        }
    }

    /* ************************************************************ */
    /*                   getAccessToken Helper                      */
    /* ************************************************************ */

    private Single<String> renewAccessToken(@NonNull User user) {
        UpdateTokenRequest body = new UpdateTokenRequest(
                "refresh_token",
                mContext.getString(R.string.com_auth0_client_id),
                user.getRefreshToken()
        );

        return mAuthService.getRefreshToken(body)
                .map(response -> {
                    if (response.code() == HTTPCode.OK && response.body() != null) {
                        String accessToken = response.body().getAccessToken();
                        setAccessToken(accessToken);
                        return accessToken;
                    }
                    else {
                        throw new ApiException(
                                ApiExceptionType.GET_NEW_ACCESS_TOKEN,
                                "Failed to get new access token using refresh token"
                        );
                    }
                });
    }

    private Single<String> getAccessTokenForPlaceholderUser(@NonNull User user) {
        Auth0 account = new Auth0(mContext);
        account.setOIDCConformant(true);
        AuthenticationAPIClient client = new AuthenticationAPIClient(account);

        return Single.create(subscriber -> {
            Log.d(LOG_TAG, "username" + user.getUsername() + "password" + user.getPlaceholderPassword());
            client.login(
                    user.getUsername(),
                    user.getPlaceholderPassword(),
                    mContext.getString(R.string.auth0_connection)
            )
                    .start(new BaseCallback<Credentials, AuthenticationException>() {
                        @Override
                        public void onSuccess(Credentials payload) {
                            Log.d(LOG_TAG, "\nTemp User Credentials:" + "\n" +
                                            "accessToken=" + payload.getAccessToken() + "\n" +
                                            "idToken=" + payload.getIdToken() + "\n" +
                                            "refreshToken=" + payload.getRefreshToken()
                            );
                            setAccessToken(payload.getAccessToken());
                            setIdToken(payload.getIdToken());
                            subscriber.onSuccess(payload.getAccessToken());
                        }

                        @Override
                        public void onFailure(AuthenticationException error) {
                            Log.d(
                                    LOG_TAG,
                                    "\nAuth0 Error Code:" + error.getCode() + "\n" +
                                            "Auth0 Error Description:" + error.getDescription() + "\n" +
                                            "Auth0 Status Code:" + error.getStatusCode()
                            );
                            subscriber.onError(error);
                        }
                    });
        });
    }

    private boolean isAccessTokenExpired() {
        User user = mAppState.getUser();
        if (user == null) {
            return true;
        }

        try {
            JWT jwt = new JWT(user.getAccessToken());
            Date now = new Date();

            return now.after(jwt.getExpiresAt());
        }
        // If access token is an opaque string, refetch
        catch (DecodeException e) {
            return false;
        }
    }

    private void setAccessToken(String accessToken) {
        mAppState.setUserAccessToken(accessToken);
        mExecutors.diskIO().execute(() -> {
            mDB.userDao().setAccessToken(accessToken, mAppState.getUsername());
        });
    }


    private void setIdToken(String idToken) {
        mAppState.setUserIdToken(idToken);
        mExecutors.diskIO().execute(() -> {
            mDB.userDao().setIdToken(idToken, mAppState.getUsername());
        });
    }

    /* ************************************************************ */
    /*                       getUser Helpers                        */
    /* ************************************************************ */

    // Registers the existence of the temporary user on the server, making the user no longer a
    // temporary user
    private Single<User> registerTemporaryUser(@NonNull User user) {
        return mInTouchService.createUser(user)
                .map(response -> {
                    if (response.code() == 201) {
                        return user;
                    }
                    else {
                        throw new ApiException(
                                ApiExceptionType.CREATE_TEMPORARY_USER,
                                "Could not create temporary user on backend"
                        );
                    }
                });
    }
}
