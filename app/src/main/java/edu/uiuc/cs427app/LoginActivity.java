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

import android.content.res.AssetManager;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import org.json.JSONArray;
import org.json.JSONObject;

import edu.uiuc.cs427app.db.*;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView signupLink;
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        signupLink = findViewById(R.id.signupLink);
        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getReadableDatabase();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                // Verify login credential.
                try {
                    login(username, password);
                } catch (Exception e) {
                    Log.e("LoginActivity", "Exception occurred", e);
                }
            }
        });

        signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to signup activity.
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                // Start the SignupActivity
                startActivity(intent);
            }
        });
    }
    private void login(String username, String password) throws Exception {
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
            setContentView(R.layout.activity_login);
        }

        cursor.close();
    }
}