package edu.uiuc.cs427app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.ui.AppBarConfiguration;
import edu.uiuc.cs427app.databinding.ActivityMainBinding;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import java.util.*;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import android.content.ContentValues;

import edu.uiuc.cs427app.db.*;
import edu.uiuc.cs427app.util.LogoutComponent;
import edu.uiuc.cs427app.util.Reference;

import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private Button addLocationButton;

    private Button removeLocationButton;

    private static final int RESULT_CODE = 1; // Variable returned from child function to notify parent function

    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    /**
     * Called when the MainActivity is first created. It initializes the user interface elements,
     * sets up event listeners for the remove and add action, and handles user interaction.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being
     * shut down, this Bundle contains the data it most recently supplied.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout buttonContainer = findViewById(R.id.buttonContainer);
        addLocationButton = findViewById(R.id.buttonAddLocation);
        removeLocationButton = findViewById(R.id.buttonRemoveLocation);
        // Initializing the UI components
        // The list of locations should be customized per user (change the implementation so that
        // buttons are added to layout programmatically

        //call refresh which is initial list
        refresh(Reference.CurrentUser);
        setTheme(Reference.CurrentUser);
        addLocationButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when the Add button is clicked and starts the AddLocActivity.
             * Starts the activity expecting a result code.
             *
             * @param v The view that was clicked, in this case, the add link.
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddLocActivity.class);
                startActivityForResult(intent, RESULT_CODE); //Expect a return request code to notify its done
            }
        });


        removeLocationButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when the remove button is clicked and starts the RemoveLocActivity.
             * Starts the activity expecting a result code.
             *
             * @param v The view that was clicked, in this case, the add link.
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RemoveLocActivity.class);
                startActivityForResult(intent, RESULT_CODE); //Expect a return request code to notify its done
            }
        });

        //Use the toggle button to switch themes
        SwitchMaterial themeSwitch = findViewById(R.id.themeToggle);

        //listener for detecting change in toggle status
        themeSwitch.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    /**
                     * Invoked when button status changed
                     * @param status - on or off
                     */
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean status) {
                        // theme is switched based on button status
                        if (status) {
                            updateTheme(Reference.CurrentUser,"1");
                            //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                            //compoundButton.setText(R.string.light_switch);
                        } else {
                            updateTheme(Reference.CurrentUser,"0");
                            //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                            //compoundButton.setText(R.string.dark_switch);
                        }
                        setTheme(Reference.CurrentUser);
                    }
                });

    }
    /**
     * maintains the toggle status correctly after making a theme selection
     */
    @Override
    protected void onResume() {
        setTheme(Reference.CurrentUser);
//        SwitchMaterial themeSwitch = findViewById(R.id.themeToggle);
//        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
//            themeSwitch.setChecked(true);
//        } else {
//            themeSwitch.setChecked(false);
//        }
        super.onResume();
    }

    /**
     * Waits for a response from a child intent. Utilized in the add and remove child intent to update the UI
     * after a list change for a specific user.
     *
     * @param requestCode   variable that is returned from a child function.
     * @param resultCode   variable that is returned at the end of the child function marking it finished.
     * @param data   The data that is returned from the child function.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { // Handle request Code If Returned
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_CODE) {
            if (resultCode == RESULT_OK) {
                refresh(Reference.CurrentUser); // if the code returned ok then refresh the ui with new user list
            }
        }
    }

    /**
     * Queries the entries of the cities that are assigned to a specific username.
     * Returns the list of cities that correspond to a specific user.
     *
     * @param username  The username of the user who is currently logged in.
     * @return cityList   Returns the list of cities that the users has associated with his username
     */

    public List<String> GetCityList(String username) {
        DatabaseHelper dbhelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        List<String> cityList = new ArrayList<>();

        // Search through table for cities from a specific user
        String query = "SELECT city FROM Item WHERE userName = ?;";

        // Run the query
        Cursor cursor = db.rawQuery(query, new String[]{username});

        // Check for Null values
        if (cursor != null && cursor.moveToFirst()) {
            if (cursor.moveToFirst()) {
                do {
                    //  Get the city name and add it to the list
                    @SuppressLint("Range") String cityName = cursor.getString(cursor.getColumnIndex("city"));
                    cityList.add(cityName);
                } while (cursor.moveToNext()); // continue this process until all cities are added to the list
            }
            cursor.close();
        }
        db.close();

        return cityList; // return list of cities
    }

    /**
     * Resets the UI by reading in the list of cities with respect to the user.
     * The buttons are remade using this list. Invoked at the start of the main activity
     * and after a city is added/removed.
     *
     * @param username  The username of the user who is currently logged in.
     */
    public void refresh(String username) { //Refresh/Create buttons based on the specified user's list
        LinearLayout buttonContainer = findViewById(R.id.buttonContainer);

        // Clear the existing buttons
        buttonContainer.removeAllViews();

        List<String> userLocation = GetCityList(Reference.CurrentUser);
        for (String city : userLocation){
            //Create the button
            Button button= new Button(this);
            button.setText(city);
            button.setId(View.generateViewId());
            button.setOnClickListener(this);

            //Set background and text color of the button
            button.setBackgroundColor(getResources().getColor(R.color.purple_500));
            button.setTextColor(getResources().getColor(R.color.white));

            //Add margins between individual buttons in dynamic list
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            int marginInDp = 8;

            layoutParams.setMargins(0, 0, 0, marginInDp);
            button.setLayoutParams(layoutParams);

            //create shape to round edges of the button
            GradientDrawable roundedCorners = new GradientDrawable();
            roundedCorners.setShape(GradientDrawable.RECTANGLE);
            roundedCorners.setColor(getResources().getColor(R.color.purple_500));
            roundedCorners.setCornerRadius(20);
            button.setBackgroundDrawable(roundedCorners);

            //Add the button to the button container
            buttonContainer.addView(button);

        }
    }

    /**
     * Updates the user's theme preference (light or dark mode) in the database based on their username.
     *
     * @param username  The username of the user for whom the theme preference is to be updated.
     * @param darkMode  The new theme preference (0 for light mode, 1 for dark mode).
     */

    public void updateTheme(String username, String darkMode) {
        DatabaseHelper dbhelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbhelper.getWritableDatabase();

        String selector = "userName = ?";

        String[] selectorArgs = {username};

        ContentValues cv = new ContentValues();
        cv.put("darkMode",darkMode);
        db.update("User",cv,selector, selectorArgs);

        db.close();
    }

    /**
     * Sets the user's theme based on their stored preference in the database. This method retrieves the
     * user's theme preference and updates the UI accordingly.
     *
     * @param username  The username of the user for whom the theme is to be set.
     */
    public void setTheme(String username) {
        DatabaseHelper dbhelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbhelper.getWritableDatabase();


        Cursor cursor = db.query(
                "User", // Table name
                null,   // Columns; null means all columns
                "username = ?", // Selection
                new String[] {username}, // Selection args
                null,   // Group by
                null,   // Having
                null    // Order by
        );
        cursor.moveToFirst();
        // stores user info in database of  if they have dark mode on or not
        String darkModeEnabled  = cursor.getString(2);
        SwitchMaterial themeSwitch = findViewById(R.id.themeToggle);
        //Light Mode
        if(darkModeEnabled.equals("0")){
            themeSwitch.setChecked(false);
            themeSwitch.setText(R.string.light_switch);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else { //DarkMode
            themeSwitch.setChecked(true);
            themeSwitch.setText(R.string.dark_switch);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        cursor.close();
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

    @Override
    /**
     * Called when one of the list items is clicked and starts the DetailsActivity.
     *
     * @param v The view that was clicked, in this case, the add link.
     */
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            //case R.id.buttonAddLocation:
                //intent = new Intent(this, SignupActivity.class);
                //startActivity(intent);
                //break;
            default:
                Button clicked = (Button) view;
                String cityName = clicked.getText().toString();

                // Sends user to DetailsActivity class with information on which location they want
                // weather details of
                intent = new Intent(this, DetailsActivity.class);
                intent.putExtra("city", cityName);
                startActivity(intent);
                break;


        }
    }
}