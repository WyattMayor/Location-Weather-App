package edu.uiuc.cs427app.db;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "UserData.db";
    private static final int DATABASE_VERSION = 2;

    // Define the table creation SQL statement
    private static final String CREATE_USER_TABLE = "CREATE TABLE User ("
            + "username TEXT PRIMARY KEY NOT NULL,"
            + "password TEXT NOT NULL);";

    private static final String CREATE_ITEMLIST_TABLE = "CREATE TABLE itemList ("
            + "listID INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "cityList TEXT NOT NULL,"
            + "User TEXT," // Add a foreign key reference to the User table
            + "FOREIGN KEY (User) REFERENCES User(username));"; // Ensure referential integrity

    private static final String CREATE_ITEM_TABLE = "CREATE TABLE Item ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "city TEXT NOT NULL," // Add a foreign key reference to the User table
            + "listId INTEGER NOT NULL,"
            + "FOREIGN KEY (listId) REFERENCES Item(listID));"; // Ensure referential integrity

    private static final String INSERT_USER = "INSERT INTO User (username, password) VALUES (?, ?);";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_ITEMLIST_TABLE);
        db.execSQL(CREATE_ITEM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Implement database upgrade logic here if needed
    }

    public void insertSampleUser(SQLiteDatabase db, String username, String password) {
        SQLiteStatement statement = db.compileStatement(INSERT_USER);
        statement.bindString(1, username);
        statement.bindString(2, password);
        statement.executeInsert();
    }
}