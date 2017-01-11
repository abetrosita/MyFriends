package com.example.abetrosita.myfriends;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by AbetRosita on 12/29/2016.
 */

public class FriendsListLoader extends AsyncTaskLoader<Cursor> {
    private static final String LOG_TAG = FriendsListLoader.class.getSimpleName();
    //private List<Friend> mFriends;
    private ContentResolver mContentResolver;
    private Cursor mCursor;

    public FriendsListLoader(Context context, Uri uri, ContentResolver contentResolver){
        super(context);
        mContentResolver = contentResolver;
    }

    public FriendsListLoader(Context context) {
        super(context);
    }

    @Override
    public void setUpdateThrottle(long delayMS) {
        super.setUpdateThrottle(delayMS);
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
    }

    @Override
    protected boolean onCancelLoad() {
        return super.onCancelLoad();
    }

    @Override
    public void onCanceled(Cursor cursor) {
        super.onCanceled(cursor);
        if(mCursor != null) {
            mCursor.close();
        }
    }

    @Override
    public Cursor loadInBackground() {
        String[] projection = {
                BaseColumns._ID,
                FriendsContract.FriendsColumns.FRIENDS_NAME,
                FriendsContract.FriendsColumns.FRIENDS_PHONE,
                FriendsContract.FriendsColumns.FRIENDS_EMAIL
        };

        List<Friend> entries = new ArrayList<Friend>();
        mCursor = mContentResolver.query(FriendsContract.URI_TABLE, projection, null, null, null);
        /*
        if(mCursor !=null){
            if(mCursor.moveToFirst()){
                do{
                    int _id = mCursor.getInt(mCursor.getColumnIndex(BaseColumns._ID));
                    String name = mCursor.getString(mCursor.getColumnIndex(FriendsContract.Friends.FRIENDS_NAME));
                    String phone = mCursor.getString(mCursor.getColumnIndex(FriendsContract.Friends.FRIENDS_PHONE));
                    String email = mCursor.getString(mCursor.getColumnIndex(FriendsContract.Friends.FRIENDS_EMAIL));
                    Friend friend = new Friend(_id, name, phone, email);
                    entries.add(friend);
                }while (mCursor.moveToNext());
            }
        }

        return entries;
        */
        return mCursor;
    }

    @Override
    public void deliverResult(Cursor cursor) {

        if(isReset()){
            if(cursor != null){
                mCursor.close();
            }
        }

//        List<Friend> oldFriendList = mFriends;
        Cursor oldCursor = mCursor;
        if(mCursor == null){
            Log.d(LOG_TAG, "+++++ No data returned");
        }

//        mFriends = friends;

        if(isStarted()) {
            super.deliverResult(cursor);
        }

        if(oldCursor != null && oldCursor != cursor) {
            mCursor.close();
        }
    }

    @Override
    protected void onStartLoading() {
        if(mCursor != null) {
            deliverResult(mCursor);
        }
        if(takeContentChanged() || mCursor == null) {
            forceLoad();
        }
    }

    @Override
    protected void onReset() {
        onStopLoading();
        if(mCursor != null){
            mCursor.close();
        }
        mCursor = null;
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected Cursor onLoadInBackground() {
        return super.onLoadInBackground();
    }

    @Override
    public void cancelLoadInBackground() {
        super.cancelLoadInBackground();
    }

    @Override
    public boolean isLoadInBackgroundCanceled() {
        return super.isLoadInBackgroundCanceled();
    }

    @Override
    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        super.dump(prefix, fd, writer, args);
    }
}
