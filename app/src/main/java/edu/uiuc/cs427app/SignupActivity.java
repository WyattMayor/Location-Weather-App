package edu.uiuc.cs427app;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import edu.uiuc.cs427app.db.*;

public class SignupActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button signupButton;
    private TextView loginLink;
    DatabaseHelper dbHelper;
    SQLiteDatabase db;

    /**
     * Called when the SignupActivity is first created. It initializes the user interface elements,
     * sets up event listeners for the signup and login actions, and handles user interaction.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being
     * shut down, this Bundle contains the data it most recently supplied.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signupButton = findViewById(R.id.signupButton);
        loginLink = findViewById(R.id.loginLink);
        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        signupButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when the signup button is clicked. It retrieves the username and password from
             * the input fields and initiates the user signup process.
             *
             * @param v The view that was clicked, in this case, the signup button.
             */
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                // Register User
                register(username, password);
            }
        });
        loginLink.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when the login link is clicked. It creates an intent to redirect the user to
             * the login activity and initiates the activity transition.
             *
             * @param v The view that was clicked, in this case, the login link.
             */
            @Override
            public void onClick(View v) {
                // Redirect to login activity.
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                // Start the SignupActivity
                startActivity(intent);
            }
        });
    }

    /**
     * Registers a user by checking if the provided username is unique and inserting the user
     * into the database if it's not already registered. If registration is successful, it navigates
     * to the main activity; otherwise, it displays an error message.
     *
     * @param username The username of the user to be registered.
     * @param password The password associated with the user.
     */
    private void register(String username, String password) {
        Cursor cursor = db.query(
                "User", // Table name
                null,   // Columns; null means all columns
                "username = ?", // Selection
                new String[] {username}, // Selection args
                null,   // Group by
                null,   // Having
                null    // Order by
        );

        // Create an account in the database with user's username
        // and password and send intent to LoginActivity
        if (!cursor.moveToFirst()) {
            dbHelper.insertUser(db, username, password,"0");
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
        } else {
            // Display sign up error
            TextView errorTextView = findViewById(R.id.errorTextView);
            errorTextView.setVisibility(View.VISIBLE);
        }

        cursor.close();
    }
}
