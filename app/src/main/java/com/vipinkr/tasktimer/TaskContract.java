package com.vipinkr.tasktimer;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;



/**
 * Created by Vipin K R on 21-01-2017.
 */

///////////////////////////////////////////////////////////////////////////
// Constants are defined in this class (database column names,content uri's)
///////////////////////////////////////////////////////////////////////////

public class TaskContract {
    //database table name
    static final String TABLE_NAME = "Tasks";

    public static final String CONTENT_TYPE="vnd.android.cursor.dir/vnd."+AppProvider.CONTENT_AUTHORITY+"."+TABLE_NAME;
    public static final String CONTENT_ITEM_TYPE= "vnd.android.cursor.item/vnd."+AppProvider.CONTENT_AUTHORITY+"."+TABLE_NAME;

    //CONTENT_AUTHORITY_URI from AppProvider class
    //uri to access tasks table
    public static final Uri CONTENT_URI=Uri.withAppendedPath(AppProvider.CONTENT_AUTHORITY_URI,TABLE_NAME);

    static Uri buildTaskUri(long taskId){
     return ContentUris.withAppendedId(CONTENT_URI,taskId);
    }

    static  long getTaskId(Uri uri){
        return ContentUris.parseId(uri);
    }

    public static class Column {
        //database column names
        public static final String _ID = BaseColumns._ID;
        public static final String TASKS_NAME = "Name";
        public static final String TASKS_DESCRIPTION = "Description";
        public static final String TASKS_SORTORDER="SortOrder";

        private Column(){
            //object of this class cannot be instantiated
        }
    }

}

