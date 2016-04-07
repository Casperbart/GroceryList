package com.printz.guano.shoppingassistant.database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.printz.guano.shoppingassistant.UserSession;
import com.printz.guano.shoppingassistant.edit_list.Ware;

import java.util.List;
import java.util.Map;

public class DatabaseLib {

    private static final String LOG_TAG = DatabaseLib.class.getSimpleName();

    private ContentResolver mContentResolver;

    public DatabaseLib(ContentResolver contenResolver) {
        this.mContentResolver = contenResolver;
    }

    public void addStandardData(String userName, String password) {
        Uri uriUser = insertDefaultUser(userName, password);
        String userId = UserContract.User.getUserId(uriUser);

        insertDefaultShoppingList(userId);
    }

    private Uri insertDefaultUser(String userName, String password) {
        ContentValues values = new ContentValues();
        values.put(UserContract.UserColumns.USERNAME, userName);
        values.put(UserContract.UserColumns.PASSWORD, password);
        return mContentResolver.insert(UserContract.TABLE_URI, values);
    }

    private Uri insertDefaultShoppingList(String userId) {
        ContentValues values = new ContentValues();
        values.put(ShoppingListContract.ShoppingListColumns.USER_ID, userId);
        return mContentResolver.insert(ShoppingListContract.TABLE_URI, values);
    }

    public String getUserId() {
        UserSession userSession = UserSession.getUserSession();
        if (!userSession.isSessionActive()) { // only called on active user, should always fail
            return "1";
        }

        // user is logged in find non default id
        Map<String, String> credentials = userSession.getCredentials();
        String userName = credentials.get(UserSession.USERNAME);

        String[] projection = {
                UserContract.UserColumns.U_ID
        };

        String selection = UserContract.UserColumns.USERNAME + "=?";

        String[] selectionArgs = {
                userName
        };

        Cursor cursor = mContentResolver.query(UserContract.TABLE_URI, projection, selection, selectionArgs, null);

        int userId = 0;

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    userId = cursor.getInt(cursor.getColumnIndex(UserContract.UserColumns.U_ID));
                } while (cursor.moveToNext());
            }
        }

        return String.valueOf(userId);
    }

    /**
     * Inserts a new WareHistory into the database.
     *
     * @param wareName name of ware
     * @return The uri of the new inserted WareHistory row
     */
    public Uri insertWareHistory(String wareName) {
        Log.d(LOG_TAG, "Inserting new ware history " + wareName);
        String userId = getUserId();

        ContentValues values = new ContentValues();
        values.put(WareHistoryContract.WareHistoryColumns.WARE_HISTORY_NAME, wareName);
        values.put(WareHistoryContract.WareHistoryColumns.WARE_HISTORY_COUNT, 1);
        values.put(WareHistoryContract.WareHistoryColumns.USER_ID, userId);
        return mContentResolver.insert(WareHistoryContract.TABLE_URI, values);
    }

    /**
     * Updates the WareHistory in the database with a +1 count
     *
     * @param newCount      the count to update the ware in the database with
     * @param wareHistoryId the id to construct the warehistory Uri
     */
    public int updateWareHistory(int wareHistoryId, int newCount) {
        Uri uri = WareHistoryContract.WareHistory.buildWareHistoryUri(String.valueOf(wareHistoryId));
        ContentValues values = new ContentValues();
        values.put(WareHistoryContract.WareHistoryColumns.WARE_HISTORY_COUNT, newCount);
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
     * Enumerates the supplied list and deletes the marked wares in the database
     * @param wares The wares to delete the marked wares in
     * @return The number of wares deleted
     */
    public int deleteMarkedWares(List<Ware> wares) {
        int deleteCount = 0;

        for(Ware ware : wares) {
            if(ware.isMarked()) {
                deleteCount += deleteWare(ware);
            }
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
        values.put(WareContract.WareColumns.WARE_NAME, ware.getName());
        values.put(WareContract.WareColumns.WARE_POSITION, ware.getPosition());
        values.put(WareContract.WareColumns.WARE_IS_MARKED, ware.isMarked());
        values.put(WareContract.WareColumns.WARE_QUANTITY_TYPE, ware.getQuantityType());
        values.put(WareContract.WareColumns.WARE_AMOUNT, ware.getAmount());
        values.put(WareContract.WareColumns.SHOPPING_LIST_ID, userId);
        return mContentResolver.insert(WareContract.TABLE_URI, values);
    }

    /**
     * Delete the ware in the database with the supplied ware id
     *
     * @param ware The ware to delete
     * @return The number of wares deleted
     */
    public int deleteWare(Ware ware) {
        Uri uri = WareContract.Ware.buildWareUri(String.valueOf(ware.getId()));
        return mContentResolver.delete(uri, null, null);
    }

    /**
     * Updates the wares in the database.
     * @param wares The list of wares to update.
     * @return Returns the amount of wares updated. It should match the size of the input list.
     */
    public int updateWares(List<Ware> wares) {
        int updateCount = 0;

        for(Ware ware : wares) {
            ContentValues values = new ContentValues();
            values.put(WareContract.WareColumns.WARE_NAME, ware.getName());
            values.put(WareContract.WareColumns.WARE_POSITION, ware.getPosition());
            values.put(WareContract.WareColumns.WARE_IS_MARKED, ware.isMarked());
            values.put(WareContract.WareColumns.WARE_QUANTITY_TYPE, ware.getQuantityType());
            values.put(WareContract.WareColumns.WARE_AMOUNT, ware.getAmount());

            Uri uri = WareContract.Ware.buildWareUri(String.valueOf(ware.getId()));
            updateCount += mContentResolver.update(uri, values, null, null);
        }

        return updateCount;
    }
}
