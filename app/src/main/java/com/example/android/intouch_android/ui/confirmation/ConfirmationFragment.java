package com.example.android.intouch_android.ui.confirmation;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.provider.AuthCallback;
import com.auth0.android.result.Credentials;
import com.example.android.intouch_android.LetterViewerFragmentArgs;
import com.example.android.intouch_android.R;
import com.example.android.intouch_android.utils.AuthUtils;
import com.example.android.intouch_android.utils.ViewUtils;
import com.example.android.intouch_android.viewmodel.ConfirmationViewModel;

import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


public class ConfirmationFragment extends Fragment {
    private final String LOG_TAG = this.getClass().getSimpleName();
    private View mParentView;
    private ImageButton mCloseButton;
    private TextView mConfirmation;
//    private Button mSignupButton;
//    private TextView mSignupText;

    private ConfirmationViewModel mViewModel;
    private CompositeDisposable mDisposables = new CompositeDisposable();

    public ConfirmationFragment() { }

    @Override
    public void onAttach(Context context) { super.onAttach(context); }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewUtils.setActionBarVisible(getActivity(), false);
        setHasOptionsMenu(false);
        ViewUtils.hideBottomNavigation(getActivity());

        // Inflate the layout for this fragment
        mParentView = inflater.inflate(R.layout.fragment_confirmation, container, false);
        mCloseButton = mParentView.findViewById(R.id.confirmation_close_button);
        mConfirmation = mParentView.findViewById(R.id.confirmation_text);
//        mSignupButton = mParentView.findViewById(R.id.signup_button);
//        mSignupText= mParentView.findViewById(R.id.placeholder_user_signup_text);

        mViewModel = ViewModelProviders.of(this).get(ConfirmationViewModel.class);

//        getString(R.string.confirmation_letter_text, "asdf");

//        if (!viewModel.isPlaceholderUser()) {
//            mSignupButton.setVisibility(View.GONE);
//            mSignupText.setVisibility(View.GONE);
//        }

        mCloseButton.setOnClickListener(view -> navigateHome());

//        mSignupButton.setOnClickListener(view -> {
//            AuthUtils.signup(getActivity(), new AuthCallback() {
//                @Override
//                public void onFailure(@NonNull Dialog dialog) { dialog.show(); }
//
//                @Override
//                public void onFailure(AuthenticationException e) { Log.d(LOG_TAG, e.toString()); }
//
//                @Override
//                public void onSuccess(@NonNull Credentials credentials) {
//                    Log.d(LOG_TAG, credentials.getAccessToken());
//                    viewModel.saveUser(AuthUtils.getUserFromCredentials(credentials));
//                    navigateHome();
//                }
//            });
//        });

        setupFromBundleArgs();

        return mParentView;
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

    /* ************************************************************ */
    /*                               Helpers                        */
    /* ************************************************************ */
    private void setupFromBundleArgs() {
        Bundle argsBundle = getArguments();
        // Load  letter
        if (ViewUtils.containsArgs(argsBundle, "LetterId")) {
            LetterViewerFragmentArgs args = LetterViewerFragmentArgs.fromBundle(argsBundle);
            String letterId = args.getLetterId();

            Disposable disp = mViewModel.getTimeDeliveredEstimate(letterId)
                    .subscribe(timeDeliveredEstimate -> {
                        String confirmation = getString(
                                R.string.confirmation_letter_text,
                                timeDeliveredEstimate
                        );
                        mConfirmation.setText(confirmation);
                    });

            mDisposables.add(disp);
        }
    }

    private void navigateHome() {
        Navigation.findNavController(mParentView).navigate(
                R.id.sentLettersFragment,
                null,
                new NavOptions.Builder()
                        .setPopUpTo(
                                R.id.sentLettersFragment,
                                true
                        ).build()
        );
    }
}
