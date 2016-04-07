package com.printz.guano.shoppingassistant.edit_list;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.printz.guano.shoppingassistant.database.ListerDatabase;
import com.printz.guano.shoppingassistant.database.WareHistoryContract;

public class WareHistoryProvider extends ContentProvider {

    private final static String LOG_TAG = WareHistoryProvider.class.getSimpleName();

    private ListerDatabase mOpenHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static final int WARE_HISTORIES = 100;
    private static final int WARE_HISTORIES_ID = 101;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = WareHistoryContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, "ware_histories", WARE_HISTORIES);
        matcher.addURI(authority, "ware_histories/*", WARE_HISTORIES_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new ListerDatabase(getContext());
        return true;
    }

    private void deleteDatabase() {
        mOpenHelper.close();
        ListerDatabase.deleteDatabase(getContext());
        mOpenHelper = new ListerDatabase(getContext());
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(ListerDatabase.Tables.WARE_HISTORY);

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case WARE_HISTORIES:
                // do nothing
                break;
            case WARE_HISTORIES_ID:
                String id = WareHistoryContract.WareHistory.getWareHistoryId(uri);
                queryBuilder.appendWhere(WareHistoryContract.WareHistoryColumns.WARE_HISTORY_NAME + "=" + id);
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }

        return queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case WARE_HISTORIES:
                return WareHistoryContract.WareHistory.CONTENT_TYPE;
            case WARE_HISTORIES_ID:
                return WareHistoryContract.WareHistory.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown Uri" + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.v(LOG_TAG, "Inserting new ware_history(uri=" + uri + ", values" + values.toString() + ")");
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(ListerDatabase.Tables.WARE_HISTORY);

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case WARE_HISTORIES:
                long recordId = db.insertOrThrow(ListerDatabase.Tables.WARE_HISTORY, null, values);
                return WareHistoryContract.WareHistory.buildWareHistoryUri(String.valueOf(recordId));
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(LOG_TAG, "Attempt at deleting ware_history(uri=" + uri + ")");
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.v(LOG_TAG, "update shopping list(uri=" + uri + ", values" + values.toString() + ")");
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        String selectionCriteria = selection;

        switch (match) {
            case WARE_HISTORIES:
                // do nothing
                break;
            case WARE_HISTORIES_ID:
                String id = WareHistoryContract.WareHistory.getWareHistoryId(uri);
                selectionCriteria = WareHistoryContract.WareHistoryColumns.H_ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }

        return db.update(ListerDatabase.Tables.WARE_HISTORY, values, selectionCriteria, selectionArgs);
    }
}
