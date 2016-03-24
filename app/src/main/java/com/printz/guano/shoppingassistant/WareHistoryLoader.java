package com.printz.guano.shoppingassistant;

import android.content.AsyncTaskLoader;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class WareHistoryLoader extends AsyncTaskLoader<List<WareHistory>> {

    private final static String LOG_TAG = WareHistoryLoader.class.getSimpleName();
    private List<WareHistory> mWareHistories;
    private ContentResolver mContentResolver;
    private Cursor mCursor;

    public WareHistoryLoader(Context context, ContentResolver contentResolver) {
        super(context);
        mContentResolver = contentResolver;
    }

    @Override
    public List<WareHistory> loadInBackground() {
        String[] projection = {
                BaseColumns._ID,
                WareHistoryContract.WareHistoryColumns.WARE_HISTORY_NAME,
                WareHistoryContract.WareHistoryColumns.WARE_HISTORY_COUNT
        };
        List<WareHistory> entries = new ArrayList<>();

        mCursor = mContentResolver.query(WareHistoryContract.TABLE_URI, projection, null, null, null);

        if(mCursor != null) {
            if(mCursor.moveToFirst()) {
                do {
                    int id = mCursor.getInt(mCursor.getColumnIndex(BaseColumns._ID));
                    String name = mCursor.getString(mCursor.getColumnIndex(WareHistoryContract.WareHistoryColumns.WARE_HISTORY_NAME));
                    int count = mCursor.getInt(mCursor.getColumnIndex(WareHistoryContract.WareHistoryColumns.WARE_HISTORY_COUNT));
                    WareHistory wareHistory = new WareHistory(id, name, count);
                    entries.add(wareHistory);
                } while (mCursor.moveToNext());
            }
        }

        return entries;
    }

    @Override
    public void deliverResult(List<WareHistory> wareHistories) {
        if (isReset()) {
            if (wareHistories != null) {
                mCursor.close();
            }
        }

        List<WareHistory> oldWaryHistories = mWareHistories;
        if (wareHistories == null || wareHistories.size() == 0) {
            Log.d(LOG_TAG, "+++++++++ No Data returned");
        }

        mWareHistories = wareHistories;
        if (isStarted()) {
            super.deliverResult(wareHistories);
        }
        if (oldWaryHistories != null && oldWaryHistories != wareHistories) {
            mCursor.close();
        }
    }


    @Override
    protected void onStartLoading() {
        if (mWareHistories != null) {
            deliverResult(mWareHistories);
        }

        if (takeContentChanged() | mWareHistories == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        onStopLoading();
        if (mCursor != null) {
            mCursor.close();
        }

        mWareHistories = null;
    }

    @Override
    public void onCanceled(List<WareHistory> wareHistories) {
        super.onCanceled(wareHistories);
        if (mCursor != null) {
            mCursor.close();
        }
    }

    @Override
    public void forceLoad() {
        super.forceLoad();
    }
}
