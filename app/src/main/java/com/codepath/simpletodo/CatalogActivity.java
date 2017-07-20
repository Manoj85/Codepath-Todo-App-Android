package com.codepath.simpletodo;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.codepath.simpletodo.data.TodoItemContract.ItemEntry;

public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    /** Identifier for the todo-item data loader */
    private static final int TODOITEM_LOADER = 0;

    // Adapter for the ListView
    TodoCursorAdapter mCursorAdapter;

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

        setupTodoItemListView();
    }


    private void setupTodoItemListView() {
        // Find the ListView which will be populated with the todoitem data
        ListView itemListView = (ListView) findViewById(R.id.list);

        // Find and Set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        itemListView.setEmptyView(emptyView);

        // Setup an Adapter to create a list item for each row of the todoitem data in the Cursor.
        // There is no todoitem data yet (until the loader finishes) so pass in null for the Cursor.
        mCursorAdapter = TodoCursorAdapter.getInstance(this, null);
        itemListView.setAdapter(mCursorAdapter);

        // Setup the item click listener
        itemListView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Create new intent to go to {@link EditorActivity}
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);

                // Form the content URI that represents the specific todo-item that was clicked on,
                // by appending the "id" (passed as input to this method) onto the
                // {@link ItemEntry#CONTENT_URI}.
                // For example, the URI would be "content://com.codepath.simpletodo/todo/2"
                // if the item with ID 2 was clicked on.
                Uri currentTodoItemUri = ContentUris.withAppendedId(ItemEntry.CONTENT_URI, id);

                // Set the URI on the data field of the intent
                intent.setData(currentTodoItemUri);

                // Launch the {@link EditorActivity} to display the data for the current todo-item.
                startActivity(intent);
            }
        });

        // Kick off the loader
        getLoaderManager().initLoader(TODOITEM_LOADER, null, this);
    }


    // Helper method to insert hardcoded todoitems data into the database.
    // For debugging purposes only.
    private void insertTodoItems() {

        // Create a ContentValues object where column names are the keys,
        // and Task1's pet attributes are the values.
        ContentValues values = new ContentValues();
        values.put(ItemEntry.COLUMN_ITEM_NAME, "Sample Task");
        values.put(ItemEntry.COLUMN_ITEM_NOTES, "Notes");
        values.put(ItemEntry.COLUMN_ITEM_PRIORITY, ItemEntry.PRIORITY_LOW);
        values.put(ItemEntry.COLUMN_ITEM_STATUS, ItemEntry.STATUS_TODO);

        // Insert a new row for "Sample-todoitem" into the provider using the ContentResolver.
        // Use the {@link TodoItemContract.ItemEntry#CONTENT_URI} to indicate that we want to insert
        // into the pets database table.
        // Receive the new content URI that will allow us to access Sample-todoitem's data in the future.

        Uri newUri = getContentResolver().insert(ItemEntry.CONTENT_URI, values);
    }

    // Helper method to delete all todoitems data from the database.
    private void deleteAllTodoItems() {
        int rowsDeleted = getContentResolver().delete(ItemEntry.CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from todo database");
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
                return true;

            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllTodoItems();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                ItemEntry._ID,
                ItemEntry.COLUMN_ITEM_NAME,
                ItemEntry.COLUMN_ITEM_NOTES };

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                ItemEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update {@link TodoCursorAdapter} with this new cursor containing updated todoitems data
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Callback called when the data needs to be deleted
        mCursorAdapter.swapCursor(null);
    }
}
