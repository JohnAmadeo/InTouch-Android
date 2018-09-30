package com.example.android.intouch_android;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;
import java.util.List;

public class LettersFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private RecyclerView mRecyclerView;
    private LettersAdapter mRecyclerViewAdapter;
    private LinearLayoutManager mLayoutManager;
    private OnListFragmentInteractionListener mListener;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

    /* See "http://developer.android.com/training/basics/fragments/communicating.html" */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Letter item);
    }
}
