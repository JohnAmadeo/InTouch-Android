package com.example.android.intouch_android.ui.sentletters;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.intouch_android.R;
import com.example.android.intouch_android.utils.ViewUtils;
import com.example.android.intouch_android.viewmodel.SentLettersViewModel;

import java.util.Arrays;
import java.util.List;

import androidx.navigation.Navigation;

public class SentLettersFragment extends Fragment {
    private final String LOG_TAG = this.getClass().getSimpleName();
    private static final String ARG_COLUMN_COUNT = "column-count";
    private List<Integer> HIDDEN_MENU_ITEMS = Arrays.asList(R.id.send_letter);

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
    private SentLettersViewModel mSentLettersViewModel;

    /* ************************************************************ */
    /*                      Public Functions                        */
    /* ************************************************************ */

    /* Mandatory empty constructor for the fragment manager to instantiate the fragment */
    public SentLettersFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewUtils.setActionBarVisible(getActivity(), true);
        ViewUtils.setupActionBarOptions(getActivity(), "InTouch", false);
        setHasOptionsMenu(true);
        ViewUtils.setBottomNavigationVisible(getActivity(), true);

        /* Setup views */
        mParentView = inflater.inflate(R.layout.fragment_letters_list, container, false);
        mNewLetterButton = mParentView.findViewById(R.id.new_letter_fab);
        setupRecyclerView();

        /* Setup observers */
        mSentLettersViewModel =
                ViewModelProviders.of(this).get(SentLettersViewModel.class);

        mSentLettersViewModel.getDisplayedLetters().observe(this, letters -> {
            getRecyclerViewAdapter().setLetters(letters);
        });

        mNewLetterButton.setOnClickListener(
                view -> Navigation.findNavController(view).navigate(R.id.letterEditorFragment)
        );

        setupFromBundleArgs();

        return mParentView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.options_menu, menu);

        ViewUtils.setupMenuItems(menu, HIDDEN_MENU_ITEMS);

        /* Setup views */
        setupSearchView(menu);

        mSearchView.setOnQueryTextListener(createQueryListener());
        mSearchMenuItem.setOnActionExpandListener(createSearchMenuItemListener());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getActivity().invalidateOptionsMenu();
    }

    /* ************************************************************ */
    /*                            Setup Helpers                     */
    /* ************************************************************ */
    private void setupRecyclerView() {
        mRecyclerView = mParentView.findViewById(R.id.fragment_letters_list);
        Context context = mRecyclerView.getContext();
        SentLettersAdapter recyclerViewAdapter = new SentLettersAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);

        // set layout of RecyclerView to a list of vertically scrolling items
        mRecyclerView.setLayoutManager(layoutManager);
        // specify what each list item looks like
        mRecyclerView.setAdapter(recyclerViewAdapter);

        // add a line in between each list item
        mRecyclerView.addItemDecoration(
                ViewUtils.createListDivider(
                        mRecyclerView,
                        (LinearLayoutManager) mRecyclerView.getLayoutManager()
                )
        );
    }

    private void setupSearchView(Menu menu) {
        mSearchMenuItem = menu.findItem(R.id.menu_search);

        mSearchView = (SearchView) mSearchMenuItem.getActionView();

        mSearchView.setQueryHint(getString(R.string.letter_search_hint));

        // Get configuration options in res/xml/searchable.xml as an object
        SearchManager searchManager =
                (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchableInfo searchableInfo =
                searchManager.getSearchableInfo(getActivity().getComponentName());

        mSearchView.setSearchableInfo(searchableInfo);
    }

    private void setupFromBundleArgs() {
        Bundle argsBundle = getArguments();
        if (ViewUtils.containsArgs(argsBundle, "DraftSaved")) {
            SentLettersFragmentArgs args = SentLettersFragmentArgs.fromBundle(argsBundle);

            if (args.getDraftSaved()) {
                Toast toast = Toast.makeText(getContext(), "Saved as draft!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM, 0, 200);
                toast.show();
            }
        }
    }

    /* ************************************************************ */
    /*                            Listeners                         */
    /* ************************************************************ */
    private SearchView.OnQueryTextListener createQueryListener() {
        return new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String searchQuery) {
                mSentLettersViewModel.setQuery(searchQuery);
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

    /* ************************************************************ */
    /*                              Helpers                         */
    /* ************************************************************ */


    private SentLettersAdapter getRecyclerViewAdapter() {
        return (SentLettersAdapter) mRecyclerView.getAdapter();
    }
}
