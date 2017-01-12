package com.example.abetrosita.myfriends;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import static android.view.View.GONE;
import static com.example.abetrosita.myfriends.FriendConstants.ACTIVITY_TITLE_UPDATE;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>, FriendAdapter.FriendAdapterOnClickHandler{

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int LOADER_ID = 1;

    private Toast mToast;
    public static FriendAdapter mFriendAdapter;
    public static ContentResolver mContentResolver;
    public static Context mContext;
    private RecyclerView mFriendList;
    private Cursor mCursor;

    private View lastView;
    private boolean editVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        mFriendList = (RecyclerView) findViewById(R.id.rv_friends);
        mFriendList.setLayoutManager(layoutManager);
        mFriendList.setHasFixedSize(false);
        mContext = this;

        mContentResolver = this.getContentResolver();
        mFriendAdapter = new FriendAdapter(mCursor, this);
        mFriendList.setAdapter(mFriendAdapter);
        
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
        int id = item.getItemId();
        switch (item.getItemId()){
            case R.id.addRecord:
                Intent intent = new Intent(MainActivity.this, FriendDetailActivity.class);
                intent.putExtra(FriendConstants.INTENT_EXTRA_TITLE, FriendConstants.ACTIVITY_TITLE_ADD);
                startActivity(intent);
                break;
            case R.id.deleteDatabase:

                break;
            case R.id.searchRecord:

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        mContentResolver = this.getContentResolver();
        return new FriendsListLoader(this, FriendsContract.URI_TABLE, mContentResolver);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mCursor = cursor;
        mFriendAdapter.loadFriendsData(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mFriendAdapter.loadFriendsData(null);
    }

    private void showToast(String message){
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        mToast.show();
    }

    @Override
    public void onClick(View viewDetail, View viewAction) {

        String viewName = String.valueOf(getResources().getResourceEntryName(viewDetail.getId()));
        Uri uri = FriendsContract.Friends.buildFriendUri(String.valueOf(viewAction.getTag()));

        switch (viewName){
            case "ll_friend_detail":
                if(lastView != null){
                    lastView.setVisibility(GONE);
                }
                if(viewAction==lastView && editVisible){
                    viewAction.setVisibility(View.GONE);
                    editVisible = false;
                }else {
                    viewAction.setVisibility(View.VISIBLE);
                    lastView = viewAction;
                    editVisible = true;
                }
                break;

            case "tv_friend_delete":
                mContentResolver.delete(uri, null, null);
                getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
                break;

            case "tv_friend_view": case "tv_friend_edit":
                //showToast("VIEW CLICKED");
                Intent intent = new Intent(MainActivity.this, FriendDetailActivity.class);
                intent.putExtra(FriendConstants.INTENT_EXTRA_TITLE, ACTIVITY_TITLE_UPDATE);
                Cursor friend = mContentResolver.query(uri,null,null,null,null);
                friend.moveToFirst();

                intent.putExtra(FriendConstants.INTENT_EXTRA_NAME, friend.getString(
                        friend.getColumnIndex(FriendsContract.FriendsColumns.FRIENDS_NAME)));
                intent.putExtra(FriendConstants.INTENT_EXTRA_PHONE, friend.getString(
                        friend.getColumnIndex(FriendsContract.FriendsColumns.FRIENDS_PHONE)));
                intent.putExtra(FriendConstants.INTENT_EXTRA_EMAIL, friend.getString(
                        friend.getColumnIndex(FriendsContract.FriendsColumns.FRIENDS_EMAIL)));
                intent.putExtra(FriendConstants.INTENT_EXTRA_ID, String.valueOf(viewAction.getTag()));
                startActivity(intent);
        }

    }
}
