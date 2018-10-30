package com.example.android.intouch_android.ui.launch;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.android.intouch_android.R;
import com.example.android.intouch_android.utils.Utils;

import androidx.navigation.Navigation;

public class LaunchFragment extends Fragment {
    private final String LOG_TAG = this.getClass().getSimpleName();

    private Activity mParentActivity;
    private View mParentView;
    private Button mGetStartedButton;
    private Button mLogInSignUpButton;

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

        mGetStartedButton.setOnClickListener(view -> {
            Navigation.findNavController(mParentView).navigate(R.id.sentLettersFragment);
        });
        mLogInSignUpButton.setOnClickListener(view -> Log.d(LOG_TAG, "Log in / Sign up"));

        return mParentView;
    }

}
