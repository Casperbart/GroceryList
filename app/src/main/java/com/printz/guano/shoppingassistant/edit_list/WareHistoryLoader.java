package com.printz.guano.shoppingassistant.edit_list;

import android.content.AsyncTaskLoader;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.printz.guano.shoppingassistant.UserSession;
import com.printz.guano.shoppingassistant.database.WareHistoryContract;

import java.util.ArrayList;
import java.util.List;

public class WareHistoryLoader extends AsyncTaskLoader<List<WareHistory>> {

    private final static String LOG_TAG = WareHistoryLoader.class.getSimpleName();
    public static final int LOADER_ID = 1;

    private List<WareHistory> mWareHistories;
    private ContentResolver mContentResolver;
    private Cursor mCursor;

    public WareHistoryLoader(Context context) {
        super(context);
        mContentResolver = context.getContentResolver();
    }

    @Override
    public List<WareHistory> loadInBackground() {
//        android.os.Debug.waitForDebugger();
        List<WareHistory> entries = new ArrayList<>();

        String[] projection = {
                WareHistoryContract.WareHistoryColumns.H_ID,
                WareHistoryContract.WareHistoryColumns.WARE_HISTORY_NAME,
                WareHistoryContract.WareHistoryColumns.WARE_HISTORY_COUNT
        };

        String selection = WareHistoryContract.WareHistoryColumns.USER_ID + "=?";

        String userId = "1"; // default user

        UserSession userSession = UserSession.getUserSession();
        if (userSession.isSessionActive()) {
            userId = userSession.getUserId();
        }

        String[] selectionArgs = {
                userId
        };

        mCursor = mContentResolver.query(WareHistoryContract.TABLE_URI, projection, selection, selectionArgs, null);

        if (mCursor != null) {
            if (mCursor.moveToFirst()) {
                do {
                    int id = mCursor.getInt(mCursor.getColumnIndex(WareHistoryContract.WareHistoryColumns.H_ID));
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

        List<WareHistory> oldWareHistories = mWareHistories;
        if (wareHistories == null || wareHistories.size() == 0) {
            Log.d(LOG_TAG, "+++ No histories returned +++");
        }

        mWareHistories = wareHistories;
        if (isStarted()) {
            super.deliverResult(wareHistories);
        }
        if (oldWareHistories != null && oldWareHistories != wareHistories) {
            mCursor.close();
        }
    }


    @Override
    protected void onStartLoading() {
        if (mWareHistories != null) {
            deliverResult(mWareHistories);
        }

        if (takeContentChanged() || mWareHistories == null) {
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
