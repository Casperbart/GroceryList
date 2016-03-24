package com.printz.guano.shoppingassistant;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class ListerDatabase extends SQLiteOpenHelper {

    private static final String LOG_TAG = ListerDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = "lister.db";
    private static final int DATABASE_VERSION = 1;
    private final Context mContext;

    public interface Tables {
        String WARES = "wares";
        String WARE_HISTORY = "ware_histories";
    }

    public ListerDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Tables.WARES + " (" +
                BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                WaresContract.WaresColumns.WARE_NAME + " TEXT NOT NULL," +
                WaresContract.WaresColumns.WARE_AMOUNT + " TEXT NOT NULL)");

        db.execSQL("CREATE TABLE " + Tables.WARE_HISTORY + " (" +
                BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                WareHistoryContract.WareHistoryColumns.WARE_HISTORY_NAME + " TEXT NOT NULL," +
                WareHistoryContract.WareHistoryColumns.WARE_HISTORY_COUNT + " INTEGER NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        int version = oldVersion;
        if (version == 1) {
            version = 2;
        }

        if (version != DATABASE_VERSION) {
            db.execSQL("DROP TABLE IF EXISTS " + Tables.WARES);
            db.execSQL("DROP TABLE IF EXISTS " + Tables.WARE_HISTORY);
            onCreate(db);
        }
    }

    public static void deleteDatabase(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }
}
