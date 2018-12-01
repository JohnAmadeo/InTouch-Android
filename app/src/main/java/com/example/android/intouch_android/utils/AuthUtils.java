package com.example.android.intouch_android.utils;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.auth0.android.Auth0;
import com.auth0.android.jwt.JWT;
import com.auth0.android.provider.AuthCallback;
import com.auth0.android.provider.WebAuthProvider;
import com.auth0.android.result.Credentials;
import com.example.android.intouch_android.R;
import com.example.android.intouch_android.model.User;
import com.example.android.intouch_android.repository.UserRepository;

import java.util.HashMap;

import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

public class AuthUtils {
    private String LOG_TAG = this.getClass().getSimpleName();

    public static void authenticate(Activity activity, AuthCallback authCallback) {
        showAuthenticationPage(
                activity,
                authCallback,
                new AuthLoginPageParamsBuilder()
                        .forceLogin()
                        .prefillWithPlaceholderEmail()
                        .setEmailInstructionsOnSignup("Use the pre-filled email if you don't use email")
                        .build()
        );
    }

    public static void signup(Activity activity, AuthCallback authCallback) {
        showAuthenticationPage(
                activity,
                authCallback,
                new AuthLoginPageParamsBuilder()
                        .disableLogin()
                        .prefillWithPlaceholderEmail()
                        .setEmailInstructionsOnSignup("Use the pre-filled email if you don't use email")
                        .build()
        );
    }

    public static void logout(Activity activity, View view, int fromId) {
        UserRepository userRepository = new UserRepository(activity.getApplicationContext());
        userRepository.clearUser();
        Navigation.findNavController(view).navigate(
                R.id.launchFragment,
                null,
                new NavOptions.Builder().setPopUpTo(fromId, true).build()
        );
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

    public static String formatAuthHeader(String accessToken) { return "Bearer " + accessToken; }

    private static void showAuthenticationPage(
            Activity activity,
            AuthCallback authCallback,
            HashMap<String, Object> params
    ) {
        Auth0 account = new Auth0(activity.getApplicationContext());
        account.setOIDCConformant(true);
        WebAuthProvider.init(account)
                .withScheme("demo")
                .withScope("openid profile email offline_access")
                .withAudience("https://intouch-android-backend.herokuapp.com/")
                .withParameters(params)
                .start(activity, authCallback);
    }
}
