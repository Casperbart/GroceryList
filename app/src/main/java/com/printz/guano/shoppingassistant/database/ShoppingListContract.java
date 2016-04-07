package com.printz.guano.shoppingassistant.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class ShoppingListContract {

    public interface ShoppingListColumns {
        String S_ID = "sId";
        String USER_ID = "user_id";
    }

    public static final String CONTENT_AUTHORITY = "com.printz.guano.shoppingassistant.shoppingListProvider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final String PATH_SHOPPING_LISTS = "shopping_lists";
    public static final Uri TABLE_URI = Uri.parse(BASE_CONTENT_URI.toString() + "/" + PATH_SHOPPING_LISTS);

    public static class ShoppingList implements ShoppingListColumns, BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_SHOPPING_LISTS).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + CONTENT_AUTHORITY + ".shopping_lists";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + CONTENT_AUTHORITY + ".shopping_lists";

        public static Uri buildWareUri(String wareId) {
            return CONTENT_URI.buildUpon().appendEncodedPath(wareId).build();
        }

        public static String getWareId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

}
