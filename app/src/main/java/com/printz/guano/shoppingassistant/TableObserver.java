package com.printz.guano.shoppingassistant;

import android.accounts.Account;
import android.content.ContentResolver;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.printz.guano.shoppingassistant.database.Contract;

public class TableObserver extends ContentObserver {

    private final static String LOG_TAG = TableObserver.class.getSimpleName();

    private final Account mAccount;

    public TableObserver(Handler handler, Account account) {
        super(handler);

        mAccount = account;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange, null);
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        Log.d(LOG_TAG, "Provider changed here: " + uri.toString());
        Bundle args = new Bundle();

        ContentResolver.requestSync(mAccount, Contract.CONTENT_AUTHORITY, args);
    }

    @Override
    public boolean deliverSelfNotifications() {
        return super.deliverSelfNotifications();
    }
}
