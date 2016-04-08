package com.printz.guano.shoppingassistant.edit_list;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.printz.guano.shoppingassistant.database.DatabaseLib;
import com.printz.guano.shoppingassistant.database.WareHistoryContract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WareHistoryAdapter extends ArrayAdapter<WareHistory> {

    private static final String LOG_TAG = WareHistoryAdapter.class.getSimpleName();

    private List<WareHistory> mWareHistories;
    private DatabaseLib mDatabaseLib;

    public WareHistoryAdapter(Context context, int resource) {
        super(context, resource);

        mWareHistories = new ArrayList<>();
        mDatabaseLib = new DatabaseLib(context.getContentResolver());
    }

    /**
     * Inserts a ware into ware history or updates if one of the same ware name exists. This
     * insert or update is mirrored in the local database.
     *
     * @param wareHistory The WareHistory to insert.
     */
    public void insertWareHistory(WareHistory wareHistory) {
        WareHistory existingWareHistory = isExistingWareHistory(wareHistory);

        if (existingWareHistory != null) {
            updateWareHistory(existingWareHistory);
        } else {
            addWareHistory(wareHistory);
        }

        setWareHistories(mWareHistories);
    }

    /**
     * Populates the WareHistory data of the auto complete box
     *
     * @param wareHistories The WareHistories to update the adapter with.
     */
    public void setWareHistories(List<WareHistory> wareHistories) {

        clear();
        if (wareHistories != null) {
            mWareHistories = wareHistories;
            sortWareHistories(wareHistories);
            for (WareHistory wareHistory : wareHistories) {
                add(wareHistory);
            }
        }
        notifyDataSetChanged();
    }

    /**
     * This method retrieves all the wares in the adapter and returns them.
     *
     * @return Returns all the wares in the adapter.
     */
    public ArrayList<WareHistory> getWareHistories() {
        return (ArrayList) mWareHistories;
    }

    private void addWareHistory(WareHistory wareHistory) {
        Log.d(LOG_TAG, "Inserting ware history " + "'" + wareHistory.getName() + "'");

        Uri uri = mDatabaseLib.insertWareHistory(wareHistory);
        String wareHistoryId = WareHistoryContract.WareHistory.getWareHistoryId(uri);
        wareHistory.setId(Integer.valueOf(wareHistoryId));

        mWareHistories.add(wareHistory);
    }

    private int updateWareHistory(WareHistory wareHistory) {
        Log.d(LOG_TAG, "Updating ware history " + "'" + wareHistory.getName() + "'");

        wareHistory.incrementCount();

        return mDatabaseLib.updateWareHistory(wareHistory);
    }

    private void sortWareHistories(List<WareHistory> wareHistories) {
        Collections.sort(wareHistories);
        Collections.reverse(wareHistories);
    }

    private WareHistory isExistingWareHistory(WareHistory wareHistory) {
        for (WareHistory wHistory : mWareHistories) {
            if (wareHistory.equals(wHistory)) {
                return wHistory;
            }
        }

        return null;
    }
}
