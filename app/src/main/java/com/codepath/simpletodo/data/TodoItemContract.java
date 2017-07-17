package com.codepath.simpletodo.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * API Contract for the SimpleTodo app
 */
public final class TodoItemContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor
    private TodoItemContract() {}

    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.codepath.simpletodo";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Possible path (appended to base content URI for possible URI's)
     * For instance, content://com.example.android.pets/pets/ is a valid path for
     * looking at pet data. content://com.example.android.pets/staff/ will fail,
     * as the ContentProvider hasn't been given any information on what to do with "staff".
     */
    public static final String PATH_TODO = "todo";

    // Inner class that defines constant values for the To-do database table
    public static final class ItemEntry implements BaseColumns {

        /** The content URI to access the pet data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_TODO);

        public final static String TABLE_NAME = "todo";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_ITEM_NAME ="name";
        public final static String COLUMN_ITEM_NOTES ="notes";
        public final static String COLUMN_ITEM_PRIORITY ="priority";
        public final static String COLUMN_ITEM_STATUS ="status";

        /**
         * Possible values for the priority of the item.
         */
        public static final int PRIORITY_LOW = 0;
        public static final int PRIORITY_MEDIUM = 1;
        public static final int PRIORITY_HIGH = 2;

        /**
         * Possible values for the status of the item.
         */
        public static final int STATUS_TODO = 0;
        public static final int STATUS_INPROGRESS = 1;
        public static final int STATUS_DONE = 2;
    }
}
