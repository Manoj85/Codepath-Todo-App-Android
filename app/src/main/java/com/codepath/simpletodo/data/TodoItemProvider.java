package com.codepath.simpletodo.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.codepath.simpletodo.data.TodoItemContract.ItemEntry;

/**
 * {@link ContentProvider} for Todo app
 */

public class TodoItemProvider extends ContentProvider {

    // Tag for the log messages
    public static final String LOG_TAG = TodoItemProvider.class.getSimpleName();

    /** URI matcher code for the content URI for the `todo` table */
    private static final int TODOITEMS = 100;

    /** URI matcher code for the content URI for a single todoitem in the `todo` table */
    private static final int TODOITEM_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer: This is run the first time anything is called from this class
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

        // The content URI of the form "content://com.codepath.simpletodo/todo" will map to the
        // integer code {@link #TODO}. This URI is used to provide access to MULTIPLE rows
        // of the todo table.

        sUriMatcher.addURI(TodoItemContract.CONTENT_AUTHORITY, TodoItemContract.PATH_TODO, TODOITEMS);

        // The content URI of the form "content://com.codepath.simpletodo/todo/#" will map to the
        // integer code {@link #TODOITEM_ID}. This URI is used to provide access to ONE single row
        // of the todo table.

        // In this case, the "#" wildcard is used where "#" can be substituted for an integer.
        // For example, "content://com.example.android.pets/pets/3" matches, but
        // "content://com.codepath.simpletodo/todo" (without a number at the end) doesn't match.

        sUriMatcher.addURI(TodoItemContract.CONTENT_AUTHORITY, TodoItemContract.PATH_TODO + "/#", TODOITEM_ID);
    }

    // Database helper that will provide us access to the database
    private TodoItemDbHelper mDbHelper;

    // Initialize the provider and the database helper object
    @Override
    public boolean onCreate() {
        mDbHelper = TodoItemDbHelper.getInstance(getContext());
        return true;
    }

    /**
     * Perform the query for the given URI.
     * Use the given projection, selection, selection arguments, and sort order.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        // Get readable database
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case TODOITEMS:
                // For the TODOITEMS code, query the "todo" table directly with the given
                // projection, selection, selection arguments, and sort order.
                // The cursor could contain multiple rows of the "todo" table
                cursor = db.query(ItemEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case TODOITEM_ID:
                // For the TODOITEM_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.codepath.simpletodo/todo/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = ItemEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                // This will perform a query on the "todo" table where the _id equals 3 to return a
                // cursor containing that row of the table.
                cursor = db.query(ItemEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        return cursor;
    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case TODOITEMS:
                return insertTodoItem(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * Insert a todoitem into the database with the given content values.
     * Return the new content URI for that specific row in the database.
     */
    private Uri insertTodoItem(Uri uri, ContentValues values) {
        return null;
    }

    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        return 0;
    }

    /**
     * Delete the data at the given selection and selection arguments.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    /**
     * Returns the MIME type of data for the content URI
     */
    @Override
    public String getType(Uri uri) {
        return null;
    }
}
