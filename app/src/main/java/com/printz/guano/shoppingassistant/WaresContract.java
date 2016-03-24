package com.printz.guano.shoppingassistant;

import android.net.Uri;
import android.provider.BaseColumns;

public class WaresContract {

    public interface WaresColumns {
        String WARE_ID = "_id";
        String WARE_NAME = "ware_name";
        String WARE_AMOUNT = "ware_amount";
    }

    public static final String CONTENT_AUTHORITY = "com.printz.guano.shoppingassistant.provider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final String PATH_WARES = "wares";
    public static final Uri TABLE_URI = Uri.parse(BASE_CONTENT_URI.toString() + "/" + PATH_WARES);

    public static class Wares implements WaresColumns, BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_WARES).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + CONTENT_AUTHORITY + ".wares";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + CONTENT_AUTHORITY + ".wares";

        public static Uri buildWareUri(String wareId) {
            return CONTENT_URI.buildUpon().appendEncodedPath(wareId).build();
        }

        public static String getWareId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

}
