package com.example.android.intouch_android.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.android.intouch_android.R;

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
    public static boolean setupActionBarMenuItems(Menu menu, List<Integer> hiddenMenuItems) {
        Log.d(LOG_TAG, "setupActionBarMenuItems called");
        for (Integer itemId:MENU_ITEMS) {
            MenuItem menuItem = menu.findItem(itemId);
            Log.d(LOG_TAG, menuItem.getTitle().toString());
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
}
