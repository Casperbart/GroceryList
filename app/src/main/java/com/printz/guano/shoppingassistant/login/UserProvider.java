package com.printz.guano.shoppingassistant.login;

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
import com.printz.guano.shoppingassistant.database.UserContract;

public class UserProvider extends ContentProvider {

    private static final String LOG_TAG = UserProvider.class.getSimpleName();

    private ListerDatabase mOpenHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static final int USERS = 100;
    private static final int USERS_ID = 101;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = UserContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, "users", USERS);
        matcher.addURI(authority, "users/*", USERS_ID);
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
        queryBuilder.setTables(ListerDatabase.Tables.USER);

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case USERS:
                // do nothing
                break;
            case USERS_ID:
                String id = UserContract.User.getUserId(uri);
                queryBuilder.appendWhere(UserContract.UserColumns.U_ID + "=" + id);
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
            case USERS:
                return UserContract.User.CONTENT_TYPE;
            case USERS_ID:
                return UserContract.User.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Uknown Uri" + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.v(LOG_TAG, "Inserting new user(uri=" + uri + ", values" + values.toString() + ")");
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(ListerDatabase.Tables.USER);

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case USERS:
                long recordId = db.insertOrThrow(ListerDatabase.Tables.USER, null, values);
                return UserContract.User.buildUserUri(String.valueOf(recordId));
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(LOG_TAG, "Attempt at deleting user(uri=" + uri + ")");
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.v(LOG_TAG, "update(uri=" + uri + ", values" + values.toString() + ")");
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        String selectionCriteria = selection;

        switch (match) {
            case USERS:
                // do nothing
                break;
            case USERS_ID:
                String id = UserContract.User.getUserId(uri);
                selectionCriteria = UserContract.UserColumns.U_ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }

        return db.update(ListerDatabase.Tables.USER, values, selectionCriteria, selectionArgs);
    }
}
