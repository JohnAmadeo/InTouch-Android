package com.example.android.intouch_android;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.intouch_android.ui.lettereditor.LetterEditorFragmentArgs;
import com.example.android.intouch_android.utils.ViewUtils;
import com.example.android.intouch_android.viewmodel.LetterViewerViewModel;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class LetterViewerFragment extends Fragment {
    private final String LOG_TAG = this.getClass().getSimpleName();

    private View mParentView;
    private TextView mRecipientView;
    private TextView mSubjectView;
    private TextView mLetterTextView;

    private LetterViewerViewModel mViewModel;
    private CompositeDisposable mDisposables = new CompositeDisposable();

    public LetterViewerFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewUtils.setActionBarVisible(getActivity(), true);
        ViewUtils.setupActionBarOptions(getActivity(), "", true);
        setHasOptionsMenu(true);
        ViewUtils.hideBottomNavigation(getActivity());

        mParentView = inflater.inflate(R.layout.fragment_letter_viewer, container, false);
        mRecipientView = mParentView.findViewById(R.id.letter_viewer_recipient);
        mSubjectView = mParentView.findViewById(R.id.letter_viewer_subject);
        mLetterTextView = mParentView.findViewById(R.id.letter_viewer_text);
        mLetterTextView.setMovementMethod(new ScrollingMovementMethod());

        mViewModel = ViewModelProviders.of(this).get(LetterViewerViewModel.class);

        setupFromBundleArgs();

        return mParentView;
    }

    @Override
    public void onAttach(Context context) { super.onAttach(context); }

    @Override
    public void onDetach() {
        super.onDetach();
        mDisposables.clear();
    }

    /* ************************************************************ */
    /*                            Setup Helpers                     */
    /* ************************************************************ */
    private void setupFromBundleArgs() {
        Bundle argsBundle = getArguments();
        // Load  letter
        if (ViewUtils.containsArgs(argsBundle, "LetterId")) {
            LetterViewerFragmentArgs args = LetterViewerFragmentArgs.fromBundle(argsBundle);
            String letterId = args.getLetterId();

            Disposable disp = mViewModel.getLetter(letterId)
                    .subscribe(letter -> {
                        mRecipientView.setText(letter.getRecipient());

                        if (letter.getSubject().isEmpty()) {
                            mSubjectView.setVisibility(View.GONE);
                            mParentView.findViewById(R.id.letter_viewer_border_two)
                                    .setVisibility(View.GONE);
                        }
                        else {
                            mSubjectView.setText(letter.getSubject());
                        }

                        mLetterTextView.setText(letter.getText());
                    });

            mDisposables.add(disp);
        }
    }
}
