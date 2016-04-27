package com.printz.guano.shoppingassistant.database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.printz.guano.shoppingassistant.UserSession;
import com.printz.guano.shoppingassistant.grocery_list.Ware;
import com.printz.guano.shoppingassistant.grocery_list.WareHistory;

import java.util.List;

public class DatabaseLib {

    private static final String LOG_TAG = DatabaseLib.class.getSimpleName();

    private ContentResolver mContentResolver;

    public DatabaseLib(ContentResolver contenResolver) {
        this.mContentResolver = contenResolver;
    }

    public Uri insertUser(String userId, String userName) {
        ContentValues values = new ContentValues();
        values.put(Contract.UserColumns.U_ID, userId);
        values.put(Contract.UserColumns.USERNAME, userName);
        return mContentResolver.insert(Contract.User.CONTENT_URI, values);
    }

    public boolean isExistingUser(String userName) {
        String[] projection = {Contract.UserColumns.USERNAME };
        String selection = Contract.UserColumns.USERNAME + "=?";
        String[] selectionArgs = { userName };

        Cursor cursor = mContentResolver.query(Contract.User.CONTENT_URI, projection, selection, selectionArgs, null);
        boolean exists = false;

        if(cursor != null) {
            exists = cursor.getCount() > 0;
            cursor.close();
        }

        return exists;
    }

    private String getUserId() {
        UserSession userSession = UserSession.getUserSession();

        if (!userSession.isSessionActive()) {
            return "1"; // default user ID
        }

        // user is logged, get username
        String userName = userSession.getUserName();

        String[] projection = {
                Contract.UserColumns.U_ID
        };

        String selection = Contract.UserColumns.USERNAME + "=?";

        String[] selectionArgs = {
                userName
        };

        Cursor cursor = mContentResolver.query(Contract.User.CONTENT_URI, projection, selection, selectionArgs, null);

        int userId = 0;

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    userId = cursor.getInt(cursor.getColumnIndex(Contract.UserColumns.U_ID));
                } while (cursor.moveToNext());
            }

            cursor.close();
        }

        return String.valueOf(userId);
    }

    /**
     * Inserts a new WareHistory into the database.
     *
     * @param wareHistory The WareHistory to insert.
     * @return The uri of the new inserted WareHistory row
     */
    public Uri insertWareHistory(WareHistory wareHistory) {
        Log.d(LOG_TAG, "Inserting new ware history " + wareHistory.getName());
        String userId = getUserId();

        ContentValues values = new ContentValues();
        values.put(Contract.WareHistoryColumns.WARE_HISTORY_NAME, wareHistory.getName());
        values.put(Contract.WareHistoryColumns.WARE_HISTORY_COUNT, wareHistory.getCount());
        values.put(Contract.WareHistoryColumns.USER_ID, userId);

        return mContentResolver.insert(Contract.WareHistory.CONTENT_URI, values);
    }

    /**
     * Updates the WareHistory in the database with a +1 count
     *
     * @param wareHistory The WareHistory to update
     * @return Returns the number of updated WareHistories
     */
    public int updateWareHistory(WareHistory wareHistory) {
        Uri uri = Contract.WareHistory.buildWareHistoryUri(String.valueOf(wareHistory.getId()));

        ContentValues values = new ContentValues();
        values.put(Contract.WareHistoryColumns.WARE_HISTORY_COUNT, wareHistory.getCount());

        return mContentResolver.update(uri, values, null, null);
    }

    /**
     * Deletes all in the supplied list of ware in the database
     * @param wares The wares to delete
     * @return The number of wares deleted
     */
    public int deleteWares(List<Ware> wares) {
        int deleteCount = 0;

        for(Ware ware : wares) {
            deleteCount += deleteWare(ware);
        }

        return deleteCount;
    }

    /**
     * Insert a new Ware into the database
     *
     * @param ware The ware to insert
     * @return The Uri of the inserted ware
     */
    public Uri insertWare(Ware ware) {
        Log.d(LOG_TAG, "Inserting new ware " + ware.getName());
        String userId = getUserId();

        ContentValues values = new ContentValues();
        values.put(Contract.WareColumns.WARE_NAME, ware.getName());
        values.put(Contract.WareColumns.WARE_POSITION, ware.getPosition());
        values.put(Contract.WareColumns.WARE_IS_MARKED, ware.isMarked());
        values.put(Contract.WareColumns.WARE_AMOUNT, ware.getAmount());
        values.put(Contract.WareColumns.WARE_QUANTITY_TYPE, ware.getType());
        values.put(Contract.WareColumns.USER_ID, userId);

        return mContentResolver.insert(Contract.Ware.CONTENT_URI, values);
    }

    /**
     * Delete the ware in the database with the supplied ware id
     *
     * @param ware The ware to delete
     * @return The number of wares deleted
     */
    public int deleteWare(Ware ware) {
        Uri uri = Contract.Ware.buildWareUri(String.valueOf(ware.getId()));
        return mContentResolver.delete(uri, null, null);
    }

    public int updateWare(Ware ware) {
        ContentValues values = new ContentValues();
        values.put(Contract.WareColumns.WARE_NAME, ware.getName());
        values.put(Contract.WareColumns.WARE_POSITION, ware.getPosition());
        values.put(Contract.WareColumns.WARE_IS_MARKED, ware.isMarked());
        values.put(Contract.WareColumns.WARE_QUANTITY_TYPE, ware.getType());
        values.put(Contract.WareColumns.WARE_AMOUNT, ware.getAmount());

        Uri uri = Contract.Ware.buildWareUri(String.valueOf(ware.getId()));
        return mContentResolver.update(uri, values, null, null);
    }
}
