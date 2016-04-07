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
    private final Context mContext;

    private static final String DEFAULT_USER_ID = "1";

    public interface Tables {
        String USER = "user";
        String SHOPPING_LIST = "shopping_list";
        String WARE = "ware";
        String WARE_HISTORY = "ware_histories";
    }

    public ListerDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + Tables.USER + " (" +
                UserContract.UserColumns.U_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                UserContract.UserColumns.USERNAME + " TEXT NOT NULL UNIQUE, " +
                UserContract.UserColumns.PASSWORD + " TEXT NOT NULL)");

        db.execSQL("CREATE TABLE " + Tables.SHOPPING_LIST + " (" +
                ShoppingListContract.ShoppingListColumns.S_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ShoppingListContract.ShoppingListColumns.USER_ID + " INTEGER, " +
                "FOREIGN KEY(" + ShoppingListContract.ShoppingListColumns.USER_ID + ") REFERENCES " +
                Tables.USER + "(" + UserContract.UserColumns.U_ID + "))");

        db.execSQL("CREATE TABLE " + Tables.WARE + " (" +
                WareContract.WareColumns.W_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                WareContract.WareColumns.WARE_NAME + " TEXT NOT NULL, " +
                WareContract.WareColumns.WARE_POSITION + " TEXT NOT NULL, " +
                WareContract.WareColumns.WARE_IS_MARKED + " BOOLEAN NOT NULL, " +
                WareContract.WareColumns.WARE_QUANTITY_TYPE + " TEXT NOT NULL, " +
                WareContract.WareColumns.WARE_AMOUNT + " TEXT NOT NULL, " +
                WareContract.WareColumns.SHOPPING_LIST_ID + " INTEGER, " +
                "FOREIGN KEY(" + WareContract.WareColumns.SHOPPING_LIST_ID + ") REFERENCES " +
                Tables.SHOPPING_LIST + "(" + ShoppingListContract.ShoppingListColumns.S_ID + "))");

        db.execSQL("CREATE TABLE " + Tables.WARE_HISTORY + " (" +
                WareHistoryContract.WareHistoryColumns.H_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                WareHistoryContract.WareHistoryColumns.WARE_HISTORY_NAME + " TEXT NOT NULL, " +
                WareHistoryContract.WareHistoryColumns.WARE_HISTORY_COUNT + " INTEGER NOT NULL, " +
                WareHistoryContract.WareHistoryColumns.USER_ID + " INTEGER, " +
                "FOREIGN KEY(" + WareHistoryContract.WareHistoryColumns.USER_ID + ") REFERENCES " +
                Tables.USER + "(" + UserContract.UserColumns.U_ID + "))");

        insertDefaultValues(db);
    }

    /**
     * Inserts the default user and default shopping list
     *
     * @param db database
     */
    private void insertDefaultValues(SQLiteDatabase db) {
        insertDefaultUser(db);
        insertDefaultShoppingList(db);
    }

    private void insertDefaultUser(SQLiteDatabase db) {
        ContentValues defaultUser = new ContentValues();
        defaultUser.put(UserContract.UserColumns.USERNAME, "default");
        defaultUser.put(UserContract.UserColumns.PASSWORD, "default");
        db.insertOrThrow(Tables.USER, null, defaultUser);
    }

    private void insertDefaultShoppingList(SQLiteDatabase db) {
        ContentValues defaultShoppingList = new ContentValues();
        defaultShoppingList.put(ShoppingListContract.ShoppingListColumns.USER_ID, DEFAULT_USER_ID);
        db.insertOrThrow(Tables.SHOPPING_LIST, null, defaultShoppingList);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        int version = oldVersion;
        if (version == 1) {
            version = 2;
        }

        if (version != DATABASE_VERSION) {
            db.execSQL("DROP TABLE IF EXISTS " + Tables.USER);
            db.execSQL("DROP TABLE IF EXISTS " + Tables.SHOPPING_LIST);
            db.execSQL("DROP TABLE IF EXISTS " + Tables.WARE);
            db.execSQL("DROP TABLE IF EXISTS " + Tables.WARE_HISTORY);
            onCreate(db);
        }
    }

    public static void deleteDatabase(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }
}
