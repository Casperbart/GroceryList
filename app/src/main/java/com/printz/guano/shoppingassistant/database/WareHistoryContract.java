package com.printz.guano.shoppingassistant.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class WareHistoryContract {

    public interface WareHistoryColumns {
        String H_ID = "hId";
        String WARE_HISTORY_NAME = "name";
        String WARE_HISTORY_COUNT = "count";
        String USER_ID = "user_id";
    }

    public static final String CONTENT_AUTHORITY = "com.printz.guano.shoppingassistant.wareHistoryProvider";
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
