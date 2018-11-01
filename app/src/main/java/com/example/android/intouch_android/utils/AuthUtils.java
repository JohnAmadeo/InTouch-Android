package com.example.android.intouch_android.utils;

import android.app.Activity;
import android.app.Dialog;
import android.support.annotation.NonNull;
import android.util.Log;

import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.jwt.JWT;
import com.auth0.android.provider.AuthCallback;
import com.auth0.android.provider.WebAuthProvider;
import com.auth0.android.result.Credentials;
import com.example.android.intouch_android.R;
import com.example.android.intouch_android.model.User;

public class AuthUtils {
    private String LOG_TAG = this.getClass().getSimpleName();

    public static void login(Activity activity, AuthCallback authCallback) {
        Auth0 account = new Auth0(activity.getApplicationContext());
        account.setOIDCConformant(true);

        WebAuthProvider.init(account)
                .withScheme("demo")
                .withScope("openid profile email offline_access")
                .withAudience("https://intouch-android-backend.herokuapp.com/")
                .start(activity, authCallback);
    }

    public static User getUserFromCredentials(Credentials credentials) {
        JWT idToken = new JWT(credentials.getIdToken());
        String username = idToken.getClaim("nickname").asString();
        String email = idToken.getClaim("name").asString();

        return new User(
                username,
                email,
                credentials.getAccessToken(),
                credentials.getIdToken(),
                credentials.getRefreshToken()

        );
    }
}
