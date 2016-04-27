package com.printz.guano.shoppingassistant.account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.util.Log;

public class SyncAccount {

    private static final String LOG_TAG = SyncAccount.class.getSimpleName();

    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "grocerygo.dev";
    // The account name
    public static final String ACCOUNT = "dummyaccount";

    private static Account mAcount;

    public static void CreateSyncAccount(Context context) {
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
    }

    public static Account getAccount() {
        return mAcount;
    }
}
