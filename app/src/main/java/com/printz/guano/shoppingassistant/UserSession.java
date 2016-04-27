package com.printz.guano.shoppingassistant;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.printz.guano.shoppingassistant.grocery_list.GroceryListActivity;

public class UserSession {

    public interface SessionChangedListener {
        void onSessionChanged();
    }

    private final static String LOG_TAB = UserSession.class.getSimpleName();

    /**
     * Key definitions of user credentials for shared preferences
     */
    private static final String USERNAME = "username";
    private static final String IS_SESSION_ACTIVE = "is_session_active";

    /**
     * Key definitions of database keys
     */
    private static final String USER_ID = "user_id";

    /**
     * Key to retrieve user credentials preferences
     */
    private static final String PREFERENCE = "preference";

    /**
     * SharedPreferences caches user related data
     */
    private static SharedPreferences sSharedPreferences;

    /**
     * Editor modifies SharedPreference values using keys
     */
    private static SharedPreferences.Editor sEditor;

    /**
     * Context used to work with shared preferences
     */
    private static Context mContext;

    private static UserSession sInstance;

    private UserSession() {
    }

    /**
     * Initializes the user session class with necessary objects
     *
     * @param context initializing class
     */
    public static void initializeUserSession(Context context) {
        mContext = context;
        sSharedPreferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        sEditor = sSharedPreferences.edit();
    }

    public static UserSession getUserSession() {
        if (sInstance != null) {
            return sInstance;
        }
        sInstance = new UserSession();
        return sInstance;
    }

    /**
     * Saves username & password in SharedPreferences
     *
     * @param username String username
     * @param userId int database userId
     */
    public void createSession(String userId, String username) {
        sEditor.putBoolean(IS_SESSION_ACTIVE, true);
        sEditor.putString(USERNAME, username);
        sEditor.putString(USER_ID, userId);
        sEditor.commit();

        triggerOnSessionChange();
    }

    private void triggerOnSessionChange() {
        Log.d(LOG_TAB, "Session changed");
        if (mContext instanceof GroceryListActivity) {
            ((GroceryListActivity) mContext).onSessionChanged();
        }
    }

    /**
     * Clears values in SharedPreferences
     */
    public void clearSession() {
        sEditor.clear();
        sEditor.commit();

        triggerOnSessionChange();
    }

    public String getUserName() {
        return sSharedPreferences.getString(USERNAME, null);
    }

    /**
     * Get user database primary key id
     *
     * @return User primary key
     */
    public String getUserId() {
        return sSharedPreferences.getString(USER_ID, null);
    }

    /**
     * Check whether or not a session is active
     *
     * @return Returns true if user a session is active, false if not
     */
    public boolean isSessionActive() {
        return sSharedPreferences.getBoolean(IS_SESSION_ACTIVE, false);
    }
}
