package edu.uiuc.cs427app.util;

import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import edu.uiuc.cs427app.LoginActivity;
import edu.uiuc.cs427app.R;

public class LogoutComponent {

    /**
     * Setup the option menus by adding a sign out button.
     *
     * @param context   The activity where the user is currently at.
     * @param menu      The menu of the activity.
     */
    public static void setupOptionsMenu(Context context, Menu menu) {
        MenuInflater inflater = new MenuInflater(context);
        inflater.inflate(R.menu.button_signout, menu);
    }

    /**
     * Sign out the user when the sign out button is clicked.
     *
     * @param item      The item that's currently selected in the menu.
     * @param context   The activity where the user is currently at.
     * @return          Whether the user has successfully sign out.
     */
    public static boolean handleLogoutItemSelected(MenuItem item, Context context) {
        int id = item.getItemId();

        // Sign out the user when the selected item is the sign out button.
        if (id == R.id.action_sign_out) {
            logout(context);
            return true;
        }

        // Do nothing otherwise.
        return false;
    }

    /**
     * Sign out the current user from the current page and redirect user to the login activity.
     *
     * @param context   The current activity where the user is signing out.
     */
    private static void logout(Context context) {
        Reference.CurrentUser = "";
        Intent logoutIntent = new Intent(context, LoginActivity.class);
        logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(logoutIntent);
    }

}
