package com.codepath.simpletodo.data;

import android.provider.BaseColumns;

/**
 * API Contract for the SimpleTodo app
 */
public final class TodoItemContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor
    private TodoItemContract() {}

    // Inner class that defines constant values for the To-do database table
    public static final class ItemEntry implements BaseColumns {

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
