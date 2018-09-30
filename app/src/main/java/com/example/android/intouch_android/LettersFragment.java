package com.example.android.intouch_android;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;
import java.util.List;

public class LettersFragment extends Fragment implements SearchView.OnQueryTextListener {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private RecyclerView mRecyclerView;
    private LettersAdapter mRecyclerViewAdapter;
    private LinearLayoutManager mLayoutManager;
    private OnListFragmentInteractionListener mListener;
    private MutableLiveData<String> mSearchQueries = new MutableLiveData<>();

    /* ************************************************************ */
    /*                      Public Functions                        */
    /* ************************************************************ */

    /* Mandatory empty constructor for the fragment manager to instantiate the fragment */
    public LettersFragment() {
    }

    public static LettersFragment newInstance(int columnCount) {
        LettersFragment fragment = new LettersFragment();
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
        // Allow fragment access to add menu items
        setHasOptionsMenu(true);

        // Associate the Letters fragment with the letters list XML
        View view = inflater.inflate(R.layout.fragment_letters_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            mRecyclerViewAdapter = new LettersAdapter(mListener);
            mLayoutManager = new LinearLayoutManager(context);

            /* Update the letters list whenever new letters are received */
            LettersViewModel lettersViewModel =
                    ViewModelProviders.of(this).get(LettersViewModel.class);

            lettersViewModel.getLetters().observe(
                    this,
                    createLettersObserver(mRecyclerViewAdapter)
            );

            mSearchQueries.observe(
                    this,
                    createQueryObserver(
                            lettersViewModel.getLetters(),
                            mRecyclerViewAdapter,
                            mRecyclerView
                    )
            );

            /* Configure the RecyclerView */

            // set layout of RecyclerView to a list of vertically scrolling items
            mRecyclerView.setLayoutManager(mLayoutManager);
            // specify what each list item looks like
            mRecyclerView.setAdapter(mRecyclerViewAdapter);
            // add a line in between each list item
            mRecyclerView.addItemDecoration(createListDivider(mRecyclerView, mLayoutManager));
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.options_menu, menu);

        // Get configuration options in res/xml/searchable.xml as an object
        SearchManager searchManager =
                (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchableInfo searchableInfo =
                searchManager.getSearchableInfo(getActivity().getComponentName());

        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchableInfo);
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean onQueryTextChange(String searchQuery) {
        mSearchQueries.setValue(searchQuery);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String searchQuery) {
        Log.w("onQueryTextSubmit", "Press enter");

        // Dismiss keyboard
        InputMethodManager inputMethodManager =
                (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(mRecyclerView.getWindowToken(), 0);

        return true;
    }

    /* ************************************************************ */
    /*                      Private Functions                       */
    /* ************************************************************ */

    private Observer<List<Letter>> createLettersObserver(final LettersAdapter adapter) {
        return new Observer<List<Letter>>() {
            @Override
            public void onChanged(@Nullable final List<Letter> letters) {
                adapter.setLetters(letters);
            }
        };
    }

    private DividerItemDecoration createListDivider(
            RecyclerView recyclerView,
            LinearLayoutManager layoutManager
    ) {
        return new DividerItemDecoration(
                recyclerView.getContext(),
                layoutManager.getOrientation()
        );
    }

    private Observer<String> createQueryObserver(
            final LiveData<List<Letter>> letters,
            final LettersAdapter adapter,
            final RecyclerView recyclerView
    ) {
        return new Observer<String>() {
            @Override
            public void onChanged(@Nullable String searchQuery) {
                adapter.setLetters(filter(letters.getValue(), searchQuery));
                recyclerView.scrollToPosition(0);
            }
        };
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

    /* See "http://developer.android.com/training/basics/fragments/communicating.html" */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Letter item);
    }
}
