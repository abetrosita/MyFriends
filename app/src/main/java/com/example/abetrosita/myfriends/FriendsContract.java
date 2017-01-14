package com.example.abetrosita.myfriends;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by AbetRosita on 12/28/2016.
 */

public class FriendsContract {

    interface FriendsColumns {
        String FRIENDS_NAME = "friends_name";
        String FRIENDS_EMAIL = "friends_email";
        String FRIENDS_PHONE = "friends_phone";
    }

    public static final String CONTENT_AUTHORITY = "com.example.abetrosita.myfriends.provider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_FRIENDS = "friends";
    public static final Uri URI_TABLE = BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_FRIENDS).build();
    public static final String VND_PREFIX = "vnd.android.cursor.";
    public static final String[] TOP_LEVEL_PATHS = {PATH_FRIENDS};

    public static class Friends implements FriendsColumns, BaseColumns {
        public static final String CONTENT_TYPE = VND_PREFIX + "dir/" + CONTENT_AUTHORITY + "." + PATH_FRIENDS;
        public static final String CONTENT_ITEM_TYPE = VND_PREFIX + "item/" + CONTENT_AUTHORITY + "." + PATH_FRIENDS;

        public static Uri buildFriendUri(String friendId) {
            return URI_TABLE.buildUpon().appendEncodedPath(friendId).build();
        }

        public static String getFriendId(Uri uri){
            return uri.getPathSegments().get(1);
        }

    }

}
