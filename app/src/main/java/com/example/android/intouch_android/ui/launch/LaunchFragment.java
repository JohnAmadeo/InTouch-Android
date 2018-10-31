package com.example.android.intouch_android.ui.launch;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.jwt.JWT;
import com.auth0.android.provider.AuthCallback;
import com.auth0.android.result.Credentials;
import com.example.android.intouch_android.R;
import com.example.android.intouch_android.model.User;
import com.example.android.intouch_android.utils.AuthUtils;
import com.example.android.intouch_android.utils.Utils;
import com.example.android.intouch_android.viewmodel.LaunchViewModel;
import com.example.android.intouch_android.viewmodel.SentLettersViewModel;

import androidx.navigation.Navigation;

public class LaunchFragment extends Fragment {
    private final String LOG_TAG = this.getClass().getSimpleName();

    private Activity mParentActivity;
    private View mParentView;
    private Button mGetStartedButton;
    private Button mLogInSignUpButton;
    private ProgressBar mSpinner;

    private LaunchViewModel mLaunchViewModel;

    public LaunchFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mParentActivity = getActivity();
        Utils.setActionBarVisible(mParentActivity, false);
        Utils.setBottomNavigationVisible(mParentActivity, false);

        mParentView = inflater.inflate(R.layout.fragment_launch, container, false);
        mGetStartedButton = mParentView.findViewById(R.id.get_started);
        mLogInSignUpButton = mParentView.findViewById(R.id.login_signup);
        mSpinner = mParentView.findViewById(R.id.launch_spinner);

        mSpinner.setVisibility(View.GONE);

        /* Setup observers */
        mLaunchViewModel = ViewModelProviders.of(this).get(LaunchViewModel.class);

        mGetStartedButton.setOnClickListener(view -> {
            Log.d(LOG_TAG, "Get started");
            mLaunchViewModel.saveUser(User.createTemporaryUser());
            Navigation.findNavController(mParentView).navigate(R.id.sentLettersFragment);
        });

        mLogInSignUpButton.setOnClickListener(view -> {
            Log.d(LOG_TAG, "Log in / Sign up");
            AuthUtils.login(mParentActivity, createLoginSignUpAuthCallback());
        });

        mLaunchViewModel.getIsLoggingIn().observe(this, isLoggingIn -> {
            if (isLoggingIn) {
                mSpinner.setVisibility(View.VISIBLE);
                mGetStartedButton.setVisibility(View.GONE);
                mLogInSignUpButton.setVisibility(View.GONE);
            }
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
                mLaunchViewModel.setIsLoggingIn(true);
                mLaunchViewModel.saveUser(AuthUtils.getUserFromCredentials(credentials));
                Navigation.findNavController(mParentView).navigate(R.id.sentLettersFragment);
            }
        };
    }
}
