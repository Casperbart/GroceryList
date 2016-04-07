package com.printz.guano.shoppingassistant;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.printz.guano.shoppingassistant.edit_list.ShoppingListActivity;

import java.util.HashMap;
import java.util.Map;

public class UserSession {

    public interface SessionChangedListener {
        void onSessionChanged();
    }

    private final static String LOG_TAB = UserSession.class.getSimpleName();

    /**
     * Key definitions of user credentials for shared preferences
     */
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    private static final String IS_SESSION_ACTIVE = "is_session_active";

    /**
     * Key definitions of database keys
     */
    public static final String USER_ID = "user_id";

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

    private static UserSession instance;

    private UserSession() {
    }

    /**
     * Initializes the user session class with necessary objects
     *
     * @param context initializing class
     */
    public static void initializeUserSession(Context context) {
        mContext = context;
        sSharedPreferences = context.getSharedPreferences(PREFERENCE, context.MODE_PRIVATE);
        sEditor = sSharedPreferences.edit();
    }

    public static UserSession getUserSession() {
        if (instance != null) {
            return instance;
        }
        instance = new UserSession();
        return instance;
    }

    /**
     * Saves username & password in SharedPreferences
     *
     * @param username String username
     * @param password String password
     */
    public void createSession(String username, String password) {
        // session is now active
        sEditor.putBoolean(IS_SESSION_ACTIVE, true);

        sEditor.putString(USERNAME, username);
        sEditor.putString(PASSWORD, password);

        // commit changes to shared preferences
        sEditor.commit();

        triggerOnSessionChange();
    }

    public void setDatabaseIds(String userId) {
        sEditor.putString(USER_ID, userId);

        sEditor.commit();
    }

    private void triggerOnSessionChange() {
        Log.d(LOG_TAB, "Session changed");
        if (mContext instanceof ShoppingListActivity) {
            ((ShoppingListActivity) mContext).onSessionChanged();
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

    /**
     * This returns username & password in a map
     *
     * @return user credentials map
     */
    public Map<String, String> getCredentials() {

        Map<String, String> userCredentials = new HashMap<>();

        String username = sSharedPreferences.getString(USERNAME, null);
        String password = sSharedPreferences.getString(PASSWORD, null);

        userCredentials.put(USERNAME, username);
        userCredentials.put(PASSWORD, password);

        return userCredentials;
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
