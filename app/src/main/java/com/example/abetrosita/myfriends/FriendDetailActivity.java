package com.example.abetrosita.myfriends;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FriendDetailActivity extends AppCompatActivity {

    Button buttonSave;
    private String mTitle;
    private static boolean isUpdate = false;
    private String _id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Button button = (Button) findViewById(R.id.btn_save);

        mTitle = getIntent().getStringExtra(FriendConstants.INTENT_EXTRA_TITLE);

        isUpdate = mTitle.equals(FriendConstants.ACTIVITY_TITLE_UPDATE);
        if(isUpdate){
            button.setText("Update Friend");
            ((EditText) findViewById(R.id.et_friend_name)).setText(
                    getIntent().getStringExtra(FriendConstants.INTENT_EXTRA_NAME));
            ((EditText) findViewById(R.id.et_friend_phone)).setText(
                    getIntent().getStringExtra(FriendConstants.INTENT_EXTRA_PHONE));
            ((EditText) findViewById(R.id.et_friend_email)).setText(
                    getIntent().getStringExtra(FriendConstants.INTENT_EXTRA_EMAIL));
            _id =   getIntent().getStringExtra(FriendConstants.INTENT_EXTRA_ID);
        }

        buttonSave = (Button) findViewById(R.id.btn_save);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put(FriendsContract.FriendsColumns.FRIENDS_NAME,
                        ((EditText) findViewById(R.id.et_friend_name)).getText().toString());
                values.put(FriendsContract.FriendsColumns.FRIENDS_PHONE,
                        ((EditText) findViewById(R.id.et_friend_phone)).getText().toString());
                values.put(FriendsContract.FriendsColumns.FRIENDS_EMAIL,
                        ((EditText) findViewById(R.id.et_friend_email)).getText().toString());

                try {
                    if(isUpdate){
                        Uri uri = FriendsContract.Friends.buildFriendUri(_id);
                        MainActivity.mContentResolver.update(uri, values, null, null);
                    }else {
                        MainActivity.mContentResolver.insert(FriendsContract.URI_TABLE, values);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

                Intent intent = new Intent(FriendDetailActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_edit, menu);
        this.setTitle(mTitle);
        return true;
    }
}
