package com.printz.guano.shoppingassistant.database;

import android.net.Uri;

public class UserContract {

    public interface UserColumns {
        String U_ID = "uId";
        String USERNAME = "username";
        String PASSWORD = "password";
    }

    public static final String CONTENT_AUTHORITY = "com.printz.guano.shoppingassistant.userProvider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final String PATH_USER = "users";
    public static final Uri TABLE_URI = Uri.parse(BASE_CONTENT_URI.toString() + "/" + PATH_USER);

    public static class User implements UserColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_USER).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + CONTENT_AUTHORITY + ".user";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + CONTENT_AUTHORITY + ".user";

        public static Uri buildUserUri(String wareId) {
            return CONTENT_URI.buildUpon().appendEncodedPath(wareId).build();
        }

        public static String getUserId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

}
