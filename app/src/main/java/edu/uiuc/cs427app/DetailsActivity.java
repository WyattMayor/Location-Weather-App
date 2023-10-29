package edu.uiuc.cs427app;

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
     * Called when the DetailsActivity is first created. It initializes the user interface elements,
     * sets up event listeners for the map action, and handles user interaction.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being
     * shut down, this Bundle contains the data it most recently supplied.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Process the Intent payload that has opened this Activity and show the information accordingly
        String cityName = getIntent().getStringExtra("city").toString();
        String welcome = "Welcome to the "+cityName;
        String cityWeatherInfo = "Detailed information about the weather of "+cityName;

        // Initializing the GUI elements
        TextView welcomeMessage = findViewById(R.id.welcomeText);
        TextView cityInfoMessage = findViewById(R.id.cityInfo);

        welcomeMessage.setText(welcome);
        cityInfoMessage.setText(cityWeatherInfo);
        // Get the weather information from a Service that connects to a weather server and show the results

        Button buttonMap = findViewById(R.id.mapButton);
        buttonMap.setOnClickListener(this);

    }
    /**
     * Not implemented yet
     */
    @Override
    public void onClick(View view) {
        //Implement this (create an Intent that goes to a new Activity, which shows the map)
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        LogoutComponent.setupOptionsMenu(this, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (LogoutComponent.handleLogoutItemSelected(item, this)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

