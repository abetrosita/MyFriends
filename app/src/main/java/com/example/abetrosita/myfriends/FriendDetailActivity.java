package com.example.abetrosita.myfriends;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FriendDetailActivity extends AppCompatActivity {

    Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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
                    MainActivity.mContentResolver.insert(FriendsContract.URI_TABLE, values);
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
        this.setTitle(getIntent().getStringExtra("title"));
        return true;
    }
}
