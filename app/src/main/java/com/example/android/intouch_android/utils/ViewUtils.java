package com.example.android.intouch_android.utils;

import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.android.intouch_android.R;
import com.example.android.intouch_android.ui.sentletters.SentLettersAdapter;
import com.example.android.intouch_android.ui.sentletters.SentLettersFragment;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

public class ViewUtils {
    private static final String LOG_TAG = "ViewUtils";
    private static List<Integer> MENU_ITEMS = Arrays.asList(R.id.send_letter, R.id.menu_search);

    public static boolean containsArgs(Bundle bundle, String ...arguments) {
        for (String arg:arguments) {
            if (!bundle.containsKey(arg)) {
                return false;
            }
        }
        return true;
    }

    public static DividerItemDecoration createListDivider(
            RecyclerView recyclerView,
            LinearLayoutManager layoutManager
    ) {
        return new DividerItemDecoration(
                recyclerView.getContext(),
                layoutManager.getOrientation()
        );
    }

    /*
     * @return true if keyboard was successfully hidden; false otherwise
     */
    public static boolean dismissKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        return imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void setActionBarVisible(Activity activity, boolean isVisible) {
        ActionBar actionBar = ((AppCompatActivity) activity).getSupportActionBar();
        if (isVisible) {
            actionBar.show();
        } else {
            actionBar.hide();
        }
    }

    public static void hideBottomNavigation(Activity activity) {
        activity.findViewById(R.id.bottom_navigation).setVisibility(View.GONE);
    }

    public static void setupBottomNavigation(
            Activity activity,
            View view,
            int selectedItemResId,
            int selectedFragmentResId
    ) {
        BottomNavigationView bottomNav = activity.findViewById(R.id.bottom_navigation);
        bottomNav.setVisibility(View.VISIBLE);
        bottomNav.setSelectedItemId(selectedItemResId);
        bottomNav.setOnNavigationItemSelectedListener(menuItem -> {
            int menuItemId = menuItem.getItemId();
            // if selected destination that the user is already on, do nothing
            if (menuItemId == selectedItemResId) {
                return true;
            }

            // TODO: Figure out back nav
            switch (menuItem.getItemId()) {
                case R.id.navigation_drafts:
                    Navigation.findNavController(view).navigate(
                            R.id.draftsFragment,
                            null,
                            new NavOptions.Builder()
                                    .setPopUpTo(
                                            selectedFragmentResId,
                                            true
                                    ).build()
                    );
                    return true;
                case R.id.navigation_letters:
                    Navigation.findNavController(view).navigate(
                            R.id.sentLettersFragment,
                            null,
                            new NavOptions.Builder()
                                    .setPopUpTo(
                                            selectedFragmentResId,
                                            true
                                    ).build()
                    );
                    return true;
                case R.id.navigation_profile:
                    Navigation.findNavController(view).navigate(
                            R.id.profileFragment,
                            null,
                            new NavOptions.Builder()
                                    .setPopUpTo(
                                            selectedFragmentResId,
                                            true
                                    ).build()
                    );
                    return true;
            }

            // Should never reach here
            return false;
        });
    }

    public static void setupActionBarOptions(
            Activity activity,
            String title,
            boolean enableUpButton
    ) {
        ActionBar actionBar = ((AppCompatActivity) activity).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
            actionBar.setDisplayHomeAsUpEnabled(enableUpButton);
        }
    }

    /*
     * @return true if all items successfully hidden; false otherwise
     */
    public static boolean setupMenuItems(Menu menu, List<Integer> visibleMenuItems) {
        for (Integer itemId:MENU_ITEMS) {
            MenuItem menuItem = menu.findItem(itemId);
            if (menuItem == null) {
                return false;
            }
            else if (visibleMenuItems.contains(itemId)) {
                menuItem.setVisible(true);
            }
            else {
                menuItem.setVisible(false);
            }
        }

        return true;
    }

    public static Pair<MenuItem, SearchView> setupSearch(
            Menu menu,
            int hintResId,
            Activity activity
    ) {
        MenuItem searchMenuItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setQueryHint(activity.getString(hintResId));

        // Get configuration options in res/xml/searchable.xml as an object
        SearchManager searchManager =
                (SearchManager) activity.getSystemService(Context.SEARCH_SERVICE);
        SearchableInfo searchableInfo =
                searchManager.getSearchableInfo(activity.getComponentName());

        searchView.setSearchableInfo(searchableInfo);

        return new Pair<>(searchMenuItem, searchView);
    }
}
