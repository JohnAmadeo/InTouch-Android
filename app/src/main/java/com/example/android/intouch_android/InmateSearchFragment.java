package com.example.android.intouch_android;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.intouch_android.utils.ViewUtils;
import com.example.android.intouch_android.viewmodel.InmateSearchViewModel;
import com.example.android.intouch_android.viewmodel.SentLettersViewModel;

import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InmateSearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InmateSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InmateSearchFragment extends Fragment {
    private final String LOG_TAG = this.getClass().getSimpleName();
    private List<Integer> HIDDEN_MENU_ITEMS = Arrays.asList(R.id.send_letter);

    /* ************************************************************ */
    /*                        UI Components                         */
    /* ************************************************************ */
    private MenuItem mSearchMenuItem;
    private SearchView mSearchView;

    /* ************************************************************ */
    /*                           Streams                            */
    /* ************************************************************ */

    private OnFragmentInteractionListener mListener;

    public InmateSearchFragment() { }
    public static InmateSearchFragment newInstance(String param1, String param2) {
        InmateSearchFragment fragment = new InmateSearchFragment();
        fragment.setArguments(new Bundle());
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
        Log.d(LOG_TAG, InmateSearchFragmentArgs.fromBundle(getArguments()).getLetterId());
        setupActionBarInfo();

        setHasOptionsMenu(true);

        ViewUtils.dismissKeyboard(getActivity());


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inmate_search, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(LOG_TAG, "onCreateOptionsMenu");
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.options_menu, menu);

        ViewUtils.setupActionBarMenuItems(menu, HIDDEN_MENU_ITEMS);

        /* Setup views */
        setupSearchView(menu);

//        /* Setup observers */
        mSearchView.setOnQueryTextListener(createQueryListener());
//        mSearchMenuItem.setOnActionExpandListener(createSearchMenuItemListener());
        mSearchView.setQuery("TEST", false);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        Log.d(LOG_TAG, "onOptionsItemSelected");
        return false;
    }

    @Override
    public void onPrepareOptionsMenu (Menu menu) {
        Log.d(LOG_TAG, "onPrepareOptionsMenu");
        // Auto-focus on search bar on loading the fragment
        mSearchMenuItem.expandActionView();
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /* ************************************************************ */
    /*                            View Helpers                      */
    /* ************************************************************ */

    private void setupActionBarInfo() {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupSearchView(Menu menu) {
        mSearchMenuItem = menu.findItem(R.id.menu_search);

        mSearchView = (SearchView) mSearchMenuItem.getActionView();

        mSearchView.setQueryHint(getString(R.string.inmate_search_hint));

        // Get configuration options in res/xml/searchable.xml as an object
        SearchManager searchManager =
                (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchableInfo searchableInfo =
                searchManager.getSearchableInfo(getActivity().getComponentName());

        mSearchView.setSearchableInfo(searchableInfo);
    }

    private SearchView.OnQueryTextListener createQueryListener() {
        return new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String searchQuery) {
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String searchQuery) {
                ViewUtils.dismissKeyboard(getActivity());
                return true;
            }
        };
    }


    /* See "http://developer.android.com/training/basics/fragments/communicating.html" */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
