package com.example.android.intouch_android.ui.profile;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.provider.AuthCallback;
import com.auth0.android.result.Credentials;
import com.example.android.intouch_android.R;
import com.example.android.intouch_android.model.User;
import com.example.android.intouch_android.utils.AuthUtils;
import com.example.android.intouch_android.utils.ViewUtils;
import com.example.android.intouch_android.viewmodel.ProfileViewModel;

import java.util.Arrays;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class ProfileFragment extends Fragment {
    private final String LOG_TAG = this.getClass().getSimpleName();
    private List<Integer> VISIBLE_MENU_ITEMS = Arrays.asList(R.id.logout);

    private View mParentView;
    private TextView mUsernameView;
    private TextView mEmailView;
    private TextView mSignupPrompt;
    private MenuItem mLogout;

    private ProfileViewModel mViewModel;
    private CompositeDisposable mDisposables = new CompositeDisposable();

    public ProfileFragment() { }

    @Override
    public void onAttach(Context context) { super.onAttach(context); }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewUtils.setActionBarVisible(getActivity(), true);
        ViewUtils.setupActionBarOptions(getActivity(), "InTouch", false);
        setHasOptionsMenu(true);

        mParentView = inflater.inflate(R.layout.fragment_profile, container, false);
        mUsernameView = mParentView.findViewById(R.id.profile_username);
        mEmailView = mParentView.findViewById(R.id.profile_email);
        mSignupPrompt = mParentView.findViewById(R.id.profile_signup_prompt);

        mViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);

        User user = mViewModel.getUser();
        if (user.isPlaceholderUser()) {
            mUsernameView.setVisibility(View.GONE);
            mEmailView.setVisibility(View.GONE);
            mSignupPrompt.setVisibility(View.VISIBLE);

            mSignupPrompt.setText(R.string.profile_signup_prompt);

            mParentView.setOnClickListener(view -> {
                AuthUtils.authenticate(getActivity(), new AuthCallback() {
                    @Override
                    public void onFailure(@NonNull Dialog dialog) { dialog.show(); }

                    @Override
                    public void onFailure(AuthenticationException e) { Log.d(LOG_TAG, e.toString()); }

                    @Override
                    public void onSuccess(@NonNull Credentials credentials) {
                        mDisposables.add(
                                mViewModel
                                        .saveUser(AuthUtils.getUserFromCredentials(credentials))
                                        .subscribe(__ -> {
                                            displayUserProfile(mViewModel.getUser());
                                            mParentView.setEnabled(false);
                                        })
                        );
                    }
                });
            });
        } else {
            displayUserProfile(user);
        }

        ViewUtils.setupBottomNavigation(
                getActivity(),
                mParentView,
                R.id.navigation_profile,
                R.id.profileFragment,
                null
        );

        return mParentView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.options_menu, menu);

        ViewUtils.setupMenuItems(menu, VISIBLE_MENU_ITEMS);

        mLogout = menu.findItem(R.id.logout);
        mLogout.setOnMenuItemClickListener(menuItem -> {
            AuthUtils.logout(getActivity(), mParentView, R.id.sentLettersFragment);
            return true;
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mDisposables.clear();
    }

    @Override
    public void onStart() {
        super.onStart();
        ViewUtils.setSelectedFragment(this);
    }

    private void displayUserProfile(User user) {
        mUsernameView.setVisibility(View.VISIBLE);
        mEmailView.setVisibility(View.VISIBLE);
        mSignupPrompt.setVisibility(View.GONE);

        mUsernameView.setText(getString(R.string.profile_username_label, user.getUsername()));
        mEmailView.setText(getString(R.string.profile_email_label, user.getEmail()));
    }
}
