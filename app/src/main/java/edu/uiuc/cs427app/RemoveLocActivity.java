package edu.uiuc.cs427app;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import androidx.appcompat.app.AppCompatActivity;

import edu.uiuc.cs427app.db.DatabaseHelper;
import edu.uiuc.cs427app.util.LogoutComponent;
import edu.uiuc.cs427app.util.Reference;

public class RemoveLocActivity extends AppCompatActivity{

    private Button RemoveLocationB;
    private EditText cityText;
    /**
     * Called when the RemoveLocActivity is first created. It initializes the user interface elements,
     * sets up event listeners for the remove action, and handles user interaction.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being
     * shut down, this Bundle contains the data it most recently supplied.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_removeloc);
        RemoveLocationB = findViewById(R.id.RemoveLocationB);
        cityText = findViewById(R.id.locationEditText);

        RemoveLocationB.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when the Remove button is clicked. Removes the city from the database
             * based on the current user. Sets result to notify the parent function it removed a city.
             *
             * @param v The view that was clicked, in this case, the remove link.
             */
            @Override
            public void onClick(View v) {
                String city = cityText.getText().toString().toLowerCase();//extract text from text box
                removeCity(Reference.CurrentUser, city);// add city to the database with respect to user
                setResult(RESULT_OK);// set result to notify the parent function it is done
                finish();
            }
        });

    }

    /**
     * Removes the city for a specific user from the database
     *
     * @param username  Current user that is logged in.
     * @param city The city name that the user inputted.
     */
    public void removeCity(String username, String city) { // method for removing cities from the database with respect to user
        DatabaseHelper dbhelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbhelper.getWritableDatabase();

        String where = "userName = ? AND city = ?";

        String[] whereArgs = {username, city};

        // removes city from specific user's list of cities in database
        db.delete("Item",where, whereArgs);

        db.close();
    }

    /**
     * create the options menu for the current activity.
     *
     * @param menu The options menu.
     * @return True to display the menu, false to not display it.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Add sign out button to menu.
        LogoutComponent.setupOptionsMenu(this, menu);
        return true;
    }

    /**
     * Called when an item in the options menu is selected.
     *
     * @param item The selected menu item.
     * @return True if the event was handled, false otherwise.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Sign out the current user when sign out button is clicked.
        if (LogoutComponent.handleLogoutItemSelected(item, this)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
