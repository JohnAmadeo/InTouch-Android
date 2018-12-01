package com.example.android.intouch_android.ui.lettereditor;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.intouch_android.MainActivity;
import com.example.android.intouch_android.R;
import com.example.android.intouch_android.model.Letter;
import com.example.android.intouch_android.utils.BackPressListener;
import com.example.android.intouch_android.utils.BackPressSetter;
import com.example.android.intouch_android.utils.ViewUtils;
import com.example.android.intouch_android.utils.VoidFunction;
import com.example.android.intouch_android.viewmodel.LetterEditorViewModel;

import java.util.Arrays;
import java.util.List;

import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;

public class LetterEditorFragment extends Fragment implements BackPressListener {
    private final String LOG_TAG = this.getClass().getSimpleName();
    private static List<Integer> VISIBLE_MENU_ITEMS = Arrays.asList(R.id.send_letter);

    // View Model
    private LetterEditorViewModel mViewModel;
    private CompositeDisposable mDisposables = new CompositeDisposable();
    // UI
    private View mEditorView;
    private TextView mInmateNameView;
    private EditText mSubjectInput;
    private EditText mTextEditor;
    private MenuItem mSendLetterButton;
    private ProgressBar mProgressBar;
    private SendDialogFragment mDialog;


    public LetterEditorFragment() { }

    @Override
    public void onBackPressed() {
        Log.d(LOG_TAG, "#onBackPressed");
        Letter draft = mViewModel.getDraft();
        if (isNonEmptyDraft(draft)) {
            mViewModel.saveDraft(draft);
            showToast(R.string.toast_saved_draft);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewUtils.setActionBarVisible(getActivity(), true);
        ViewUtils.setupActionBarOptions(getActivity(), "Write Letter", true);
        setHasOptionsMenu(true);
        ViewUtils.hideBottomNavigation(getActivity());

        /* -------------------------------------------------------------------------------------- */
        mEditorView = inflater.inflate(R.layout.fragment_letter_editor, container,false);
        mInmateNameView = mEditorView.findViewById(R.id.inmate_search_bar);
        mSubjectInput = mEditorView.findViewById(R.id.subject_input);
        mTextEditor = mEditorView.findViewById(R.id.text_editor);
        mProgressBar = mEditorView.findViewById(R.id.send_letter_progress_bar);
        mDialog = new SendDialogFragment();

        mViewModel = ViewModelProviders.of(this).get(LetterEditorViewModel.class);
        /* -------------------------------------------------------------------------------------- */

        setupFromBundleArgs();

        mInmateNameView.setOnClickListener(view -> {
            mViewModel.saveDraft(mViewModel.getDraft());
            Navigation.findNavController(view).navigate(
                    LetterEditorFragmentDirections.searchAction(
                            mViewModel.getDraft().getId()
                    )
            );
        });

        mViewModel.getInitialDraft().observe(this, draft -> {
            if (draft != null) {
                mInmateNameView.setText(draft.getRecipient());
                mInmateNameView.setTextColor(Color.BLACK);
                mSubjectInput.setText(draft.getSubject());
                mTextEditor.setText(draft.getText());
            }
        });

        mSubjectInput.addTextChangedListener(
                createOnTextChangeListener(mViewModel::setDraftSubject)
        );
        mTextEditor.addTextChangedListener(
                createOnTextChangeListener(mViewModel::setDraftText)
        );

        mDialog.setOnDialogPositiveClickListener(() -> {
            mProgressBar.setIndeterminate(true);
            ViewUtils.dismissKeyboard(getActivity());

            Disposable stream = mViewModel.sendLetter(mViewModel.getDraft())
                    .subscribe(letterId -> {
                        mProgressBar.setIndeterminate(false);
                        Navigation.findNavController(mEditorView).navigate(
                                LetterEditorFragmentDirections.showConfirmation(letterId),
                                new NavOptions.Builder()
                                        .setPopUpTo(R.id.sentLettersFragment, false)
                                        .build()
                        );
                    }, error -> {
                        ViewUtils.dismissKeyboard(getActivity());
                        mProgressBar.setIndeterminate(false);
                        logErrors(error);
                        showSnackbar(R.string.snackbar_send_letter_error);
                    });
            mDisposables.add(stream);
        });

        return mEditorView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.options_menu, menu);

        ViewUtils.setupMenuItems(menu, VISIBLE_MENU_ITEMS);

        mSendLetterButton = menu.findItem(R.id.send_letter);
        mSendLetterButton.setOnMenuItemClickListener(menuItem -> {
            if (isNonEmptyDraft(mViewModel.getDraft())) {
                mDialog.show(getActivity().getSupportFragmentManager(), "SendDialogFragment");
            }
            else {
                ViewUtils.dismissKeyboard(getActivity());
                showSnackbar(R.string.snackbar_empty_letter_error);
            }
            return true;
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ViewUtils.dismissKeyboard(getActivity());
        mDisposables.clear();
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        // Back button on action bar selected
        if (item.getItemId() == android.R.id.home) {
            Log.d(LOG_TAG, "Up Navigation");
            Letter draft = mViewModel.getDraft();
            if (isNonEmptyDraft(draft)) {
                mViewModel.saveDraft(draft);
                showToast(R.string.toast_saved_draft);
            }

            Navigation.findNavController(mEditorView).popBackStack();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        ViewUtils.setSelectedFragment(this);
    }

    /* ************************************************************ */
    /*                            Setup Helpers                     */
    /* ************************************************************ */
    private void setupFromBundleArgs() {
        Bundle argsBundle = getArguments();
        String letterId = null;
        // Load pre-existing letter
        if (ViewUtils.containsArgs(argsBundle, "LetterId")) {
            LetterEditorFragmentArgs args = LetterEditorFragmentArgs.fromBundle(argsBundle);
            letterId = args.getLetterId();

            mViewModel.setLoadRequest(letterId);
        }
    }

    /* ************************************************************ */
    /*                        Observers/Listeners                   */
    /* ************************************************************ */
    private TextWatcher createOnTextChangeListener(VoidFunction<String> onChangeListener) {
        return new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                onChangeListener.apply(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) { }

            @Override
            public void afterTextChanged(Editable s) { }
        };
    }
    /* ************************************************************ */
    /*                           Helpers                            */
    /* ************************************************************ */

    private boolean isNonEmptyDraft(Letter draft) {
        return draft.getText().trim().length() > 0 ||
                draft.getSubject().trim().length() > 0 ||
                draft.getRecipient() != null;
    }

    private void showSnackbar(int messageResId) {
        Snackbar snackbar = Snackbar.make(
                mEditorView,
                messageResId,
                Snackbar.LENGTH_LONG
        );
        snackbar.setAction("Dismiss", view -> snackbar.dismiss());
        snackbar.show();
    }

    private void showToast(int messageResId) {
        Toast toast = Toast.makeText(getContext(), getString(messageResId), Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 200);
        toast.show();
    }

    private void logErrors(Throwable error) {
        if (error instanceof CompositeException) {
            for (Throwable singleError:((CompositeException) error).getExceptions()) {
                Log.d(LOG_TAG, singleError.getMessage());
            }
        } else {
            Log.d(LOG_TAG, error.getMessage());
        }
    }
 }
