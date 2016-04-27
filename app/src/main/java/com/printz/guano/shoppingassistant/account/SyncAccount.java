package com.printz.guano.shoppingassistant.account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.printz.guano.shoppingassistant.database.Contract;

public class SyncAccount {

    private static final String LOG_TAG = SyncAccount.class.getSimpleName();

    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "grocerygo.dev";
    // The account name
    public static final String ACCOUNT = "dummyaccount";

    private static Account mAcount;

    public static Account createSyncAccount(Context context) {
        // Create the account type and default account
        mAcount = new Account(ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (!accountManager.addAccountExplicitly(mAcount, null, null)) {
            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */
            Log.d(LOG_TAG, "Account already exists");
        }

        return mAcount;
    }

    /**
     * Helper method to trigger an immediate sync ("refresh").
     * <p/>
     * <p>This should only be used when we need to preempt the normal sync schedule. Typically, this
     * means the user has pressed the "refresh" button.
     * <p/>
     * Note that SYNC_EXTRAS_MANUAL will cause an immediate sync, without any optimization to
     * preserve battery life. If you know new data is available (perhaps via a GCM notification),
     * but the user is not actively waiting for that data, you should omit this flag; this will give
     * the OS additional freedom in scheduling your sync request.
     */
    public static void TriggerRefresh() {
        Bundle bundle = new Bundle();
        // Disable sync backoff and ignore sync preferences. In other words...perform sync NOW!
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(mAcount, Contract.CONTENT_AUTHORITY, bundle);
    }

    public static Account getAccount() {
        return mAcount;
    }
}
