package edu.uiuc.cs427app;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import androidx.appcompat.app.AppCompatActivity;

import edu.uiuc.cs427app.db.DatabaseHelper;
import edu.uiuc.cs427app.util.LogoutComponent;
import edu.uiuc.cs427app.util.Reference;

public class AddLocActivity extends AppCompatActivity{
    private Button AddLocationB;
    private EditText cityText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addloc);
        AddLocationB = findViewById(R.id.AddLocationB);
        cityText = findViewById(R.id.locationEditText);

        AddLocationB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = cityText.getText().toString().toLowerCase(); //extract text from text box
                addCity(Reference.CurrentUser, city); // add city to the database with respect to user
                setResult(RESULT_OK);  // set result to notify the parent function it is done
                finish();
            }
        });
    }

    public void addCity(String username, String city) { // method for added in city with respect to a user
        DatabaseHelper dbhelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbhelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("userName", username);
        values.put("city", city);

        db.insert("Item", null, values);

        db.close();
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
