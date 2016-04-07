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
import com.printz.guano.shoppingassistant.database.WareContract;

public class WareProvider extends ContentProvider {

    private static final String LOG_TAG = WareProvider.class.getSimpleName();
    private ListerDatabase mWaresDatabase;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final int WARES = 100;
    private static final int WARES_ID = 101;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = WareContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, "wares", WARES);
        matcher.addURI(authority, "wares/*", WARES_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mWaresDatabase = new ListerDatabase(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case WARES:
                return WareContract.Ware.CONTENT_TYPE;
            case WARES_ID:
                return WareContract.Ware.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown Uri " + uri);
        }

    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mWaresDatabase.getReadableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(ListerDatabase.Tables.WARE);
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case WARES:
                break;
            case WARES_ID:
                String id = WareContract.Ware.getWareId(uri);
                queryBuilder.appendWhere(WareContract.WareColumns.WARE_NAME + "=" + id);
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri " + uri);
        }
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d(LOG_TAG, "Inserting with uri " + uri + " with values " + values.toString());
        SQLiteDatabase db = mWaresDatabase.getWritableDatabase();
        int match = sUriMatcher.match(uri);

        switch (match) {
            case WARES:
                long rowId = db.insertOrThrow(ListerDatabase.Tables.WARE, null, values);
                return WareContract.Ware.buildWareUri(String.valueOf(rowId));
            default:
                throw new IllegalArgumentException("Unknown Uri " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(LOG_TAG, "Deleting with Uri " + uri);

        if (uri.equals(WareContract.TABLE_URI)) {
            ListerDatabase.deleteDatabase(getContext());
            return 0;
        }

        SQLiteDatabase db = mWaresDatabase.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        switch (match) {
            case WARES_ID:
                String _id = WareContract.Ware.getWareId(uri);
                String selectionCriteria = WareContract.WareColumns.W_ID + "=" + _id +
                        (!TextUtils.isEmpty(selection) ? " AND " + "(" + selection + ")" : "");
                return db.delete(ListerDatabase.Tables.WARE, selectionCriteria, selectionArgs);
            default:
                throw new IllegalArgumentException("Unknown Uri " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.d(LOG_TAG, "Updating with Uri " + uri + " with values " + values.toString());
        SQLiteDatabase db = mWaresDatabase.getWritableDatabase();
        int match = sUriMatcher.match(uri);

        String selectionCriteria = selection;
        switch (match) {
            case WARES:
                // do noting
                break;
            case WARES_ID:
                String _id = WareContract.Ware.getWareId(uri);
                selectionCriteria = WareContract.WareColumns.W_ID + "=" + _id +
                        (!TextUtils.isEmpty(selection) ? " AND " + "(" + selection + ")" : "");
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri " + uri + " with values " + values.toString());
        }
        return db.update(ListerDatabase.Tables.WARE, values, selectionCriteria, selectionArgs);
    }

    private void deleteDatabase() {
        mWaresDatabase.close();
        ListerDatabase.deleteDatabase(getContext());
        mWaresDatabase = new ListerDatabase(getContext());
    }
}
