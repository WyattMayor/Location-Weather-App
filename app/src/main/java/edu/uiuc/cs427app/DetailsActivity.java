package edu.uiuc.cs427app;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.uiuc.cs427app.util.LogoutComponent;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener{
    /**
     * Initializes the activity. It sets up the UI for the details page, formats the city name received from the intent,
     * and prepares buttons for additional weather and map details.
     * When the weather or map button is clicked, it initiate actions to display the respective information.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Retrieve the city name passed from the Intent and format the city name to camel case.
        String cityName = toCamelCase(getIntent().getStringExtra("city"));

        // Find the TextView in the layout by its ID and set the text to the formatted city name.
        TextView cityNameView = findViewById(R.id.cityNameText);
        cityNameView.setText(cityName);

        // Get the weather information from a Service that connects to a weather server and show the results
        Button buttonWeather = findViewById(R.id.weatherButton);
        buttonWeather.setOnClickListener(this);

        // Get the map information from a Service that connects to a map server and show the results
        Button buttonMap = findViewById(R.id.mapButton);
        buttonMap.setOnClickListener(this);
    }

    /**
     * Handles click events for buttons within the activity. Determines which button is clicked,
     * either to show weather details or map details, and initiates the corresponding activity.
     */
    @Override
    public void onClick(View view) {
        // Retrieve city name from the Intent and initialize a new Intent
        String cityName = getIntent().getStringExtra("city").toString();
        Intent intent;
        // Check which button was clicked and respond accordingly
        // If the weather button was clicked, start the WeatherActivity
        if (view.getId() == R.id.weatherButton) {
            intent = new Intent(this, WeatherActivity.class);
            intent.putExtra("city", cityName);
            startActivity(intent);
        }
        // If the map button was clicked, start the MapActivity
        else if(view.getId() == R.id.mapButton) {
            // Add intent MapActivity.
        }
    }

    /**
     * Converts a given string to Camel Case, where the first letter of each word is capitalized and the rest are lowercase.
     *
     * @param textInput The string input to be converted to Camel Case.
     * @return A Camel Case version of the input string or null if the input is null.
     */
    private String toCamelCase(final String textInput) {
        // If the input text is null, nothing to convert
        if (textInput == null)
            return null;
        // Initialize a StringBuilder with the expected length for efficiency
        final StringBuilder textOutput = new StringBuilder(textInput.length());
        // Split the text into words, and process each word
        for (final String word : textInput.split(" ")) {
            // If the word is not empty, convert it to camel case
            if (!word.isEmpty()) {
                textOutput.append(Character.toUpperCase(word.charAt(0)));
                textOutput.append(word.substring(1).toLowerCase());
            }
            // If the current length of output is less than the input, add space before the next word
            if (!(textOutput.length() == textInput.length()))
                textOutput.append(" ");
        }

        return textOutput.toString();
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

