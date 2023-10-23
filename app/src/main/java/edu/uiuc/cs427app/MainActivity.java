package edu.uiuc.cs427app;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import androidx.navigation.ui.AppBarConfiguration;
import edu.uiuc.cs427app.databinding.ActivityMainBinding;
import android.widget.Button;
import android.widget.LinearLayout;
import java.util.*;
import android.database.sqlite.SQLiteDatabase;
import edu.uiuc.cs427app.db.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout buttonContainer = findViewById(R.id.buttonContainer);
        // Initializing the UI components
        // The list of locations should be customized per user (change the implementation so that
        // buttons are added to layout programmatically


        /*Query statements that query based on username
        String query = "SELECT Item.city FROM Item " +
                "INNER JOIN User ON Item.userName = User.username " +
                "WHERE User.username = ?";*/

        //for testing - Wyatt Mayor
        String[] userLocations = {"Chicago", "Los Angeles", "Champaign", "New York", "New Jersey"};
        List<String> userLocation = Arrays.asList(userLocations);
        //List<String> userLocations = getUserLocations();
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

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.buttonAddLocation:
                // Implement this action to add a new location to the list of locations
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

