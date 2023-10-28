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
        db.execSQL(CREATE_ITEMLIST_TABLE);
        db.execSQL(CREATE_ITEM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Implement database upgrade logic here if needed
    }

    /**
     * Inserts a new user into the database with the provided username and password.
     *
     * @param db The SQLiteDatabase instance to which the user will be added.
     * @param username The username of the user to be inserted.
     * @param password The password associated with the user.
     */
    public void insertUser(SQLiteDatabase db, String username, String password) {
        SQLiteStatement statement = db.compileStatement(INSERT_USER);
        statement.bindString(1, username);
        statement.bindString(2, password);
        statement.executeInsert();
    }
}