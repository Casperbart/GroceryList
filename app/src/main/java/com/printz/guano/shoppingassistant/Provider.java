package com.printz.guano.shoppingassistant;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.printz.guano.shoppingassistant.database.Contract;
import com.printz.guano.shoppingassistant.database.ListerDatabase;

public class Provider extends ContentProvider {

    private static final String LOG_TAG = Provider.class.getSimpleName();

    private ListerDatabase mOpenHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static final int WARES = 100;
    private static final int WARES_ID = 101;
    private static final int WARE_HISTORIES = 102;
    private static final int WARE_HISTORIES_ID = 103;
    private static final int USERS = 104;
    private static final int USERS_ID = 105;
    private static final int INVITATIONS = 106;
    private static final int INVITATIONS_ID = 107;


    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = Contract.CONTENT_AUTHORITY;
        matcher.addURI(authority, "wares", WARES);
        matcher.addURI(authority, "wares/*", WARES_ID);
        matcher.addURI(authority, "ware_histories", WARE_HISTORIES);
        matcher.addURI(authority, "ware_histories/*", WARE_HISTORIES_ID);
        matcher.addURI(authority, "users", USERS);
        matcher.addURI(authority, "users/*", USERS_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new ListerDatabase(getContext());
        return true;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case WARES:
                return Contract.Ware.CONTENT_TYPE;
            case WARES_ID:
                return Contract.Ware.CONTENT_ITEM_TYPE;
            case USERS:
                return Contract.User.CONTENT_TYPE;
            case USERS_ID:
                return Contract.User.CONTENT_ITEM_TYPE;
            case WARE_HISTORIES:
                return Contract.WareHistory.CONTENT_TYPE;
            case WARE_HISTORIES_ID:
                return Contract.WareHistory.CONTENT_ITEM_TYPE;
            case INVITATIONS:
                return Contract.Invitation.CONTENT_TYPE;
            case INVITATIONS_ID:
                return Contract.Invitation.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown Uri " + uri);
        }
    }

    private String getQueryTable(int match) {
        switch (match) {
            case WARES:
            case WARES_ID:
                return ListerDatabase.Tables.WARES;
            case WARE_HISTORIES:
            case WARE_HISTORIES_ID:
                return ListerDatabase.Tables.WARE_HISTORIES;
            case USERS:
            case USERS_ID:
                return ListerDatabase.Tables.USERS;
            default:
                return "";
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        final int match = sUriMatcher.match(uri);
        final String table = getQueryTable(match);
        String rowId;

        queryBuilder.setTables(table);

        switch (match) {
            case WARES:
                if(TextUtils.isEmpty(sortOrder)) {
                    sortOrder = Contract.Ware.DEFAULT_SORT_ORDER;
                }
                break;
            case WARES_ID:
                rowId = Contract.Ware.getWareId(uri);
                queryBuilder.appendWhere(Contract.WareColumns.WARE_NAME + "=" + rowId);
                break;
            case WARE_HISTORIES:
                // do nothing
                break;
            case WARE_HISTORIES_ID:
                rowId = Contract.WareHistory.getWareHistoryId(uri);
                queryBuilder.appendWhere(Contract.WareHistoryColumns.WARE_HISTORY_NAME + "=" + rowId);
                break;
            case USERS:
                // do nothing
                break;
            case USERS_ID:
                rowId = Contract.User.getUserId(uri);
                queryBuilder.appendWhere(Contract.UserColumns.U_ID + "=" + rowId);
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri " + uri);
        }

        return queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        Log.d(LOG_TAG, "Inserting with uri " + uri + " with values " + values.toString());
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        long rowId;
        Uri contentUri;

        switch (match) {
            case WARES:
                values.put(Contract.CommonColumns.SYNC_METHOD, "POST");
                values.put(Contract.CommonColumns.SYNC_STATUS, "PENDING");
                rowId = db.insertOrThrow(ListerDatabase.Tables.WARES, null, values);
                contentUri = Contract.Ware.buildWareUri(String.valueOf(rowId));
                break;
            case WARE_HISTORIES:
                rowId = db.insertOrThrow(ListerDatabase.Tables.WARE_HISTORIES, null, values);
                contentUri = Contract.WareHistory.buildWareHistoryUri(String.valueOf(rowId));
                break;
            case USERS:
                rowId = db.insertOrThrow(ListerDatabase.Tables.USERS, null, values);
                contentUri = Contract.User.buildUserUri(String.valueOf(rowId));
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri " + uri);
        }

        getContext().getContentResolver().notifyChange(contentUri, null);

        return contentUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        Log.d(LOG_TAG, "Deleting with Uri " + uri);
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        String rowId;
        String table = getQueryTable(match);
        String selectionCriteria;

        switch (match) {
            case WARES_ID:
                rowId = Contract.Ware.getWareId(uri);
                selectionCriteria = Contract.WareColumns.W_ID + "=" + rowId +
                        (!TextUtils.isEmpty(selection) ? " AND " + "(" + selection + ")" : "");
                break;
            case WARE_HISTORIES_ID:
                rowId = Contract.WareHistory.getWareHistoryId(uri);
                selectionCriteria = Contract.WareHistoryColumns.H_ID + "=" + rowId +
                        (!TextUtils.isEmpty(selection) ? " AND " + "(" + selection + ")" : "");
                break;
            case USERS_ID:
                rowId = Contract.User.getUserId(uri);
                selectionCriteria = Contract.UserColumns.U_ID + "=" + rowId
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri " + uri);
        }

        int deleteCount = db.delete(table, selectionCriteria, selectionArgs);

        if(deleteCount > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return deleteCount;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.d(LOG_TAG, "Updating with Uri " + uri + " with values " + values.toString());
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        String rowId;
        String table = getQueryTable(match);
        String selectionCriteria;

        switch (match) {
            case WARES_ID:
                values.put(Contract.CommonColumns.SYNC_METHOD, "PUT");
                values.put(Contract.CommonColumns.SYNC_STATUS, "PENDING");
                rowId = Contract.Ware.getWareId(uri);
                selectionCriteria = Contract.WareColumns.W_ID + "=" + rowId +
                        (!TextUtils.isEmpty(selection) ? " AND " + "(" + selection + ")" : "");
                break;
            case WARE_HISTORIES_ID:
                rowId = Contract.WareHistory.getWareHistoryId(uri);
                selectionCriteria = Contract.WareHistoryColumns.H_ID + "=" + rowId
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
                break;
            case USERS_ID:
                rowId = Contract.User.getUserId(uri);
                selectionCriteria = Contract.UserColumns.U_ID + "=" + rowId
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri " + uri + " with values " + values.toString());
        }

        int updateCount = db.update(table, values, selectionCriteria, selectionArgs);

        if(updateCount > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return updateCount;
    }
}
