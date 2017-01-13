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

/**
 * Created by AbetRosita on 12/29/2016.
 */

public class FriendsListLoader extends AsyncTaskLoader<Cursor> {
    private static final String LOG_TAG = FriendsListLoader.class.getSimpleName();
    private ContentResolver mContentResolver;
    private Cursor mCursor;
    private String mFilterText;

    public FriendsListLoader(Context context, Uri uri, ContentResolver contentResolver, String filterText){
        super(context);
        mContentResolver = contentResolver;
        mFilterText = filterText;
    }

    public FriendsListLoader(Context context, Uri uri, ContentResolver contentResolver){
        super(context);
        mContentResolver = contentResolver;
        mFilterText = "";
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

        String selection = FriendsContract.FriendsColumns.FRIENDS_NAME + " LIKE '" + mFilterText + "%'";
        mCursor = mContentResolver.query(FriendsContract.URI_TABLE, projection, selection, null, null);
        return mCursor;
    }

    @Override
    public void deliverResult(Cursor cursor) {

        if(isReset()){
            Log.d(LOG_TAG, "+++++ Data came while loader is reset.");
            if(cursor != null){
                mCursor.close();
                return;
            }
        }

        Cursor oldCursor = mCursor;
        mCursor = cursor;
        if(mCursor == null){
            Log.d(LOG_TAG, "+++++ No data returned.");
        }

        if(isStarted()) {
            Log.d(LOG_TAG, "+++ Delivering results to the LoaderManager.");
            super.deliverResult(cursor);
        }

        if(oldCursor != null && oldCursor != cursor) {
            Log.d(LOG_TAG, "+++++ Releasing any old data associated with this Loader.");
            oldCursor.close();
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
        Log.d(LOG_TAG, "+++++ On reset called.");
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
