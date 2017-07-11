package com.codepath.simpletodo;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.codepath.simpletodo.data.ItemContract;
import com.codepath.simpletodo.data.TodoItemDbHelper;

public class EditorActivity extends AppCompatActivity {

    /** EditText field to enter the item's title */
    private EditText mNameEditText;

    /** EditText field to enter the item's notes */
    private EditText mNotesEditText;

    // EditText field to enter the item's priority
    private Spinner mPrioritySpinner;

    // EditText field to enter the item's status
    private Spinner mStatusSpinner;

    /**
     * Priority of the TodoItem. The possible valid values are in the ItemContract.java file:
     * {@link ItemContract.ItemEntry#PRIORITY_LOW}, {@link ItemContract.ItemEntry#PRIORITY_MEDIUM}, or
     * {@link ItemContract.ItemEntry#PRIORITY_HIGH}.
     */
    private int mPriority = ItemContract.ItemEntry.PRIORITY_LOW;

    /**
     * Status of the TodoItem. The possible valid values are in the ItemContract.java file:
     * {@link ItemContract.ItemEntry#STATUS_TODO}, {@link ItemContract.ItemEntry#STATUS_INPROGRESS}, or
     * {@link ItemContract.ItemEntry#STATUS_DONE}.
     */
    private int mStatus = ItemContract.ItemEntry.STATUS_TODO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Find all relevant views that we will need to read user input from
        mNameEditText = (EditText) findViewById(R.id.edit_task_name);
        mNotesEditText = (EditText) findViewById(R.id.edit_task_description);
        mPrioritySpinner = (Spinner) findViewById(R.id.spinner_priority);
        mStatusSpinner = (Spinner) findViewById(R.id.spinner_status);

        setupSpinner();
    }

    // Setup the dropdown spinner that allows the user to select the priority of the item.
    private void setupSpinner() {
        // Create adapter for spinner.
        // The list options are from the String array it will use the default layout
        ArrayAdapter prioritySpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_priority_options, android.R.layout.simple_spinner_item);

        ArrayAdapter statusSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_status_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        prioritySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        statusSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mPrioritySpinner.setAdapter(prioritySpinnerAdapter);
        mStatusSpinner.setAdapter(statusSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mPrioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.priority_low))) {
                        mPriority = 0;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Set the integer mSelected to the constant values
        mStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.priority_low))) {
                        mPriority = 0;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    // Get user input from editor and save new todoitem into database
    private void insertTodoItem() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String nameString = mNameEditText.getText().toString().trim();
        String notesString = mNotesEditText.getText().toString().trim();

        // Create database helper
        TodoItemDbHelper mDbHelper = new TodoItemDbHelper(this);

        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and todoitem attributes from the editor are the values
        ContentValues values = new ContentValues();
        values.put(ItemContract.ItemEntry.COLUMN_ITEM_NAME, nameString);
        values.put(ItemContract.ItemEntry.COLUMN_ITEM_NOTES, notesString);
        values.put(ItemContract.ItemEntry.COLUMN_ITEM_PRIORITY, mPriority);
        values.put(ItemContract.ItemEntry.COLUMN_ITEM_STATUS, mStatus);

        // Insert a new row for todoitem in the database, returning ID of that new row.
        long newRowId = db.insert(ItemContract.ItemEntry.TABLE_NAME, null, values);

        // Show a toast message depending on whether or not the insertion was successful.
        if (newRowId == -1) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText(this, "Error with saving ToDo Item", Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
            Toast.makeText(this, "ToDo item saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file
        // This adds menu items to the app bar
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save todoitem to database
                insertTodoItem();
                // Exit activity
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing for now
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
