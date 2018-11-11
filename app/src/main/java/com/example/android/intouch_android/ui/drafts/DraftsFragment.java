package com.example.android.intouch_android.ui.drafts;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.intouch_android.R;
import com.example.android.intouch_android.utils.ViewUtils;

import java.util.Arrays;
import java.util.List;

import androidx.navigation.Navigation;

public class DraftsFragment extends Fragment {
    private List<Integer> VISIBLE_MENU_ITEMS = Arrays.asList(R.id.menu_search);
    private View mParentView;

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

        ViewUtils.setupBottomNavigation(
                getActivity(),
                mParentView,
                R.id.navigation_drafts
        );

        return mParentView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.options_menu, menu);

        ViewUtils.setupMenuItems(menu, VISIBLE_MENU_ITEMS);
    }

    @Override
    public void onDetach() { super.onDetach(); }
}
