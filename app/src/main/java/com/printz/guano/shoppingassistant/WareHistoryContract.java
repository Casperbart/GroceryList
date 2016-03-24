package com.printz.guano.shoppingassistant;

import android.net.Uri;
import android.provider.BaseColumns;

public class WareHistoryContract {

    public interface WareHistoryColumns {
        String WARE_HISTORY_ID = "_id";
        String WARE_HISTORY_NAME = "ware_history_name";
        String WARE_HISTORY_COUNT = "ware_history_count";
    }

    public static final String CONTENT_AUTHORITY = "com.printz.guano.shoppingassistant.provider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final String PATH_WARE_HISTORY = "ware_histories";
    public static final Uri TABLE_URI = Uri.parse(BASE_CONTENT_URI.toString() + "/" + PATH_WARE_HISTORY);

    public static class WareHistory implements WareHistoryColumns, BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_WARE_HISTORY).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + CONTENT_AUTHORITY + ".wares";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + CONTENT_AUTHORITY + ".wares";

        public static Uri buildWareHistoryUri(String wareId) {
            return CONTENT_URI.buildUpon().appendEncodedPath(wareId).build();
        }

        public static String getWareHistoryId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

}
