package com.vipinkr.tasktimer;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Vipin K R on 21-01-2017.
 */

///////////////////////////////////////////////////////////////////////////
// Content provider class
///////////////////////////////////////////////////////////////////////////

public class AppProvider extends ContentProvider {
    private static final String TAG = "AppProvider";

    private AppDatabase mOpenHelper;
    public static final UriMatcher sUriMatcher = buildUriMatcher();

    static final String CONTENT_AUTHORITY = "com.vipinkr.tasktimer.provider";
    public static final Uri CONTENT_AUTHORITY_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final int TASKS = 100;
    public static final int TASKS_ID = 101;

    private static final int TIMINGS = 200;
    private static final int TIMINGS_ID = 201;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        //content authority,path=>com.vipinkr.tasktimer.provider/Tasks,code
        matcher.addURI(CONTENT_AUTHORITY, TaskContract.TABLE_NAME, TASKS);//returns 100
        //content authority,path=>com.vipinkr.tasktimer.provider/Tasks/8,code
        matcher.addURI(CONTENT_AUTHORITY, TaskContract.TABLE_NAME + "/#", TASKS_ID);//returns 101

        return matcher;

    }

    @Override
    public boolean onCreate() {
        Log.d(TAG, "onCreate: starts");
        mOpenHelper = AppDatabase.getInstance(getContext());
        Log.d(TAG, "onCreate: mOpenHelper: " + mOpenHelper);
        Log.d(TAG, "onCreate: ends");
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.d(TAG, "query: starts");
        final int match = sUriMatcher.match(uri);
        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
        switch (match) {
            case TASKS:
                sqLiteQueryBuilder.setTables(TaskContract.TABLE_NAME);
                Log.d(TAG, "query: " + TASKS);
                break;
            case TASKS_ID:
                Log.d(TAG, "query: " + TASKS);
                sqLiteQueryBuilder.setTables(TaskContract.TABLE_NAME);
                long taskId = TaskContract.getTaskId(uri);
                sqLiteQueryBuilder.appendWhere(TaskContract.Column._ID + "=" + taskId);
                break;

            default:
                throw new IllegalArgumentException("unknown uri");
        }
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
//        return queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        Cursor cursor = sqLiteQueryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        Log.d(TAG, "query: rows in returned cursor = " + cursor.getCount()); // TODO remove this line

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {

            case TASKS:
                return TaskContract.CONTENT_TYPE;

            case TASKS_ID:
                return TaskContract.CONTENT_ITEM_TYPE;

            default:
                throw new IllegalArgumentException("unknown uri");
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        SQLiteDatabase db;
        Uri returnUri;
        long recordId;

        switch (match) {

            case TASKS:
                db = mOpenHelper.getWritableDatabase();
                recordId = db.insert(TaskContract.TABLE_NAME, null, contentValues);
                if (recordId > 0) {
                    returnUri = TaskContract.buildTaskUri(recordId);
                } else {
                    throw new android.database.SQLException("failed to insert into: " + uri.toString());
                }
                break;


            default:
                throw new IllegalArgumentException("unknown uri");
        }
        if (recordId >= 0) {
            // something was inserted
            Log.d(TAG, "insert: Setting notifyChangd with " + uri);
            getContext().getContentResolver().notifyChange(uri, null);
        } else {
            Log.d(TAG, "insert: nothing inserted");
        }

        Log.d(TAG, "Exiting insert, returning " + returnUri);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        SQLiteDatabase db;
        String selectionCriteria;
        int count;
        switch (match) {

            case TASKS:
                db = mOpenHelper.getWritableDatabase();
                count = db.delete(TaskContract.TABLE_NAME, selection, selectionArgs);
                break;
            case TASKS_ID:
                db=mOpenHelper.getWritableDatabase();
                long taskId=TaskContract.getTaskId(uri);
                selectionCriteria=TaskContract.Column._ID+"="+taskId;
                if((selection!=null) && (selection.length()>0)){
                    selectionCriteria+=" AND ("+selection+")";
                }
                count=db.delete(TaskContract.TABLE_NAME,selectionCriteria, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("unknown uri");
        }
        if(count > 0) {
            // something was deleted
            Log.d(TAG, "delete: Setting notifyChange with " + uri);
            getContext().getContentResolver().notifyChange(uri, null);
        } else {
            Log.d(TAG, "delete: nothing deleted");
        }

        Log.d(TAG, "Exiting update, returning " + count);
        return count;

    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        SQLiteDatabase db;
        String selectionCriteria;
        int count;
        switch (match) {

            case TASKS:
                db = mOpenHelper.getWritableDatabase();
                count = db.update(TaskContract.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            case TASKS_ID:
                db=mOpenHelper.getWritableDatabase();
                long taskId=TaskContract.getTaskId(uri);
                selectionCriteria=TaskContract.Column._ID+"="+taskId;
                if((selection!=null) && (selection.length()>0)){
                    selectionCriteria+=" AND ("+selection+")";
                }
                count=db.update(TaskContract.TABLE_NAME, contentValues, selectionCriteria, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("unknown uri");
        }
        if(count > 0) {
            // something was deleted
            Log.d(TAG, "update: Setting notifyChange with " + uri);
            getContext().getContentResolver().notifyChange(uri, null);
        } else {
            Log.d(TAG, "update: nothing deleted");
        }

        Log.d(TAG, "Exiting update, returning " + count);
        return count;
    }
}
