package com.example.android.intouch_android.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.auth0.android.jwt.JWT;
import com.example.android.intouch_android.R;
import com.example.android.intouch_android.api.AuthService;
import com.example.android.intouch_android.api.AuthServiceProvider;
import com.example.android.intouch_android.api.Webservice;
import com.example.android.intouch_android.api.WebserviceProvider;
import com.example.android.intouch_android.database.LocalDatabase;
import com.example.android.intouch_android.model.User;
import com.example.android.intouch_android.model.container.ApiException;
import com.example.android.intouch_android.model.container.ApiExceptionType;
import com.example.android.intouch_android.model.container.HTTPCode;
import com.example.android.intouch_android.model.container.UpdateTokenRequest;
import com.example.android.intouch_android.model.container.UpdateTokenResponse;
import com.example.android.intouch_android.utils.AppExecutors;
import com.example.android.intouch_android.utils.AppState;

import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.schedulers.Schedulers;

public class UserRepository {
    private final String LOG_TAG = this.getClass().getSimpleName();
    private final Context mContext;
    private final Webservice mWebservice;
    private final AuthService mAuthService;
    private final LocalDatabase mDB;
    private final AppExecutors mExecutors;
    private final AppState mAppState;

    public UserRepository(Context context) {
        mContext = context;
        mWebservice = WebserviceProvider.getInstance();
        mAuthService = AuthServiceProvider.getInstance();
        mDB = LocalDatabase.getInstance(context);
        mExecutors = AppExecutors.getInstance();
        mAppState = AppState.getInstance();
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

    public Single<String> getAccessToken() {
        User user = mAppState.getUser();
        if (user == null) {
            throw Exceptions.propagate(new Exception("No user stored in app state"));
        }

        if (isAccessTokenExpired()) {
            return getNewAccessToken(user);
        }
        else {
            return Single.just(user.getAccessToken());
        }
    }

    /* ************************************************************ */
    /*                       Private Functions                      */
    /* ************************************************************ */

    private Single<String> getNewAccessToken(@NonNull User user) {
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
                        throw Exceptions.propagate(new ApiException(
                                ApiExceptionType.GET_NEW_ACCESS_TOKEN,
                                "Failed to get new access token using refresh token"
                        ));
                    }
                });
    }

    private boolean isAccessTokenExpired() {
        User user = mAppState.getUser();
        if (user == null) {
            return true;
        }

        JWT jwt = new JWT(user.getAccessToken());
        Date now = new Date();

        return now.after(jwt.getExpiresAt());
    }

    private void setAccessToken(String accessToken) {
        mAppState.setUserAccessToken(accessToken);
        mExecutors.diskIO().execute(() -> {
            mDB.userDao().setAccessToken(accessToken, mAppState.getUsername());
        });
    }
}
