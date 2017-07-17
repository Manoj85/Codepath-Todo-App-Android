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

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ", ";

    private static TodoItemDbHelper mInstance = null;


    // Create a string that contains the SQL statement to create the "todoitems" table
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TodoItemContract.ItemEntry.TABLE_NAME + " ("
                    + TodoItemContract.ItemEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + COMMA_SEP
                    + TodoItemContract.ItemEntry.COLUMN_ITEM_NAME + TEXT_TYPE + " NOT NULL" + COMMA_SEP
                    + TodoItemContract.ItemEntry.COLUMN_ITEM_NOTES + TEXT_TYPE + COMMA_SEP
                    + TodoItemContract.ItemEntry.COLUMN_ITEM_PRIORITY + " INTEGER NOT NULL DEFAULT 0" + COMMA_SEP
                    + TodoItemContract.ItemEntry.COLUMN_ITEM_STATUS + " INTEGER NOT NULL DEFAULT 0);";

    // Create a string that contains the SQL statement to Drop the `todoitems` table
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TodoItemContract.ItemEntry.TABLE_NAME;

    public static TodoItemDbHelper getInstance(Context ctx) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (mInstance == null) {
            mInstance = new TodoItemDbHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }

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
        // Execute the SQL statement
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    // This is called when the database needs to be upgraded.
    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to be done here.
    }
}
