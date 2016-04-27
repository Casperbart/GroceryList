package com.printz.guano.shoppingassistant.database;

import android.net.Uri;

public class Contract {

    public interface WareColumns {
        String W_ID = "wId";
        String WARE_NAME = "name";
        String WARE_POSITION = "position";
        String WARE_IS_MARKED = "isMarked";
        String WARE_QUANTITY_TYPE = "quantityType";
        String WARE_AMOUNT = "amount";
        String USER_ID = "userId";
    }

    public interface UserColumns {
        String U_ID = "uId";
        String USERNAME = "username";
    }

    public interface WareHistoryColumns {
        String H_ID = "hId";
        String WARE_HISTORY_NAME = "name";
        String WARE_HISTORY_COUNT = "count";
        String USER_ID = "user_id";
    }

    public interface InvitationColumns {
        String I_ID = "iId";
        String SENDER_GROUP_ID = "sender_groupId";
        String RECEIVER_USERNAME = "receiver_username";
        String STATUS = "status";
    }

    public interface CommonColumns {
        String SYNC_METHOD = "http_method";
        String SYNC_STATUS = "transaction_status";
    }


    public static final String CONTENT_AUTHORITY = "com.printz.guano.shoppingassistant.Provider";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static class Ware {
        private static final String PATH_WARES = "wares";

        public static final String DEFAULT_SORT_ORDER = WareColumns.WARE_IS_MARKED + ", " + WareColumns.WARE_POSITION;

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_WARES).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + CONTENT_AUTHORITY + ".wares";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + CONTENT_AUTHORITY + ".wares";

        public static Uri buildWareUri(String rowId) {
            return CONTENT_URI.buildUpon().appendEncodedPath(rowId).build();
        }

        public static String getWareId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static class WareHistory {
        private static final String PATH_WARE_HISTORIES = "ware_histories";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_WARE_HISTORIES).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + CONTENT_AUTHORITY + ".wares";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + CONTENT_AUTHORITY + ".wares";

        public static Uri buildWareHistoryUri(String wareId) {
            return CONTENT_URI.buildUpon().appendEncodedPath(wareId).build();
        }

        public static String getWareHistoryId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static class Group {
        private static final String PATH_GROUPS = "groups";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_GROUPS).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + CONTENT_AUTHORITY + ".group";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + CONTENT_AUTHORITY + ".group";

        public static Uri buildUserUri(String wareId) {
            return CONTENT_URI.buildUpon().appendEncodedPath(wareId).build();
        }

        public static String getUserId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static class Invitation {
        private static final String PATH_INVITATIONS = "invitations";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_INVITATIONS).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + CONTENT_AUTHORITY + ".invitations";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + CONTENT_AUTHORITY + ".invitations";

        public static Uri buildInvitationUri(String invitationId) {
            return CONTENT_URI.buildUpon().appendEncodedPath(invitationId).build();
        }

        public static String getInvitationId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static class User {
        private static final String PATH_USERS = "users";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_USERS).build();
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
