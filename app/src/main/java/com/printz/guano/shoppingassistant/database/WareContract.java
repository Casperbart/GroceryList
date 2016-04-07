package com.printz.guano.shoppingassistant.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class WareContract {

    public interface WareColumns {
        String W_ID = "wId";
        String WARE_NAME = "name";
        String WARE_POSITION = "position";
        String WARE_IS_MARKED = "isMarked";
        String WARE_QUANTITY_TYPE = "quantityType";
        String WARE_AMOUNT = "amount";
        String SHOPPING_LIST_ID = "shopping_list_id";
    }

    public static final String CONTENT_AUTHORITY = "com.printz.guano.shoppingassistant.wareProvider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final String PATH_WARES = "wares";
    public static final Uri TABLE_URI = Uri.parse(BASE_CONTENT_URI.toString() + "/" + PATH_WARES);

    public static class Ware implements WareColumns, BaseColumns {
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
