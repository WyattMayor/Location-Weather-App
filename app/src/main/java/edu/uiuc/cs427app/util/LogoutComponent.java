package edu.uiuc.cs427app.util;

import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import edu.uiuc.cs427app.LoginActivity;
import edu.uiuc.cs427app.R;

public class LogoutComponent {

    public static void setupOptionsMenu(Context context, Menu menu) {
        MenuInflater inflater = new MenuInflater(context);
        inflater.inflate(R.menu.button_signout, menu);
    }

    public static boolean handleLogoutItemSelected(MenuItem item, Context context) {
        int id = item.getItemId();

        if (id == R.id.action_sign_out) {
            logout(context);
            return true;
        }
        return false;
    }

    private static void logout(Context context) {
        Reference.CurrentUser = "";
        Intent logoutIntent = new Intent(context, LoginActivity.class);
        logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(logoutIntent);
    }

}
