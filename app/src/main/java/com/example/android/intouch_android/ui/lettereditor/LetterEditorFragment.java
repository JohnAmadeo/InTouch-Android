package com.example.android.intouch_android.ui.lettereditor;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.intouch_android.R;
import com.example.android.intouch_android.model.Letter;
import com.example.android.intouch_android.utils.Utils;
import com.example.android.intouch_android.utils.VoidFunction;
import com.example.android.intouch_android.viewmodel.LetterEditorViewModel;

import java.util.Arrays;
import java.util.List;

import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LetterEditorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LetterEditorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LetterEditorFragment extends Fragment {
    private final String LOG_TAG = this.getClass().getSimpleName();
    private static List<Integer> HIDDEN_MENU_ITEMS = Arrays.asList(R.id.menu_search);
    private OnFragmentInteractionListener mListener;

    // View Model
    private LetterEditorViewModel mViewModel;
    // UI
    private View mEditorView;
    private TextView mInmateNameView;
    private EditText mSubjectInput;
    private EditText mTextEditor;

    public LetterEditorFragment() { }

    public static LetterEditorFragment newInstance(String param1, String param2) {
        LetterEditorFragment fragment = new LetterEditorFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Utils.setupActionBarOptions(getActivity(), "Write Letter", true);
        setHasOptionsMenu(true);
        Utils.setBottomNavigationVisible(getActivity(), false);

        /* -------------------------------------------------------------------------------------- */
        mEditorView = inflater.inflate(R.layout.fragment_letter_editor, container,false);
        mInmateNameView = mEditorView.findViewById(R.id.inmate_search_bar);
        mSubjectInput = mEditorView.findViewById(R.id.subject_input);
        mTextEditor = mEditorView.findViewById(R.id.text_editor);

        mViewModel = ViewModelProviders.of(this).get(LetterEditorViewModel.class);
        /* -------------------------------------------------------------------------------------- */

        setupFromBundleArgs();

        mInmateNameView.setOnClickListener(view -> {
            mViewModel.saveDraft(mViewModel.getDraft().getValue());
            Navigation.findNavController(view).navigate(
                    LetterEditorFragmentDirections.searchAction(
                            mViewModel.getDraft().getValue().getId()
                    )
            );
        });

        mViewModel.getDraft().observe(this, draft -> Log.d(LOG_TAG, draft == null ? "null" : draft.toString()));

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

        return mEditorView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.options_menu, menu);

        Utils.setupMenuItems(menu, HIDDEN_MENU_ITEMS);

        MenuItem sendLetterButton = menu.findItem(R.id.send_letter);
        sendLetterButton.setOnMenuItemClickListener(createSendLetterButtonListener());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Utils.dismissKeyboard(getActivity());
        mListener = null;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        // Back button on action bar selected
        if (item.getItemId() == android.R.id.home) {
            boolean draftSaved = false;
            Letter draft = mViewModel.getDraft().getValue();
            if (shouldSaveAsDraft(draft)) {
                mViewModel.saveDraft(draft);
                draftSaved = true;
            }

            Navigation.findNavController(mEditorView).navigate(
                    LetterEditorFragmentDirections.exitEditorAction(draftSaved),
                    new NavOptions.Builder()
                            .setPopUpTo(R.id.sentLettersFragment, false)
                            .build()
            );
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /* ************************************************************ */
    /*                            Setup Helpers                     */
    /* ************************************************************ */
    private void setupFromBundleArgs() {
        Bundle argsBundle = getArguments();
        String letterId = null;
        // Load pre-existing letter
        if (Utils.containsArgs(argsBundle, "LetterId")) {
            LetterEditorFragmentArgs args = LetterEditorFragmentArgs.fromBundle(argsBundle);
            letterId = args.getLetterId();
        }

        mViewModel.setLoadRequest(letterId);
    }

    /* ************************************************************ */
    /*                        Observers/Listeners                   */
    /* ************************************************************ */

    // TODO: Replace with lambda function
    private MenuItem.OnMenuItemClickListener createSendLetterButtonListener() {
        return new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                // TODO: Create new letter, and if first letter sent, reroute to new-user sign up
                // Navigation.findNavController(mEditorView).navigate(R.id.FILL_IN_ID);
                return true;
            }
        };
    }

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

    private boolean shouldSaveAsDraft(Letter draft) {
        return draft.getText().length() > 0 ||
                draft.getSubject().length() > 0 ||
                draft.getRecipientId() != null;
    }


    /**
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

