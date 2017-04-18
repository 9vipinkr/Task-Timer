package com.vipinkr.tasktimer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Vipin K R on 21-01-2017.
 */
///////////////////////////////////////////////////////////////////////////
// Database HELPER class instance should only be used by content provider
///////////////////////////////////////////////////////////////////////////

  //singleton class

class AppDatabase extends SQLiteOpenHelper {

    private static final String TAG = "AppDatabase";

    public static final String DATABASE_NAME="TaskTimer.db";
    public static final int DATABASE_VERSION=1;
    private static AppDatabase instance=null;


   private  AppDatabase(Context context){
       super(context,DATABASE_NAME,null,DATABASE_VERSION);
   }

    //this makes sure only one object can be created but it is not thread safe
    public static AppDatabase getInstance(Context context){
        if(instance==null){
            instance=new AppDatabase(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        Log.d(TAG, "onCreate: starts");
       String sql;
        sql="create table "+TaskContract.TABLE_NAME+"("+
                TaskContract.Column._ID+" INTEGER PRIMARY KEY NOT NULL,"+
                TaskContract.Column.TASKS_NAME+" TEXT NOT NULL,"+
                TaskContract.Column.TASKS_DESCRIPTION+" TEXT,"+
                TaskContract.Column.TASKS_SORTORDER+" INTEGER);";
        Log.d(TAG, "onCreate: sql= "+sql);
        sqLiteDatabase.execSQL(sql);

        Log.d(TAG, "onCreate: ends");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade: starts");
        switch(oldVersion) {
            case 1:
                // upgrade logic from version 1
                break;
            default:
                throw new IllegalStateException("onUpgrade() with unknown newVersion: " + newVersion);
        }
        Log.d(TAG, "onUpgrade: ends");
    }
}
