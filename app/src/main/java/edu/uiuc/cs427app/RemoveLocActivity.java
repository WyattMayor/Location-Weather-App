package edu.uiuc.cs427app;
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

public class RemoveLocActivity extends AppCompatActivity{

    private Button RemoveLocationB;
    private EditText cityText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_removeloc);
        RemoveLocationB = findViewById(R.id.RemoveLocationB);
        cityText = findViewById(R.id.locationEditText);

        RemoveLocationB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = cityText.getText().toString().toLowerCase();//extract text from text box
                removeCity(Reference.CurrentUser, city);// add city to the database with respect to user
                setResult(RESULT_OK);// set result to notify the parent function it is done
                finish();
            }
        });

    }
    public void removeCity(String username, String city) { // method for removing cities from the database with respect to user
        DatabaseHelper dbhelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbhelper.getWritableDatabase();

        String where = "userName = ? AND city = ?";

        String[] whereArgs = {username, city};

        db.delete("Item",where, whereArgs);

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
