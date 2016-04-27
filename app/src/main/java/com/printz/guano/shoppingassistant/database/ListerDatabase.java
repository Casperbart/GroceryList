package com.printz.guano.shoppingassistant.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ListerDatabase extends SQLiteOpenHelper {

    private static final String LOG_TAG = ListerDatabase.class.getSimpleName();

    /**
     * Database file used to store data
     */
    private static final String DATABASE_NAME = "lister.db";
    private static final int DATABASE_VERSION = 1;

    public interface Tables {
        String USERS = "users";
        String INVITATIONS = "invitations";
        String WARES = "wares";
        String WARE_HISTORIES = "ware_histories";
    }

    public ListerDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + Tables.USERS + " (" +
                Contract.UserColumns.U_ID + " INTEGER PRIMARY KEY, " +
                Contract.UserColumns.USERNAME + " TEXT NOT NULL UNIQUE)");

        db.execSQL("CREATE TABLE " + Tables.INVITATIONS + " (" +
                Contract.InvitationColumns.I_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Contract.InvitationColumns.RECEIVER_USERNAME + " TEXT NOT NULL, " +
                Contract.InvitationColumns.SENDER_GROUP_ID + " INTEGER NOT NULL, " +
                Contract.InvitationColumns.STATUS + " TEXT NOT NULL, " +
                Contract.CommonColumns.SYNC_METHOD + " TEXT NOT NULL, " +
                Contract.CommonColumns.SYNC_STATUS + " TEXT NOT NULL)");

        db.execSQL("CREATE TABLE " + Tables.WARES + " (" +
                Contract.WareColumns.W_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Contract.WareColumns.WARE_NAME + " TEXT NOT NULL, " +
                Contract.WareColumns.WARE_POSITION + " INTEGER NOT NULL, " +
                Contract.WareColumns.WARE_IS_MARKED + " BOOLEAN NOT NULL, " +
                Contract.WareColumns.WARE_AMOUNT + " TEXT NOT NULL, " +
                Contract.WareColumns.WARE_QUANTITY_TYPE + " TEXT NOT NULL, " +
                Contract.WareColumns.USER_ID + " INTEGER, " +
                Contract.CommonColumns.SYNC_METHOD + " TEXT NOT NULL, " +
                Contract.CommonColumns.SYNC_STATUS + " TEXT NOT NULL)");

        db.execSQL("CREATE TABLE " + Tables.WARE_HISTORIES + " (" +
                Contract.WareHistoryColumns.H_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Contract.WareHistoryColumns.WARE_HISTORY_NAME + " TEXT NOT NULL, " +
                Contract.WareHistoryColumns.WARE_HISTORY_COUNT + " INTEGER NOT NULL, " +
                Contract.WareHistoryColumns.USER_ID + " INTEGER, " +
                "FOREIGN KEY(" + Contract.WareHistoryColumns.USER_ID + ") REFERENCES " +
                Tables.USERS + "(" + Contract.UserColumns.U_ID + "))");

        insertDefaultUser(db);
    }

    /**
     * Inserts the default user
     *
     * @param db database
     */
    private void insertDefaultUser(SQLiteDatabase db) {
        ContentValues userValues = new ContentValues();
        userValues.put(Contract.UserColumns.U_ID, "-1");
        userValues.put(Contract.UserColumns.USERNAME, "default");
        db.insertOrThrow(Tables.USERS, null, userValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        int version = oldVersion;
        if (version == 1) {
            version = 2;
        }

        if (version != DATABASE_VERSION) {
            db.execSQL("DROP TABLE IF EXISTS " + Tables.USERS);
            db.execSQL("DROP TABLE IF EXISTS " + Tables.WARES);
            db.execSQL("DROP TABLE IF EXISTS " + Tables.WARE_HISTORIES);
            onCreate(db);
        }
    }

    public static void deleteDatabase(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }
}
