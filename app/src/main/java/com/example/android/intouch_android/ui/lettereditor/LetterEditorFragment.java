package com.example.android.intouch_android.ui.lettereditor;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.intouch_android.R;
import com.example.android.intouch_android.model.Inmate;
import com.example.android.intouch_android.model.Inmate$$Parcelable;
import com.example.android.intouch_android.model.Letter;
import com.example.android.intouch_android.utils.Utils;

import org.parceler.Parcels;

import java.util.Arrays;
import java.util.List;

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

    /* ************************************************************ */
    /*                             State                            */
    /* ************************************************************ */
    private Inmate mInmate;
    private Letter mLetter;

    /* ************************************************************ */
    /*                        UI Components                         */
    /* ************************************************************ */
    private View mEditorView;
    private TextView mInmateNameView;

    public LetterEditorFragment() { }

    public static LetterEditorFragment newInstance(String param1, String param2) {
        LetterEditorFragment fragment = new LetterEditorFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setupActionBarInfo();
        setHasOptionsMenu(true);
        setupStateFromBundleArgs();
        Utils.setBottomNavigationVisible(getActivity(), false);

        mEditorView = inflater.inflate(R.layout.fragment_letter_editor, container,false);

        mInmateNameView = (TextView) mEditorView.findViewById(R.id.inmate_search_bar);
        Log.d(LOG_TAG, mInmateNameView.toString());

        mInmateNameView.setOnClickListener(view -> {
            // TODO: Save as draft??? Or at least grab all letter info and pass it as a bundle
            // TODO: Pass actual ID to bundle after modelling the data flow
            Navigation.findNavController(view).navigate(
                    LetterEditorFragmentDirections.searchAction("-1")
            );
        });

        return mEditorView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.options_menu, menu);

        Utils.setupActionBarMenuItems(menu, HIDDEN_MENU_ITEMS);

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
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /* ************************************************************ */
    /*                            Setup Helpers                     */
    /* ************************************************************ */

    private void setupActionBarInfo() {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Write Letter");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupStateFromBundleArgs() {
        Bundle argsBundle = getArguments();
        // From InmateSearchFragment
        if (Utils.containsArgs(argsBundle, "LetterId", "InmateRecipient")) {
            LetterEditorFragmentArgs args = LetterEditorFragmentArgs.fromBundle(argsBundle);

            Inmate$$Parcelable x = args.getInmateRecipient();
            mInmate = Parcels.unwrap(args.getInmateRecipient());
        }
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

    /**
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

