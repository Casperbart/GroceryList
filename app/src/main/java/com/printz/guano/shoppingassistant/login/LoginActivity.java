package com.printz.guano.shoppingassistant.login;

import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.Loader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.printz.guano.shoppingassistant.BaseActivity;
import com.printz.guano.shoppingassistant.R;
import com.printz.guano.shoppingassistant.UserSession;
import com.printz.guano.shoppingassistant.database.DatabaseLib;

import java.util.List;

/**
 * A login screen that offers login via username/password.
 */
public class LoginActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<List<User>> {

    private static final String LOG_TAG = LoginActivity.class.getSimpleName();

    private DatabaseLib mLocalDbLib;

    /**
     * A authentication store containing known user names and passwords.
     */
    private List<User> mUsers;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    private ContentResolver mContentResolver;

    // UI references.
    private EditText mUsernameView;
    private EditText mPasswordView;

    /**
     * Credential sanitation variables
     */
    private final int MIN_PASSWORD_LENGTH = 5;
    private final int MIN_USERNAME_LENGTH = 5;
    private final int MAX_PASSWORD_LENGTH = 100;
    private final int MAX_USERNAME_LENGTH = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        activateToolbarWithHomeEnabled();

        mContentResolver = getContentResolver();

        mLocalDbLib = new DatabaseLib(mContentResolver);

        mUsernameView = (EditText) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                boolean handled = false;
                mPasswordView.setCursorVisible(false);
                // hitting green checkmark button on keyboard
                if (id == EditorInfo.IME_ACTION_DONE) {
                    handled = true;
                }
                return handled;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.username_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        populateExistingUsers();
    }

    private void populateExistingUsers() {
        Log.d(LOG_TAG, "Populating existing users");
        getLoaderManager().initLoader(UserLoader.LOADER_ID, null, this);
    }

    @Override
    public Loader<List<User>> onCreateLoader(int id, Bundle args) {
        return new UserLoader(this, mContentResolver);
    }

    @Override
    public void onLoadFinished(Loader<List<User>> loader, List<User> users) {
        Log.d(LOG_TAG, "Finished loading users");
        mUsers = users;
    }

    @Override
    public void onLoaderReset(Loader<List<User>> loader) {

    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid username, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordCorrectLength(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid username
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            mUsernameView.setError(getString(R.string.error_invalid_username));
            focusView = mUsernameView;
            cancel = true;
        } else if (!isUsernameCorrectLength(username)) {
            mUsernameView.setError(getString(R.string.error_username_length));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            // Kick off a background task to perform the user login attempt.
            mAuthTask = new UserLoginTask(username, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isUsernameValid(String username) {
        if (TextUtils.getTrimmedLength(username) != username.length()) {
            return false;
        }

        return true;
    }

    private boolean isUsernameCorrectLength(String username) {
        if (username.length() < MIN_USERNAME_LENGTH || username.length() > MAX_USERNAME_LENGTH) {
            return false;
        }

        return true;
    }

    private boolean isPasswordValid(String password) {
        if (TextUtils.getTrimmedLength(password) != password.length()) {
            return false;
        }

        return true;
    }

    private boolean isPasswordCorrectLength(String password) {
        if (password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH) {
            return false;
        }

        return true;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;

        UserLoginTask(String username, String password) {
            mUsername = username;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean isExistingUser = false;

            for (User user : mUsers) {
                if (mUsername.equals(user.getUserName())) {
                    // Account exists, return true if the password matches.
                    isExistingUser = mPassword.equals(user.getPassword());
                    if (isExistingUser) {
                        Log.d(LOG_TAG, "Logged in as " + mUsername);
                    }
                    return isExistingUser;
                }
            }

            Log.d(LOG_TAG, "Created a new account as " + mUsername);
            // new user, add standard data: user and shopping list to database
            mLocalDbLib.addStandardData(mUsername, mPassword);

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;

            if (success) {
                UserSession userSession = UserSession.getUserSession();
                userSession.createSession(mUsername, mPassword);
                String userId = mLocalDbLib.getUserId();
                userSession.setDatabaseIds(userId);
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }
}

