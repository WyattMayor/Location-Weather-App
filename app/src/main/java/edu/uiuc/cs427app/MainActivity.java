package edu.uiuc.cs427app;

import android.content.ContentValues;
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

import edu.uiuc.cs427app.db.*;
import java.io.Serializable;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private Button addLocationButton;

    private Button removeLocationButton;

    private static final int RESULT_CODE = 1; // Variable returned from child function to notify parent function

    DatabaseHelper dbHelper;
    SQLiteDatabase db;

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
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddLocActivity.class);
                startActivityForResult(intent, RESULT_CODE); //Expect a return request code to notify its done
            }
        });


        removeLocationButton.setOnClickListener(new View.OnClickListener() {
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
//                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                            compoundButton.setText(R.string.light_switch);
                        } else {
                            updateTheme(Reference.CurrentUser,"0");
//                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                            compoundButton.setText(R.string.dark_switch);
                        }
                        setTheme(Reference.CurrentUser);
                    }
                });

    }

    //To maintain the toggle status correctly after making a theme selection
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { // Handle request Code If Returned
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_CODE) {
            if (resultCode == RESULT_OK) {
                refresh(Reference.CurrentUser); // if the code returned ok then refresh the ui with new user list
            }
        }
    }
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
                    String cityName = cursor.getString(cursor.getColumnIndex("city"));
                    cityList.add(cityName);
                } while (cursor.moveToNext()); // continue this process until all cities are added to the list
            }
            cursor.close();
        }
        db.close();

        return cityList; // return list of cities
    }


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

    //Customised-UI based on user preferences
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
    //Customised-UI based on user preferences
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



    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.buttonAddLocation:
                intent = new Intent(this, SignupActivity.class);
                startActivity(intent);
                break;
            default:
                Button clicked = (Button) view;
                String cityName = clicked.getText().toString();

                intent = new Intent(this, DetailsActivity.class);
                intent.putExtra("city", cityName);
                startActivity(intent);
                break;


        }
    }
}