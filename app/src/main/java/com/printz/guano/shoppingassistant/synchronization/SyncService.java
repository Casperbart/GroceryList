package com.printz.guano.shoppingassistant.synchronization;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class SyncService extends Service {

    private static final String LOG_TAG = SyncService.class.getSimpleName();

    private static SyncAdapter sSyncAdapter;

    // Object ot use as a thread-safe lock
    private static final Object sSyncAdapterLock = new Object();

    @Override
    public void onCreate() {
        synchronized (sSyncAdapterLock) {
            if(sSyncAdapter == null) {
                sSyncAdapter = new SyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }
}
