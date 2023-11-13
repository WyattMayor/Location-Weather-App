package edu.uiuc.cs427app;

import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.uiuc.cs427app.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private String cityName;
    /**
     * Called when the MapsActivity is first created. It initializes the user interface elements,
     * sets up event listeners for the remove action, and handles user interaction.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being
     * shut down, this Bundle contains the data it most recently supplied.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cityName = toCamelCase(getIntent().getStringExtra("city"));

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLon(cityName, mMap, MapsActivity.this);
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
     * Calls the openweathergeo API and returns the latitude and longitude of the city
     *
     * @param mMap Map interface
     * @param city Name of the city
     * @param context ""
     */
    private void LatLon(String city, GoogleMap mMap, Context context) {
        String baseUrl = "https://api.openweathermap.org/geo/1.0/direct";
        String apiKey = "e05da8e2e7eec0f149c076e2650144f5";
        String url = baseUrl + "?q=" + city + "," + "+1" +
                "&limit=1" + "&appid=" + apiKey;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        final double[] latitude = {0};
        final double[] longitude = {0};

        JsonArrayRequest JsonArrayRequest = new JsonArrayRequest(Request.Method.GET,url,null,
                new Response.Listener<JSONArray>() {
                    /**
                     * Handles the response of the api call
                     *
                     * @param response Api data
                     */
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject firstObject = response.getJSONObject(0);
                            latitude[0] = firstObject.getDouble("lat");
                            longitude[0] = firstObject.getDouble("lon");
                            LatLng citylatlon = new LatLng(latitude[0], longitude[0]);
                            mMap.addMarker(new MarkerOptions().position(citylatlon).title(city +" - Lat:"+ String.valueOf(latitude[0]) + " - Lon:" + String.valueOf(longitude[0])));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(citylatlon));
                            mMap.animateCamera( CameraUpdateFactory.zoomTo( 9.0f ) );
                        }
                        catch(JSONException e){
                        }
                    }
                },
                new Response.ErrorListener() {
                    /**
                     * Handles an error in the response of the api call
                     *
                     * @param error error that was returned
                     */
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors here
                        System.err.println("Error: " + error.toString());
                    }
                });
        requestQueue.add(JsonArrayRequest);
    }
}