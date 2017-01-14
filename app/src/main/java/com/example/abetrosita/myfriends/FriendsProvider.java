package com.example.abetrosita.myfriends;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by AbetRosita on 12/28/2016.
 */

public class FriendsProvider extends ContentProvider{
    private FriendsDatabase mOpenHelper;

    private static String TAG = FriendsProvider.class.getSimpleName();
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static final int FRIENDS = 100;
    private static final int FRIENDS_ID = 101;

    private static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FriendsContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, FriendsContract.PATH_FRIENDS, FRIENDS);
        matcher.addURI(authority, FriendsContract.PATH_FRIENDS + "/*", FRIENDS_ID);

        return matcher;
    }

    public void deleteDatabase(){
        mOpenHelper.close();
        FriendsDatabase.deleteDatabase(getContext());
        mOpenHelper = new FriendsDatabase(getContext());
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new FriendsDatabase(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        final int match = sUriMatcher.match(uri);

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(FriendsDatabase.Tables.FRIENDS);

        switch (match) {
            case FRIENDS:
                // do nothing
                break;
            case FRIENDS_ID:
                String id = uri.getLastPathSegment();
                Log.d("testUri", String.valueOf(uri));
                queryBuilder.appendWhere(BaseColumns._ID +"="+ id);
                break;
            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }

        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FRIENDS:
                return FriendsContract.Friends.CONTENT_TYPE;
            case FRIENDS_ID:
                return FriendsContract.Friends.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.v(TAG, "insert(uri=" + uri + " values=" + values.toString());
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case FRIENDS:
                long recordId = db.insertOrThrow(FriendsDatabase.Tables.FRIENDS, null, values);
                if(recordId > 0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return FriendsContract.Friends.buildFriendUri(String.valueOf(recordId));
            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.v(TAG, "delete(uri=" + uri);
        int numRowsDeleted = 0;
        if(uri.equals(FriendsContract.URI_TABLE)){
            deleteDatabase();
            return numRowsDeleted;
        }
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case FRIENDS_ID:
                String id = FriendsContract.Friends.getFriendId(uri);
                String selectionCriteria = BaseColumns._ID + "=" + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
                numRowsDeleted = db.delete(FriendsDatabase.Tables.FRIENDS, selectionCriteria, selectionArgs);
                if (numRowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(FriendsContract.URI_TABLE, null);
                    Log.v(TAG, "++++ DELETED " + String.valueOf(numRowsDeleted));
                }
                return numRowsDeleted;
            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.v(TAG, "update(uri=" + uri + " values=" + values.toString());
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        
        int numRowsUpdated = 0;

        String selectionCriteria = selection;
        switch (match) {
            case FRIENDS:
                break;
            case FRIENDS_ID:
                String id = FriendsContract.Friends.getFriendId(uri);
                selectionCriteria = BaseColumns._ID + "=" + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
                break;
            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }

        numRowsUpdated = db.update(FriendsDatabase.Tables.FRIENDS, values, selectionCriteria, selectionArgs);
        /*if (numRowsUpdated > 0) {
            getContext().getContentResolver().notifyChange(FriendsContract.Friends.CONTENT_URI, null);
        }*/
        return numRowsUpdated;
        
    }

}
