package edu.uiuc.cs427app;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import edu.uiuc.cs427app.db.*;
import edu.uiuc.cs427app.util.Reference;

import com.google.android.material.switchmaterial.SwitchMaterial;


public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView signupLink;
    DatabaseHelper dbHelper;
    SQLiteDatabase db;

    /**
     * Called when the LoginActivity is first created. It initializes the user interface elements,
     * sets up event listeners for the login and signup actions, and handles user interaction.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being
     * shut down, this Bundle contains the data it most recently supplied.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        signupLink = findViewById(R.id.signupLink);
        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getReadableDatabase();

        loginButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when the login button is clicked. It retrieves the username and password from
             * the input fields, then verifies the login credentials.
             *
             * @param v The view that was clicked, in this case, the login button.
             */
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                // Verify login credential.
                login(username, password);
            }
        });

        signupLink.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when the signup link is clicked. It creates an intent to redirect the user to
             * the signup activity and initiates the activity transition.
             *
             * @param v The view that was clicked, in this case, the signup link.
             */
            @Override
            public void onClick(View v) {
                // Redirect to signup activity.
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                // Start the SignupActivity
                startActivity(intent);
            }
        });
    }



    /**
     * Logs in a user by checking their credentials in the database and navigating to the main activity if valid.
     * This method queries the "User" table in the database to verify the provided username and password.
     * If the user exists, it starts the MainActivity; otherwise, it displays an error message.
     *
     * @param username The username of the user attempting to log in.
     * @param password The password of the user attempting to log in.
     */
    private void login(String username, String password)  {
        Cursor cursor = db.query(
                "User", // Table name
                null,   // Columns; null means all columns
                "username = ? AND password = ?", // Selection
                new String[] { username, password }, // Selection args
                null,   // Group by
                null,   // Having
                null    // Order by
        );

        if (cursor.moveToFirst()) {
            // User exists, retrieve data
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            Reference.CurrentUser = username;
            // Start the SignupActivity
            startActivity(intent);
        } else { // Error with login
            TextView errorTextView = findViewById(R.id.errorTextView);
            errorTextView.setVisibility(View.VISIBLE);
        }

        cursor.close();
    }

    public void setDatabaseHelper(SQLiteDatabase databaseHelper) {
        db = databaseHelper;
    }
}
