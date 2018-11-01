package com.example.android.intouch_android.ui.launch;

import android.app.Activity;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.provider.AuthCallback;
import com.auth0.android.result.Credentials;
import com.example.android.intouch_android.R;
import com.example.android.intouch_android.model.User;
import com.example.android.intouch_android.utils.AuthUtils;
import com.example.android.intouch_android.utils.Utils;
import com.example.android.intouch_android.viewmodel.LaunchViewModel;

import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

public class LaunchFragment extends Fragment {
    private final String LOG_TAG = this.getClass().getSimpleName();

    private Activity mParentActivity;
    private View mParentView;
    private Button mGetStartedButton;
    private Button mLogInSignUpButton;

    private LaunchViewModel mLaunchViewModel;

    public LaunchFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mParentActivity = getActivity();
        Utils.setActionBarVisible(mParentActivity, false);
        Utils.setBottomNavigationVisible(mParentActivity, false);

        mParentView = inflater.inflate(R.layout.fragment_launch, container, false);
        mGetStartedButton = mParentView.findViewById(R.id.get_started);
        mLogInSignUpButton = mParentView.findViewById(R.id.login_signup);

        /* Setup observers */
        mLaunchViewModel = ViewModelProviders.of(this).get(LaunchViewModel.class);

        mLaunchViewModel.getUser().observe(this, user -> {
            if (user != null) {
                mLaunchViewModel.saveUser(user);
                navigateAwayFromLaunchScreen();
            }
        });

        mGetStartedButton.setOnClickListener(view -> {
            Log.d(LOG_TAG, "Get started");
            mLaunchViewModel.saveUser(User.createTemporaryUser());
            navigateAwayFromLaunchScreen();
        });

        mLogInSignUpButton.setOnClickListener(view -> {
            Log.d(LOG_TAG, "Log in / Sign up");
            AuthUtils.login(mParentActivity, createLoginSignUpAuthCallback());
        });

        return mParentView;
    }

    public AuthCallback createLoginSignUpAuthCallback() {
        return new AuthCallback() {
            @Override
            public void onFailure(@NonNull Dialog dialog) { dialog.show(); }

            @Override
            public void onFailure(AuthenticationException e) { Log.d(LOG_TAG, e.toString()); }

            @Override
            public void onSuccess(@NonNull Credentials credentials) {
                mLaunchViewModel.saveUser(AuthUtils.getUserFromCredentials(credentials));
                navigateAwayFromLaunchScreen();
            }
        };
    }

    private void navigateAwayFromLaunchScreen() {
        Navigation.findNavController(mParentView).navigate(
                R.id.sentLettersFragment,
                null,
                new NavOptions.Builder()
                        .setPopUpTo(
                                R.id.launchFragment,
                                true
                        ).build()
        );
    }
}
