package com.printz.guano.shoppingassistant.login;

import android.content.AsyncTaskLoader;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.printz.guano.shoppingassistant.database.Contract;
import com.printz.guano.shoppingassistant.grocery_list.WareHistoryLoader;

import java.util.ArrayList;
import java.util.List;

class UserLoader extends AsyncTaskLoader<List<User>> {

    private final static String LOG_TAG = WareHistoryLoader.class.getSimpleName();
    private List<User> mUsers;
    private ContentResolver mContentResolver;
    private Cursor mCursor;
    public static final int LOADER_ID = 2;

    public UserLoader(Context context, ContentResolver contentResolver) {
        super(context);
        mContentResolver = contentResolver;
    }

    @Override
    public List<User> loadInBackground() {
        String[] projection = {
                Contract.UserColumns.U_ID,
                Contract.UserColumns.USERNAME,
        };

        List<User> entries = new ArrayList<>();

        mCursor = mContentResolver.query(Contract.User.CONTENT_URI, projection, null, null, null);

        if (mCursor != null) {
            if (mCursor.moveToFirst()) {
                do {
                    int id = mCursor.getInt(mCursor.getColumnIndex(Contract.UserColumns.U_ID));
                    String userName = mCursor.getString(mCursor.getColumnIndex(Contract.UserColumns.USERNAME));
                    User user = new User(id, userName);
                    entries.add(user);
                } while (mCursor.moveToNext());
            }

            mCursor.close();
        }

        return entries;
    }

    @Override
    public void deliverResult(List<User> users) {
        if (isReset() && users != null) {
            mCursor.close();
        }

        List<User> oldUsers = mUsers;
        if (users == null || users.size() == 0) {
            Log.d(LOG_TAG, "+++ No users returned +++");
        }

        mUsers = users;
        if (isStarted()) {
            super.deliverResult(users);
        }
        if (oldUsers != null && oldUsers != users) {
            mCursor.close();
        }
    }

    @Override
    protected void onStartLoading() {
        if (mUsers != null) {
            deliverResult(mUsers);
        }

        if (takeContentChanged() || mUsers == null) {
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

        mUsers = null;
    }

    @Override
    public void onCanceled(List<User> data) {
        super.onCanceled(data);
        if (mCursor != null) {
            mCursor.close();
        }
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
    }
}
