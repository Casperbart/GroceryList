package com.printz.guano.shoppingassistant;

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
import com.printz.guano.shoppingassistant.database.ShoppingListContract;

public class ShoppingListProvider extends ContentProvider {

    private final static String LOG_TAG = ShoppingListProvider.class.getSimpleName();

    private ListerDatabase mOpenHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static final int SHOPPING_LISTS = 100;
    private static final int SHOPPING_LISTS_ID = 101;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = ShoppingListContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, "shopping_lists", SHOPPING_LISTS);
        matcher.addURI(authority, "shopping_lists/*", SHOPPING_LISTS_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new ListerDatabase(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(ListerDatabase.Tables.SHOPPING_LIST);

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case SHOPPING_LISTS_ID:
                String id = ShoppingListContract.ShoppingList.getWareId(uri);
                queryBuilder.appendWhere(ShoppingListContract.ShoppingListColumns.S_ID + "=" + id);
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
            case SHOPPING_LISTS:
                return ShoppingListContract.ShoppingList.CONTENT_TYPE;
            case SHOPPING_LISTS_ID:
                return ShoppingListContract.ShoppingList.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown Uri" + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.v(LOG_TAG, "Inserting new shopping list(uri=" + uri + ", values" + values.toString() + ")");
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(ListerDatabase.Tables.SHOPPING_LIST);

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case SHOPPING_LISTS:
                long recordId = db.insertOrThrow(ListerDatabase.Tables.SHOPPING_LIST, null, values);
                return ShoppingListContract.ShoppingList.buildWareUri(String.valueOf(recordId));
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(LOG_TAG, "Attempt at deleting shopping list(uri=" + uri + ")");
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.v(LOG_TAG, "update shopping list(uri=" + uri + ", values" + values.toString() + ")");
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        String selectionCriteria = selection;

        switch (match) {
            case SHOPPING_LISTS:
                // do nothing
                break;
            case SHOPPING_LISTS_ID:
                String id = ShoppingListContract.ShoppingList.getWareId(uri);
                selectionCriteria = ShoppingListContract.ShoppingListColumns.S_ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }

        return db.update(ListerDatabase.Tables.SHOPPING_LIST, values, selectionCriteria, selectionArgs);
    }
}
