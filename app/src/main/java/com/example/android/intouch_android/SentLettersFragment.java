package com.example.android.intouch_android;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import androidx.navigation.Navigation;

public class SentLettersFragment extends Fragment {
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int[] HIDDEN_MENU_ITEMS = { R.id.send_letter };

    /* ************************************************************ */
    /*                        UI Components                         */
    /* ************************************************************ */
    private View mParentView;
    private RecyclerView mRecyclerView;
    private MenuItem mSearchMenuItem;
    private SearchView mSearchView;
    private FloatingActionButton mNewLetterButton;

    /* ************************************************************ */
    /*                           Streams                            */
    /* ************************************************************ */
    private LettersViewModel mLettersViewModel;

    // TODO: Get rid of this variable
    private OnListFragmentInteractionListener mListener;

    /* ************************************************************ */
    /*                      Public Functions                        */
    /* ************************************************************ */

    /* Mandatory empty constructor for the fragment manager to instantiate the fragment */
    public SentLettersFragment() {
    }

    public static SentLettersFragment newInstance(int columnCount) {
        SentLettersFragment fragment = new SentLettersFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Set action bar details
        setupActionBarInfo();

        // Allow fragment access to add menu items
        setHasOptionsMenu(true);

        /* Setup views */
        mParentView = inflater.inflate(
                R.layout.fragment_letters_list,
                container,
                false
        );
        mNewLetterButton = mParentView.findViewById(R.id.new_letter_fab);
        setupRecyclerView();

        /* Setup observers */
        mLettersViewModel =
                ViewModelProviders.of(this).get(LettersViewModel.class);
        mLettersViewModel.getLetters().observe(this, createLettersObserver());

        mNewLetterButton.setOnClickListener(createNewLetterButtonListener());

        return mParentView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.options_menu, menu);

        ViewUtils.setupActionBarMenuItems(menu, HIDDEN_MENU_ITEMS);

        /* Setup views */
        setupSearchView(menu);

        /* Setup observers */
        mSearchView.setOnQueryTextListener(createQueryListener(mLettersViewModel.getLetters()));
        mSearchMenuItem.setOnActionExpandListener(createSearchMenuItemListener());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getActivity().invalidateOptionsMenu();
        mListener = null;
    }

    /* ************************************************************ */
    /*                            View Helpers                      */
    /* ************************************************************ */

    private void setupActionBarInfo() {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("InTouch");
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    private void setupRecyclerView() {
        mRecyclerView = mParentView.findViewById(R.id.fragment_letters_list);
        Context context = mRecyclerView.getContext();
        SentLettersAdapter recyclerViewAdapter = new SentLettersAdapter(mListener);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);

        // set layout of RecyclerView to a list of vertically scrolling items
        mRecyclerView.setLayoutManager(layoutManager);
        // specify what each list item looks like
        mRecyclerView.setAdapter(recyclerViewAdapter);

        // add a line in between each list item
        mRecyclerView.addItemDecoration(
                createListDivider(
                        mRecyclerView,
                        (LinearLayoutManager) mRecyclerView.getLayoutManager()
                )
        );
    }

    private void setupSearchView(Menu menu) {
        mSearchMenuItem = menu.findItem(R.id.letter_search);

        mSearchView = (SearchView) mSearchMenuItem.getActionView();

        // Get configuration options in res/xml/searchable.xml as an object
        SearchManager searchManager =
                (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchableInfo searchableInfo =
                searchManager.getSearchableInfo(getActivity().getComponentName());

        mSearchView.setSearchableInfo(searchableInfo);
    }

    /* ************************************************************ */
    /*                        Observers/Listeners                   */
    /* ************************************************************ */

    private Observer<List<Letter>> createLettersObserver() {
        return new Observer<List<Letter>>() {
            @Override
            public void onChanged(@Nullable List<Letter> letters) {
                getRecyclerViewAdapter().setLetters(letters);
            }
        };
    }

    private SearchView.OnQueryTextListener createQueryListener(
            final LiveData<List<Letter>> letters
    ) {
        return new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String searchQuery) {
                getRecyclerViewAdapter().setLetters(filter(letters.getValue(), searchQuery));
                mRecyclerView.scrollToPosition(0);

                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String searchQuery) {
                ViewUtils.dismissKeyboard(getActivity());
                return true;
            }
        };
    }

    private MenuItem.OnActionExpandListener createSearchMenuItemListener() {
        return new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                mNewLetterButton.hide();
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                mNewLetterButton.show();
                return true;
            }
        };
    }

    private View.OnClickListener createNewLetterButtonListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.letterEditorFragment);
            }
        };
    }

    /* ************************************************************ */
    /*                     Helpers                */
    /* ************************************************************ */

    private DividerItemDecoration createListDivider(
            RecyclerView recyclerView,
            LinearLayoutManager layoutManager
    ) {
        return new DividerItemDecoration(
                recyclerView.getContext(),
                layoutManager.getOrientation()
        );
    }

    private List<Letter> filter(List<Letter> letters, String searchQuery) {
        List<Letter> filteredLetters = new ArrayList<>();
        for (Letter letter:letters) {
            if (letter.contains(searchQuery)) {
                filteredLetters.add(letter);
            }
        }
        return filteredLetters;
    }

    private SentLettersAdapter getRecyclerViewAdapter() {
        return (SentLettersAdapter) mRecyclerView.getAdapter();
    }

    /* See "http://developer.android.com/training/basics/fragments/communicating.html" */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Letter item);
    }
}
