package com.codepath.simpletodo;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.codepath.simpletodo.data.TodoItemContract.ItemEntry;

/**
 * {@link TodoCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of pet data as its data source. This adapter knows
 * how to create list items for each row of todoitem data in the {@link Cursor}.
 */
public class TodoCursorAdapter extends CursorAdapter {

    private static TodoCursorAdapter mInstance = null;

    /**
     * Constructs a new {@link TodoCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public TodoCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    public static TodoCursorAdapter getInstance(Context ctx, Cursor cursor) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (mInstance == null) {
            mInstance = new TodoCursorAdapter(ctx.getApplicationContext(), cursor);
        }
        return mInstance;
    }
    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data.
     *                The cursor is already moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate the list item view using the layout specified in @link list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the todoitem data (in the current row pointed to by cursor) to the given
     * list item layout.
     * For example, the name for the current pet can be set on the name TextView in the list item layout
     *
     * @param view      Existing view, returned earlier by newView() method
     * @param context   app context
     * @param cursor    The cursor from which to get the data.
     *                  The cursor is already moved to the correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView notesTextView = (TextView) view.findViewById(R.id.notes);

        // Find the columns of todoitem attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_NAME);
        int notesColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_NOTES);
        // int priorityColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_PRIORITY);
        // int statusColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_STATUS);

        // Read the todoitem attributes from the Cursor for the current todoitem
        String itemName = cursor.getString(nameColumnIndex);
        String itemNotes = cursor.getString(notesColumnIndex);
        // String itemPriority = cursor.getString(priorityColumnIndex);
        // String itemStatus = cursor.getString(statusColumnIndex);

        // Update the TextViews with the attributes for the current todoitem
        nameTextView.setText(itemName);
        notesTextView.setText(itemNotes);
    }


}
