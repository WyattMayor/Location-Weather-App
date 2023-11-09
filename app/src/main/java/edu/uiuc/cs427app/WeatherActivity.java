package edu.uiuc.cs427app;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import static java.lang.System.*;
import static java.security.AccessController.getContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.uiuc.cs427app.util.LogoutComponent;

public class WeatherActivity extends AppCompatActivity implements View.OnClickListener{
    /**
     * This method sets the content view to the activity_weather layout,
     * formats the city name into camel case, and updates the UI with this name.
     * Then renders the current date and time for the selected city
     * from the provided list of cities (Champaign, Chicago, New York, Los Angeles, and San Francisco).
     * Noted that it also sets placeholder weather data to represent where actual weather data would be displayed
     * once the weather API is integrated into the application.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        // Get the city name from the intent and format it into camel case
        String cityName = toCamelCase(getIntent().getStringExtra("city"));

        // Set the city name to the TextView
        TextView cityNameTextView = findViewById(R.id.cityNameText);
        cityNameTextView.setText(cityName);

        // Render date and time based on current selected city
        // (Champaign, Chicago, New York, Los Angeles, and San Fransisco)
        renderCurrentDateTime(cityName);

        // Placeholder for rendering weather.
        RenderWeatherData( cityName, WeatherActivity.this);
    }
    public interface LatLonCallback {
        void onLatLonReceived(double latitude, double longitude);
    }

    public interface WeatherCallback {
        void onWeatherDataReceived(double temperature, double windSpeed, double windDegrees,double humidity, String weatherDescription);
    }
    private void getLatLon(String city, Context context, LatLonCallback callback) {
        String baseUrl = "https://api.openweathermap.org/geo/1.0/direct";
        String apiKey = "e05da8e2e7eec0f149c076e2650144f5";
        String url = baseUrl + "?q=" + city + "," + "+1" +
                "&limit=1" + "&appid=" + apiKey;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        final double[] latitude = {0};
        final double[] longitude = {0};

        JsonArrayRequest JsonArrayRequest = new JsonArrayRequest(Request.Method.GET,url,null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject firstObject = response.getJSONObject(0);
                            latitude[0] = firstObject.getDouble("lat");
                            longitude[0] = firstObject.getDouble("lon");
                            callback.onLatLonReceived(latitude[0], longitude[0]);

                        }
                        catch(JSONException e){
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors here
                        System.err.println("Error: " + error.toString());
                    }
                });
        requestQueue.add(JsonArrayRequest);
    }

    private void getWeatherData(double latitude, double longitude, Context context, WeatherCallback callback) {
        String baseWeatherUrl = "https://api.openweathermap.org/data/2.5/weather?";
        String apiKey = "e05da8e2e7eec0f149c076e2650144f5";
        String urlWeather = baseWeatherUrl + "lat=" + String.valueOf(latitude) + "&lon=" + String.valueOf(longitude) + "&appid=" + apiKey;
        final double[] temp = {0};
        final double[] wind_speed = {0};
        final double[] wind_deg = {0};
        final double[] humidity2 = {0};
        final String[] weather2 = {""};
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        Log.d("check2", urlWeather);
        JsonObjectRequest jsonArrayRequestWeather = new JsonObjectRequest(Request.Method.GET, urlWeather, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONObject main = response.getJSONObject("main");
                            JSONObject wind = response.getJSONObject("wind");
                            JSONArray weatherparse = response.getJSONArray("weather");
                            JSONObject weatherparser2 = weatherparse.getJSONObject(0);
                            temp[0] = main.getDouble("temp");
                            wind_speed[0] = wind.getDouble("speed");
                            wind_deg[0] = wind.getDouble("deg");
                            humidity2[0] = main.getDouble("humidity");
                            weather2[0] = weatherparser2.getString("main");
                            callback.onWeatherDataReceived(temp[0],  wind_speed[0], wind_deg[0],  humidity2[0], weather2[0]);
                        }
                        catch(JSONException e){
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors here
                        System.err.println("Error: " + error.toString());
                    }
                });
        requestQueue.add(jsonArrayRequestWeather);
    }

    /**
     * Updates the weather-related TextViews with the provided data.
     * This method serves as a placeholder for rendering weather data on the UI.
     * It should be connected to a weather API to fetch and display live data.
     *
     */
    private void RenderWeatherData(String City, Context context) {

        double temperature = 0.0;
        String weatherDescription = "";
        double humidity= 0.0;
        double windSpeed= 0.0;
        double windDegrees= 0.0;

        getLatLon(City, this, new LatLonCallback() {
            @Override
            public void onLatLonReceived(double latitude, double longitude) {
                // First request completed, now make the second request
                getWeatherData(latitude, longitude, WeatherActivity.this, new WeatherCallback() {
                    @Override
                    public void onWeatherDataReceived(double temperature, double windSpeed, double windDegrees, double humidity, String weatherDescription) {
                        // Handle weather data here
                        Log.d("Weather", "Temperature: " + temperature + ", Wind Speed: " + windSpeed + ", Weather: " + weatherDescription);
                        TextView temperatureTextView = findViewById(R.id.temperatureText);
                        temperatureTextView.setText(String.valueOf(temperature));

                        TextView weatherConditionTextView = findViewById(R.id.weatherConditionText);
                        weatherConditionTextView.setText(weatherDescription);

                        TextView humidityTextView = findViewById(R.id.humidityText);
                        humidityTextView.setText("Humidity: " + String.valueOf(humidity));

                        TextView windConditionTextView = findViewById(R.id.windConditionText);
                        windConditionTextView.setText("Wind Speed: " + String.valueOf(windSpeed)+  " Wind deg: " + String.valueOf(windDegrees));
                    }
                });
            }
        });

        /*TextView temperatureTextView = findViewById(R.id.temperatureText);
        temperatureTextView.setText(String.valueOf(temperature));

        TextView weatherConditionTextView = findViewById(R.id.weatherConditionText);
        weatherConditionTextView.setText(weatherDescription);

        TextView humidityTextView = findViewById(R.id.humidityText);
        humidityTextView.setText("Humidity: " + String.valueOf(humidity));

        TextView windConditionTextView = findViewById(R.id.windConditionText);
        windConditionTextView.setText("Wind: " + String.valueOf(windSpeed)+  "Wind deg: " + String.valueOf(windDegrees));*/
    }

    /**
     * Updates the UI to display the current date and time according to the city's time zone.
     * 1. determines the time zone based on the city name using the `mapCityToTimeZone` method.
     * 2. obtains the current date and time in that time zone and formats them
     * 3. sets the formatted date and time strings to the respective TextViews on the screen.
     *
     * @param city The name of the city for which to display the current date and time.
     */
    private void renderCurrentDateTime(String city) {


        // Map the city to its time zone
        String timeZoneId = mapCityToTimeZone(city);

        // Get the current date and time in the specified time zone
        Calendar calendar = Calendar.getInstance();
        TimeZone timeZone = TimeZone.getTimeZone(timeZoneId);
        calendar.setTimeZone(timeZone);

        // Create a SimpleDateFormat instance with the desired format
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
        dateFormat.setTimeZone(timeZone);
        timeFormat.setTimeZone(timeZone);

        // Format the current date and time
        String currentDate = dateFormat.format(calendar.getTime());
        String currentTime = timeFormat.format(calendar.getTime());

        // Set the date and time to the TextViews
        TextView dateTextView = findViewById(R.id.dateText);
        dateTextView.setText(currentDate);
        TextView timeTextView = findViewById(R.id.timeText);
        timeTextView.setText(currentTime);
    }

    /**
     * Maps a given city name to its corresponding time zone identifier.
     * The function handles five specific city names and maps each to the appropriate time zone.
     * If a city name is not one of the five specified, the function returns the time zone ID of the device's default time zone.
     *
     * @param city The name of the city to map to a time zone.
     * @return A string representing the time zone ID for the given city or the device's default time zone if the city is not recognized.
     */
    private String mapCityToTimeZone(String city) {
        switch (city) {
            case "Champaign":
            case "Chicago":
                return "America/Chicago";
            case "New York":
                return "America/New_York";
            case "Los Angeles":
            case "San Francisco":
                return "America/Los_Angeles";
            default:
                // Return the device's default time zone if the city is not one of the five specified
                return TimeZone.getDefault().getID();
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
     * Not implemented yet
     */
    @Override
    public void onClick(View view) {
        //Implement this (create an Intent that goes to a new Activity, which shows the map)
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

