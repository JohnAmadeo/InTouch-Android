package com.example.android.intouch_android.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

public class ViewUtils {
    private static final String LOG_TAG = "ViewUtils";
    private static List<Integer> MENU_ITEMS = Arrays.asList(R.id.send_letter, R.id.menu_search);

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

    /*
     * @return true if all items successfully hidden; false otherwise
     */
    public static boolean setupMenuItems(Menu menu, List<Integer> hiddenMenuItems) {
        for (Integer itemId:MENU_ITEMS) {
            MenuItem menuItem = menu.findItem(itemId);
            if (menuItem == null) {
                return false;
            }
            else if (hiddenMenuItems.contains(itemId)) {
                menuItem.setVisible(false);
            }
            else {
                menuItem.setVisible(true);
            }
        }

        return true;
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

    public static DividerItemDecoration createListDivider(
            RecyclerView recyclerView,
            LinearLayoutManager layoutManager
    ) {
        return new DividerItemDecoration(
                recyclerView.getContext(),
                layoutManager.getOrientation()
        );
    }

    public static void setBottomNavigationVisible(Activity activity, boolean isVisible) {
        activity.findViewById(R.id.bottom_navigation)
                .setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    public static void setActionBarVisible(Activity activity, boolean isVisible) {
        ActionBar actionBar = ((AppCompatActivity) activity).getSupportActionBar();
        if (isVisible) {
            actionBar.show();
        } else {
            actionBar.hide();
        }
    }

    public static boolean containsArgs(Bundle bundle, String ...arguments) {
        for (String arg:arguments) {
            if (!bundle.containsKey(arg)) {
                return false;
            }
        }
        return true;
    }
}
