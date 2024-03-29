package edu.uiuc.cs427app.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "UserData.db";
    private static final int DATABASE_VERSION = 2;

    // Define the table creation SQL statement
    private static final String CREATE_USER_TABLE = "CREATE TABLE User ("
            + "username TEXT PRIMARY KEY NOT NULL,"
            + "password TEXT NOT NULL,"
            + "darkMode TEXT NOT NULL);";

    private static final String CREATE_ITEM_TABLE = "CREATE TABLE Item ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "city TEXT NOT NULL," // Add a foreign key reference to the User table
            + "userName TEXT  NOT NULL,"
            + "FOREIGN KEY (userName) REFERENCES User(username));"; // Ensure referential integrity

    private static final String INSERT_USER = "INSERT INTO User (username, password, darkMode) VALUES (?, ?, ?);";

    /**
     * Constructs a new instance of the DatabaseHelper class.
     *
     * @param context The application context in which the database helper will be used.
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time. It executes SQL statements to create
     * needed tables in the database.
     *
     * @param db The SQLiteDatabase instance in which the tables will be created.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_ITEM_TABLE);
    }

    /**
     * Not implemented
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Implement database upgrade logic here if needed
    }

    /**
     * Inserts a new user into the database with the provided username and password.
     *
     * @param db       The SQLiteDatabase instance to which the user will be added.
     * @param username The username of the user to be inserted.
     * @param password The password associated with the user.
     * @param darkMode Either 0 and 1 and sets the theme of app
     */
    public void insertUser(SQLiteDatabase db, String username, String password, String darkMode) {
        SQLiteStatement statement = db.compileStatement(INSERT_USER);
        statement.bindString(1, username);
        statement.bindString(2, password);
        statement.bindString(3, darkMode);
        statement.executeInsert();
    }

    /**
     * Deletes a new user in the database with the provided username and password.
     *
     * @param db       The SQLiteDatabase instance to which the user will be added.
     * @param username The username of the user to be inserted.
     */
    public void deleteUser(SQLiteDatabase db, String username) {
        String whereClause = "username = ?";
        String[] whereArgs = new String[] { username };
        db.delete("User", whereClause, whereArgs);
    }

    public boolean findUser(SQLiteDatabase db, String username, String password) {
        Cursor cursor = db.query(
                "User", // Table name
                null,   // Columns; null means all columns
                "username = ? AND password = ?", // Selection
                new String[] { "abc", "123" }, // Selection args
                null,   // Group by
                null,   // Having
                null    // Order by
        );

        return cursor.moveToFirst();
    }

}