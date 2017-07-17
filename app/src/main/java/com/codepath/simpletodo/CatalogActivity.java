package com.codepath.simpletodo;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.simpletodo.data.TodoItemContract;
import com.codepath.simpletodo.data.TodoItemDbHelper;

public class CatalogActivity extends AppCompatActivity {

    // Database helper that will provide us access to the database
    private TodoItemDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        //  Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        mDbHelper = TodoItemDbHelper.getInstance(this);
        // mDbHelper.getReadableDatabase();
        displayDatabaseInfo();
    }


    private void displayDatabaseInfo() {
        // Create and/or open a database to read from it.
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query
        String[] projection = {
                TodoItemContract.ItemEntry._ID,
                TodoItemContract.ItemEntry.COLUMN_ITEM_NAME,
                TodoItemContract.ItemEntry.COLUMN_ITEM_NOTES,
                TodoItemContract.ItemEntry.COLUMN_ITEM_PRIORITY,
                TodoItemContract.ItemEntry.COLUMN_ITEM_STATUS
        };

        // Perform a query on the todoitems table
        Cursor cursor = db.query(
                TodoItemContract.ItemEntry.TABLE_NAME, // The table to query
                projection,                        // The columns to return
                null,                              // The columns for the WHERE clause
                null,                              // The values for the WHERE clause
                null,                              // Don't group the rows
                null,                              // Don't filter by row groups
                null                               // The sort order
        );

        try {
            // Create a header in the Text View that looks like this:
            //
            // The todoitems table contains <number of rows in Cursor> items.
            // _id - name - notes - priority - status
            //
            // In the while loop below, iterate through the rows of the cursor and display
            // the information from each column in this order.
            TextView displayView = (TextView) findViewById(R.id.text_view_item);
            displayView.setText("The todoitems table contains " + cursor.getCount() + " items. \n\n");
            displayView.append(TodoItemContract.ItemEntry._ID + " - " +
                    TodoItemContract.ItemEntry.COLUMN_ITEM_NAME + " - " +
                    TodoItemContract.ItemEntry.COLUMN_ITEM_NOTES + " - " +
                    TodoItemContract.ItemEntry.COLUMN_ITEM_PRIORITY + " - " +
                    TodoItemContract.ItemEntry.COLUMN_ITEM_STATUS + "\n"
            );

            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(TodoItemContract.ItemEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(TodoItemContract.ItemEntry.COLUMN_ITEM_NAME);
            int notesColumnIndex = cursor.getColumnIndex(TodoItemContract.ItemEntry.COLUMN_ITEM_NOTES);
            int priorityColumnIndex = cursor.getColumnIndex(TodoItemContract.ItemEntry.COLUMN_ITEM_PRIORITY);
            int statusColumnIndex = cursor.getColumnIndex(TodoItemContract.ItemEntry.COLUMN_ITEM_STATUS);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                String currentNotes = cursor.getString(notesColumnIndex);
                int currentPriority = cursor.getInt(priorityColumnIndex);
                int currentStatus = cursor.getInt(statusColumnIndex);

                // Display the values from each column of the current row in the cursor in the TextView
                displayView.append("\n" + currentID + " - " +
                        currentName + " - " +
                        currentNotes + " - " +
                        currentPriority + " - " +
                        currentStatus
                );
            }
        } finally {
            // Always close the cursor when you're done reading from it.
            // This releases all its resources and makes it invalid.
            cursor.close();
        }

    }

    // Helper method to insert hardcoded todoitems data into the database.
    // For debugging purposes only.
    private void insertTodoItems() {
        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and Task1's pet attributes are the values.
        ContentValues values = new ContentValues();
        values.put(TodoItemContract.ItemEntry.COLUMN_ITEM_NAME, "Task");
        values.put(TodoItemContract.ItemEntry.COLUMN_ITEM_NOTES, "Notes");
        values.put(TodoItemContract.ItemEntry.COLUMN_ITEM_PRIORITY, TodoItemContract.ItemEntry.PRIORITY_LOW);
        values.put(TodoItemContract.ItemEntry.COLUMN_ITEM_STATUS, TodoItemContract.ItemEntry.STATUS_TODO);

        long newRowId = db.insert(TodoItemContract.ItemEntry.TABLE_NAME, // The tododitems table name
                null,   // the name of a column in which the framework
                        // can insert NULL in the event that the ContentValues is empty (if
                        // this is set to "null", then the framework will not insert a row when
                        // there are no values).
                values  // the ContentValues object containing the info for Task1
        );
    }

    // Helper method to delete all todoitems data from the database.
    private void deleteAllTodoItems() {
        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        long deletedRowId = db.delete(TodoItemContract.ItemEntry.TABLE_NAME, null, null);

        // Show a toast message depending on whether or not the insertion was successful.
        if (deletedRowId == -1) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText(this, "Error with deleting All ToDo Item(s)", Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
            Toast.makeText(this, "All ToDo item(s) deleted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file
        // This adds menu items to the app bar
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertTodoItems();
                displayDatabaseInfo();
                return true;

            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllTodoItems();
                displayDatabaseInfo();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
