package com.example.android.intouch_android.ui.inmatesearch;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.intouch_android.R;
import com.example.android.intouch_android.model.Inmate;
import com.example.android.intouch_android.model.Inmate$$Parcelable;
import com.example.android.intouch_android.model.Letter;
import com.example.android.intouch_android.utils.OnListItemClickListener;
import com.example.android.intouch_android.utils.ViewUtils;
import com.example.android.intouch_android.viewmodel.InmateSearchViewModel;

import org.parceler.Parcels;

import java.util.Arrays;
import java.util.List;

import androidx.navigation.Navigation;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InmateSearchFragment.OnListFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InmateSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InmateSearchFragment
        extends Fragment {
    private final String LOG_TAG = this.getClass().getSimpleName();
    private List<Integer> HIDDEN_MENU_ITEMS = Arrays.asList(R.id.send_letter);
    private String mLetterId;

    /* ************************************************************ */
    /*                        UI Components                         */
    /* ************************************************************ */
    private View mParentView;
    private MenuItem mSearchMenuItem;
    private SearchView mSearchView;

    private RecyclerView mRecyclerView;

    /* ************************************************************ */
    /*                           Streams                            */
    /* ************************************************************ */

    private InmateSearchViewModel mInmateSearchViewModel;
    private OnListFragmentInteractionListener mListener;

    public InmateSearchFragment() { }
    public static InmateSearchFragment newInstance(String param1, String param2) {
        InmateSearchFragment fragment = new InmateSearchFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
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
        setupActionBarInfo();
        setHasOptionsMenu(true);
        ViewUtils.hideBottomNavigation(getActivity());
        setupStateFromBundleArgs();

        mParentView = inflater.inflate(R.layout.fragment_inmate_search, container,false);
        setupRecyclerView();

        // TODO: Need to dismiss keyboard appearing from clicking on SearchView in previous Activity.
        // TODO: Can improve by changing UI element in LetterEditorFragment to a non-SearchView/EditView UI
        ViewUtils.dismissKeyboard(getActivity());

        /* Setup observers */
        mInmateSearchViewModel =
                ViewModelProviders.of(this).get(InmateSearchViewModel.class);

        mInmateSearchViewModel.getInmates().observe(this, inmates -> {
            if (inmates != null && inmates.data != null) {
                getRecyclerViewAdapter().setInmates(inmates.data);
            }
        });

        return mParentView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.options_menu, menu);

        ViewUtils.setupActionBarMenuItems(menu, HIDDEN_MENU_ITEMS);

        /* Setup views */
        setupSearchView(menu);
        mSearchView.setOnQueryTextListener(createQueryListener());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onPrepareOptionsMenu (Menu menu) {
        // Auto-focus on search bar on loading the fragment
        mSearchMenuItem.expandActionView();
        // Go back to letter editor on hitting up button
        mSearchMenuItem.setOnActionExpandListener(createSearchCollapseListener());
    }
    /* ************************************************************ */
    /*                            View Helpers                      */
    /* ************************************************************ */

    private void setupActionBarInfo() {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
        }
    }

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

    /* ************************************************************ */
    /*                             Listeners                        */
    /* ************************************************************ */

    private SearchView.OnQueryTextListener createQueryListener() {
        return new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String searchQuery) {
                Log.d(LOG_TAG, searchQuery);
                mInmateSearchViewModel.setQuery(searchQuery);
//                mRecyclerView.scrollToPosition(0);
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
                getFragmentManager().popBackStack();
                return true;
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) { return true; }
        };
    }

    private OnListItemClickListener<Inmate> createInmateOnClickListener() {
        return inmate -> {
            Navigation.findNavController(mParentView).navigate(
                    InmateSearchFragmentDirections.selectInmateAction(
                            (Inmate$$Parcelable) Parcels.wrap(inmate),
                            mLetterId
                    )
            );
        };
    }

    /* ************************************************************ */
    /*                              Helpers                         */
    /* ************************************************************ */

    /* See "http://developer.android.com/training/basics/fragments/communicating.html" */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Letter item);
    }

    private InmateSearchAdapter getRecyclerViewAdapter() {
        return (InmateSearchAdapter) mRecyclerView.getAdapter();
    }

    private void setupStateFromBundleArgs() {
        Bundle argsBundle = getArguments();
        if (argsBundle != null) {
            InmateSearchFragmentArgs args = InmateSearchFragmentArgs.fromBundle(getArguments());
            mLetterId = args.getLetterId();
        }
    }
}
