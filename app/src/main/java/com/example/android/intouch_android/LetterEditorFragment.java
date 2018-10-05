package com.example.android.intouch_android;

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
    private static int[] HIDDEN_MENU_ITEMS = { R.id.letter_search };
    private OnFragmentInteractionListener mListener;

    private View mEditorView;

    public LetterEditorFragment() {
        // Required empty public constructor
    }

    public static LetterEditorFragment newInstance(String param1, String param2) {
        LetterEditorFragment fragment = new LetterEditorFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setupActionBar();

        // Allow fragment access to add menu items
        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        mEditorView = inflater.inflate(
                R.layout.fragment_letter_editor,
                container,
                false
        );
        return mEditorView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.options_menu, menu);

        ViewUtils.setupActionBarMenuItems(menu, HIDDEN_MENU_ITEMS);

        MenuItem sendLetterButton = menu.findItem(R.id.send_letter);
        sendLetterButton.setOnMenuItemClickListener(createSendLetterButtonListener());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ViewUtils.dismissKeyboard(getActivity());
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
    /*                            View Helpers                      */
    /* ************************************************************ */

    private void setupActionBar() {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Write Letter");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /* ************************************************************ */
    /*                        Observers/Listeners                   */
    /* ************************************************************ */

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
