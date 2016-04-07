package com.printz.guano.shoppingassistant.login;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.content.AsyncTaskLoader;
import android.util.Log;

import com.printz.guano.shoppingassistant.edit_list.WareHistoryLoader;
import com.printz.guano.shoppingassistant.database.UserContract;

import java.util.ArrayList;
import java.util.List;

public class UserLoader extends AsyncTaskLoader<List<User>> {

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
                UserContract.UserColumns.U_ID,
                UserContract.UserColumns.USERNAME,
                UserContract.UserColumns.PASSWORD
        };

        List<User> entries = new ArrayList<>();

        mCursor = mContentResolver.query(UserContract.TABLE_URI, projection, null, null, null);

        if (mCursor != null) {
            if (mCursor.moveToFirst()) {
                do {
                    int id = mCursor.getInt(mCursor.getColumnIndex(UserContract.UserColumns.U_ID));
                    String userName = mCursor.getString(mCursor.getColumnIndex(UserContract.UserColumns.USERNAME));
                    String password = mCursor.getString(mCursor.getColumnIndex(UserContract.UserColumns.PASSWORD));
                    User user = new User(id, userName, password);
                    entries.add(user);
                } while (mCursor.moveToNext());
            }
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
