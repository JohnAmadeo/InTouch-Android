package com.example.android.intouch_android;

import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class ViewUtils {

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
    public static boolean setupActionBarMenuItems(Menu menu, int[] hiddenMenuItems) {
        for (int itemId:hiddenMenuItems) {
            MenuItem menuItem = menu.findItem(itemId);
            if (menuItem != null) {
                menuItem.setVisible(false);
            }
            else {
                return false;
            }
        }
        return true;
    }
}
