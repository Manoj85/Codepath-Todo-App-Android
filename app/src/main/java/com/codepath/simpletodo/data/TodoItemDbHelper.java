package com.codepath.simpletodo.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Database helper for SimpleTodo app. Manages database creation and version management.
 */
public class TodoItemDbHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = TodoItemDbHelper.class.getSimpleName();

    // Name of the database file
    private static final String DATABASE_NAME = "simpletodo.db";

    // Database version. If you change the database schema, you must increase the database version.
    private static final int DATABASE_VERSION = 1;


    /**
     * Constructs a new instance of {@link TodoItemDbHelper}
     * @param context of the app
     */
    public TodoItemDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // This is called when the database is created for the first time
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a string that contains the SQL statement to create the simpletodo table
        String SQL_CREATE_TODO_TABLE = "CREATE TABLE " + ItemContract.ItemEntry.TABLE_NAME + " ("
                + ItemContract.ItemEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ItemContract.ItemEntry.COLUMN_ITEM_NAME + " TEXT NOT NULL, "
                + ItemContract.ItemEntry.COLUMN_ITEM_NOTES + " TEXT, "
                + ItemContract.ItemEntry.COLUMN_ITEM_PRIORITY + " INTEGER NOT NULL DEFAULT 0, "
                + ItemContract.ItemEntry.COLUMN_ITEM_STATUS + "INTEGER NOT NULL DEFAULT 0);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_TODO_TABLE);
    }

    // This is called when the database needs to be upgraded.
    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to be done here.
    }
}
