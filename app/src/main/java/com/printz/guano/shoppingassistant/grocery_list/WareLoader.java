package com.printz.guano.shoppingassistant.grocery_list;

import android.content.AsyncTaskLoader;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.printz.guano.shoppingassistant.UserSession;
import com.printz.guano.shoppingassistant.database.Contract;

import java.util.ArrayList;
import java.util.List;

class WareLoader extends AsyncTaskLoader<List<Ware>> {

    private static final String LOG_TAG = WareLoader.class.getSimpleName();

    public static final int LOADER_ID = 3;

    private ContentResolver mContentResolver;
    private List<Ware> mWares;
    private Cursor mCursor;

    public WareLoader(Context context) {
        super(context);
        mContentResolver = context.getContentResolver();
    }

    @Override
    public List<Ware> loadInBackground() {
//        android.os.Debug.waitForDebugger();
        List<Ware> entries = new ArrayList<>();

        String[] projection = {
                Contract.WareColumns.W_ID,
                Contract.WareColumns.WARE_NAME,
                Contract.WareColumns.WARE_POSITION,
                Contract.WareColumns.WARE_IS_MARKED,
                Contract.WareColumns.WARE_QUANTITY_TYPE,
                Contract.WareColumns.WARE_AMOUNT
        };

        String selection = Contract.WareColumns.USER_ID + "=?";

        String userId = "1"; // default user

        UserSession userSession = UserSession.getUserSession();
        if (userSession.isSessionActive()) {
            userId = userSession.getUserId();
        }

        // ShoppingList id's match the user id
        String[] selectionArgs = {
                userId
        };

        mCursor = mContentResolver.query(Contract.Ware.CONTENT_URI, projection, selection, selectionArgs, null);

        if (mCursor != null) {
            if (mCursor.moveToFirst()) {
                do {
                    int id = mCursor.getInt(mCursor.getColumnIndex(Contract.WareColumns.W_ID));
                    String name = mCursor.getString(mCursor.getColumnIndex(Contract.WareColumns.WARE_NAME));
                    int position = mCursor.getInt(mCursor.getColumnIndex(Contract.WareColumns.WARE_POSITION));
                    boolean isMarked = mCursor.getInt(mCursor.getColumnIndex(Contract.WareColumns.WARE_IS_MARKED)) != 0;
                    String type = mCursor.getString(mCursor.getColumnIndex(Contract.WareColumns.WARE_QUANTITY_TYPE));
                    String amount = mCursor.getString(mCursor.getColumnIndex(Contract.WareColumns.WARE_AMOUNT));

                    Ware ware = new Ware(id, name, position, isMarked, type, amount);
                    entries.add(ware);
                } while (mCursor.moveToNext());
            }

            mCursor.close();
        }

        return entries;
    }


    @Override
    public void deliverResult(List<Ware> wares) {
        if (isReset()) {
            if (wares != null) {
                mCursor.close();
            }
        }

        List<Ware> oldWares = mWares;
        if (wares == null || wares.size() == 0) {
            Log.d(LOG_TAG, "+++ No wares returned +++");
        }

        mWares = wares;
        if (isStarted()) {
            super.deliverResult(wares);
        }
        if (oldWares != null && oldWares != wares) {
            mCursor.close();
        }
    }


    @Override
    protected void onStartLoading() {
        if (mWares != null) {
            deliverResult(mWares);
        }

        if (takeContentChanged() || mWares == null) {
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

        mWares = null;
    }

    @Override
    public void onCanceled(List<Ware> wares) {
        super.onCanceled(wares);
        if (mCursor != null) {
            mCursor.close();
        }
    }

    @Override
    public void forceLoad() {
        super.forceLoad();
    }
}
