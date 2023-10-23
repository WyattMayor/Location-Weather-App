package edu.uiuc.cs427app.db;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "UserData.db";
    private static final int DATABASE_VERSION = 1;

    // Define the table creation SQL statement
    private static final String CREATE_USER_TABLE = "CREATE TABLE User ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "username TEXT NOT NULL,"
            + "password TEXT NOT NULL);";

    private static final String CREATE_ITEM_TABLE = "CREATE TABLE Item ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "city TEXT NOT NULL,"
            + "userName TEXT," // Add a foreign key reference to the User table
            + "FOREIGN KEY (userName) REFERENCES User(username));"; // Ensure referential integrity

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_ITEM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Implement database upgrade logic here if needed
    }
}