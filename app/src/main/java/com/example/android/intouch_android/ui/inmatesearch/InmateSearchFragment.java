package com.example.android.intouch_android.ui.inmatesearch;

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
import com.example.android.intouch_android.model.Inmate;
import com.example.android.intouch_android.utils.OnListItemClickListener;
import com.example.android.intouch_android.utils.ViewUtils;
import com.example.android.intouch_android.viewmodel.InmateSearchViewModel;

import java.util.Arrays;
import java.util.List;

import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

public class InmateSearchFragment
        extends Fragment {
    private final String LOG_TAG = this.getClass().getSimpleName();
    private List<Integer> VISIBLE_MENU_ITEMS = Arrays.asList(R.id.menu_search);

    /* ************************************************************ */
    /*                           State                              */
    /* ************************************************************ */
    private String mLetterId;
    private InmateSearchViewModel mInmateSearchViewModel;

    /* ************************************************************ */
    /*                        UI Components                         */
    /* ************************************************************ */
    private View mParentView;
    private MenuItem mSearchMenuItem;
    private SearchView mSearchView;

    private RecyclerView mRecyclerView;

    public InmateSearchFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewUtils.setActionBarVisible(getActivity(), true);
        ViewUtils.setupActionBarOptions(getActivity(), "", false);
        setHasOptionsMenu(true);
        ViewUtils.hideBottomNavigation(getActivity());
        setupStateFromBundleArgs();

        mParentView = inflater.inflate(R.layout.fragment_inmate_search, container,false);
        setupRecyclerView();

        /* Setup observers */
        mInmateSearchViewModel =
                ViewModelProviders.of(this).get(InmateSearchViewModel.class);

        mInmateSearchViewModel.getInmates().observe(this, resource -> {
            if (resource != null && resource.data != null) {
                List<Inmate> inmates = resource.data;
                getRecyclerViewAdapter().setInmates(inmates);
            }
        });

        return mParentView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.options_menu, menu);

        ViewUtils.setupMenuItems(menu, VISIBLE_MENU_ITEMS);

        /* Setup views */
        Pair<MenuItem, SearchView> views = ViewUtils.setupSearch(
                menu,
                R.string.inmate_search_hint,
                getActivity()
        );
        mSearchMenuItem = views.first;
        mSearchView = views.second;

        mSearchView.setOnQueryTextListener(createQueryListener());
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPrepareOptionsMenu (Menu menu) {
        // Auto-focus on search bar on loading the fragment
        mSearchMenuItem.expandActionView();
        // Go back to letter editor on hitting up button
        mSearchMenuItem.setOnActionExpandListener(createSearchCollapseListener());
    }
    /* ************************************************************ */
    /*                            Setup Helpers                     */
    /* ************************************************************ */
    private void setupRecyclerView() {
        mRecyclerView = mParentView.findViewById(R.id.inmates_list);
        Context context = mRecyclerView.getContext();
        InmateSearchAdapter recyclerViewAdapter = new InmateSearchAdapter(
                createInmateOnClickListener()
        );

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(recyclerViewAdapter);

        // add a line in between each list item
        mRecyclerView.addItemDecoration(
                ViewUtils.createListDivider(
                        mRecyclerView,
                        (LinearLayoutManager) mRecyclerView.getLayoutManager()
                )
        );
    }

    private void setupStateFromBundleArgs() {
        Bundle argsBundle = getArguments();
        if (ViewUtils.containsArgs(argsBundle, "LetterId")) {
            InmateSearchFragmentArgs args = InmateSearchFragmentArgs.fromBundle(argsBundle);
            mLetterId = args.getLetterId();
        }
    }

    /* ************************************************************ */
    /*                             Listeners                        */
    /* ************************************************************ */

    private SearchView.OnQueryTextListener createQueryListener() {
        return new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String searchQuery) {
                mInmateSearchViewModel.setQuery(searchQuery);
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

    private MenuItem.OnActionExpandListener createSearchCollapseListener() {
        return new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                Navigation.findNavController(mParentView).navigate(
                        InmateSearchFragmentDirections.selectInmateAction(mLetterId),
                        new NavOptions.Builder()
                                .setPopUpTo(R.id.letterEditorFragment, false)
                                .build()
                );
                return true;
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) { return true; }
        };
    }

    private OnListItemClickListener<Inmate> createInmateOnClickListener() {
        return inmate -> {
            mInmateSearchViewModel.updateDraftRecipient(
                    mLetterId,
                    inmate.getId(),
                    inmate.getName()
            );

            // NOTE: VERY UGLY!
            // Since we need to a) collapse the action view to prevent it from appearing on the
            // fragment we want to transition to, and b) we have an event listener attached
            // on the action view to handle the case where the user wants to navigate back w/o
            // picking an inmate, we have to delegate navigation to the collapse listener. Otherwise
            // we will try to navigate back 2x and throw and error
            mSearchMenuItem.collapseActionView();
        };
    }

    /* ************************************************************ */
    /*                              Helpers                         */
    /* ************************************************************ */

    private InmateSearchAdapter getRecyclerViewAdapter() {
        return (InmateSearchAdapter) mRecyclerView.getAdapter();
    }
}
