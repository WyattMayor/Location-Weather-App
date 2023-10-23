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

import edu.uiuc.cs427app.db.*;

public class SignupActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button signupButton;
    private TextView loginLink;
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signupButton = findViewById(R.id.signupButton);
        loginLink = findViewById(R.id.loginLink);
        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                // Register User
                register(username, password);
            }
        });
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to login activity.
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                // Start the SignupActivity
                startActivity(intent);
            }
        });
    }
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

        if (!cursor.moveToFirst()) {
            dbHelper.insertSampleUser(db, username, password);
            Intent intent = new Intent(SignupActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            TextView errorTextView = findViewById(R.id.errorTextView);
            errorTextView.setVisibility(View.VISIBLE);
        }

        cursor.close();
    }
}
