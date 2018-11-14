package com.example.android.intouch_android.ui.drafts;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.intouch_android.R;
import com.example.android.intouch_android.common.LettersAdapter;
import com.example.android.intouch_android.utils.ViewUtils;
import com.example.android.intouch_android.viewmodel.DraftsViewModel;

import java.util.Arrays;
import java.util.List;

import androidx.navigation.Navigation;

public class DraftsFragment extends Fragment {
    private final String LOG_TAG = this.getClass().getSimpleName();
    private List<Integer> VISIBLE_MENU_ITEMS = Arrays.asList(R.id.menu_search);

    private View mParentView;
    private RecyclerView mRecyclerView;
    private MenuItem mSearchMenuItem;
    private SearchView mSearchView;

    private DraftsViewModel mViewModel;

    public DraftsFragment() { }

    @Override
    public void onAttach(Context context) { super.onAttach(context); }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewUtils.setActionBarVisible(getActivity(), true);
        ViewUtils.setupActionBarOptions(getActivity(), "InTouch", false);
        setHasOptionsMenu(true);

        mParentView = inflater.inflate(R.layout.fragment_drafts, container, false);

        setupRecyclerView();
        ViewUtils.setupBottomNavigation(
                getActivity(),
                mParentView,
                R.id.navigation_drafts,
                R.id.draftsFragment,
                () -> { mSearchMenuItem.collapseActionView(); }
        );

        mViewModel = ViewModelProviders.of(this).get(DraftsViewModel.class);
        mViewModel.getDisplayedDrafts().observe(this, drafts -> {
            getRecyclerViewAdapter().setLetters(drafts);
        });

        return mParentView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.options_menu, menu);

        ViewUtils.setupMenuItems(menu, VISIBLE_MENU_ITEMS);

        Pair<MenuItem, SearchView> views = ViewUtils.setupSearch(
                menu,
                R.string.drafts_search_hint,
                getActivity()
        );

        mSearchMenuItem = views.first;
        mSearchView = views.second;

        mSearchView.setOnQueryTextListener(createQueryListener());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onStart() {
        super.onStart();
        ViewUtils.setSelectedFragment(this);
    }

    /* ************************************************************ */
    /*                            Setup Helpers                     */
    /* ************************************************************ */
    private void setupRecyclerView() {
        mRecyclerView = mParentView.findViewById(R.id.fragment_drafts);
        Context context = mRecyclerView.getContext();
        LettersAdapter recyclerViewAdapter = new LettersAdapter(letter -> Log.d(LOG_TAG, "Clicked"));
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

    /* ************************************************************ */
    /*                            Listeners                         */
    /* ************************************************************ */
    private SearchView.OnQueryTextListener createQueryListener() {
        return new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String searchQuery) {
                mViewModel.setQuery(searchQuery);
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

    /* ************************************************************ */
    /*                              Helpers                         */
    /* ************************************************************ */

    private LettersAdapter getRecyclerViewAdapter() {
        return (LettersAdapter) mRecyclerView.getAdapter();
    }
}
