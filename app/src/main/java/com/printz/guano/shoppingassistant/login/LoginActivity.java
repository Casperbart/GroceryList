package com.printz.guano.shoppingassistant.login;

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
import com.printz.guano.shoppingassistant.RestClient;
import com.printz.guano.shoppingassistant.UserSession;
import com.printz.guano.shoppingassistant.database.DatabaseLib;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

/**
 * A login screen that offers login via username/password.
 */
public class LoginActivity extends BaseActivity {

    private static final String LOG_TAG = LoginActivity.class.getSimpleName();

    private DatabaseLib mLocalDbLib;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserAuthenticationTask mAuthTask = null;

    // UI references.
    private EditText mUsernameView;
    private EditText mPasswordView;

    enum AuthenticationStatus {
        REGISTER_OK, REGISTER_USERNAME_EXISTS, LOGIN_OK,
        LOGIN_WRONG_USERNAME, LOGIN_WRONG_PASSWORD, SERVER_ERROR, UNHANDLED_ERROR
    }

    enum AuthenticationType {REGISTER, LOGIN}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        activateToolbarWithHomeEnabled();

        mLocalDbLib = new DatabaseLib(getContentResolver());

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

        Button signUpButton = (Button) findViewById(R.id.buttonSignUp);
        signUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptAuthentication(AuthenticationType.REGISTER);
            }
        });

        Button loginButton = (Button) findViewById(R.id.buttonLogIn);
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptAuthentication(AuthenticationType.LOGIN);
            }
        });
    }

    /**
     * Attempts to sign in the account specified by the authenticaion form.
     * If there are form errors (invalid username, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptAuthentication(AuthenticationType authenticationType) {
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

        FormValidator formValidator = new FormValidator(username, password, cancel, focusView);
        formValidator.validate();
        cancel = formValidator.isCancel();
        focusView = formValidator.getFocusView();

        if (cancel) {
            focusView.requestFocus();
        } else {
            // Kick off a background task to perform the user authentication.
            mAuthTask = new UserAuthenticationTask(authenticationType, username, password);
            mAuthTask.execute((Void) null);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    private class UserAuthenticationTask extends AsyncTask<Void, Void, AuthenticationStatus> {

        private final String mUsername;
        private final String mPassword;

        // rest client to call authentication service
        private final RestClient mRestClient;

        // authentication type, either login or sign up, to perform
        private final AuthenticationType mAuthenticationType;

        UserAuthenticationTask(AuthenticationType authenticationType, String username, String password) {
            mAuthenticationType = authenticationType;
            mUsername = username;
            mPassword = password;
            mRestClient = new RestClient();
        }

        @Override
        protected AuthenticationStatus doInBackground(Void... params) {

            switch (mAuthenticationType) {
                case REGISTER:
                    return register();
                case LOGIN:
                    return login();
                default:
                    // this should never happen, its actually impossible.. I think
                    return AuthenticationStatus.UNHANDLED_ERROR;
            }
        }

        private AuthenticationStatus login() {
            mRestClient.setRoute(RestClient.LOGIN);
            mRestClient.addBodyParam("username", mUsername);
            mRestClient.addBodyParam("password", mPassword);

            int responseCode = 0;

            try {
                responseCode = mRestClient.executePost();
            } catch (Exception e) {
                // do noting m9!
            }

            if (responseCode == HttpURLConnection.HTTP_OK) {
                return AuthenticationStatus.LOGIN_OK;
            } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                return AuthenticationStatus.LOGIN_WRONG_USERNAME;
            } else if (responseCode == HttpURLConnection.HTTP_FORBIDDEN) {
                return AuthenticationStatus.LOGIN_WRONG_PASSWORD;
            } else if (responseCode == HttpURLConnection.HTTP_INTERNAL_ERROR) {
                return AuthenticationStatus.SERVER_ERROR;
            } else {
                return AuthenticationStatus.UNHANDLED_ERROR;
            }
        }

        private AuthenticationStatus register() {
            mRestClient.setRoute(RestClient.REGISTER);
            mRestClient.addBodyParam("username", mUsername);
            mRestClient.addBodyParam("password", mPassword);

            int responseCode = 0;

            try {
                responseCode = mRestClient.executePost();
            } catch (Exception e) {
                // do noting m9!
            }

            if (responseCode == HttpURLConnection.HTTP_OK) {
                return AuthenticationStatus.REGISTER_OK;
            } else if (responseCode == HttpURLConnection.HTTP_CONFLICT) {
                return AuthenticationStatus.REGISTER_USERNAME_EXISTS;
            } else if (responseCode == HttpURLConnection.HTTP_INTERNAL_ERROR) {
                return AuthenticationStatus.SERVER_ERROR;
            } else {
                return AuthenticationStatus.UNHANDLED_ERROR;
            }
        }

        @Override
        protected void onPostExecute(final AuthenticationStatus status) {
            mAuthTask = null;

            switch (status) {
                case LOGIN_OK:
                    Log.d(LOG_TAG, "Logged in as " + mUsername);
                    startSession();
                    finish();
                    break;
                case REGISTER_OK:
                    Log.d(LOG_TAG, "Created a new account as " + mUsername);
                    startSession();
                    finish();
                    break;
                case LOGIN_WRONG_PASSWORD:
                    mPasswordView.setError(getString(R.string.auth_wrong_password));
                    mPasswordView.requestFocus();
                    break;
                case LOGIN_WRONG_USERNAME:
                    mUsernameView.setError(getString(R.string.auth_username_does_not_exist));
                    mUsernameView.requestFocus();
                    break;
                case REGISTER_USERNAME_EXISTS:
                    mUsernameView.setError(getString(R.string.auth_username_already_exists));
                    mUsernameView.requestFocus();
                    break;
                case SERVER_ERROR:
                    mUsernameView.setError(getString(R.string.auth_server_error));
                    mUsernameView.requestFocus();
                    break;
                case UNHANDLED_ERROR:
                    mUsernameView.setError(getString(R.string.auth_unknown_error));
                    mUsernameView.requestFocus();
                    break;
                default:
                    // cant happen because of register() method return value
            }
        }

        private void startSession() {
            JSONObject readData = mRestClient.getResponseBody();

            try {
                String userId = readData.getString("uId");
                String username = readData.getString("username");

                if (!mLocalDbLib.isExistingUser(username)) {
                    mLocalDbLib.insertUser(userId, username);
                }
                UserSession userSession = UserSession.getUserSession();
                userSession.createSession(userId, username);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }

    private class FormValidator {

        /**
         * Credential sanitation variables
         */
        private final int MIN_PASSWORD_LENGTH = 5;
        private final int MIN_USERNAME_LENGTH = 5;
        private final int MAX_PASSWORD_LENGTH = 100;
        private final int MAX_USERNAME_LENGTH = 100;

        private String username;
        private String password;
        private boolean cancel;
        private View focusView;

        public FormValidator(String username, String password, boolean cancel, View focusView) {
            this.username = username;
            this.password = password;
            this.cancel = cancel;
            this.focusView = focusView;
        }

        public boolean isCancel() {
            return cancel;
        }

        public View getFocusView() {
            return focusView;
        }

        public void validate() {
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
                mPasswordView.setError(getString(R.string.error_password_length));
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
        }

        private boolean isPasswordValid(String password) {
            return TextUtils.getTrimmedLength(password) == password.length();

        }

        private boolean isPasswordCorrectLength(String password) {
            return !(password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH);

        }

        private boolean isUsernameValid(String username) {
            return TextUtils.getTrimmedLength(username) == username.length();

        }

        private boolean isUsernameCorrectLength(String username) {
            return !(username.length() < MIN_USERNAME_LENGTH || username.length() > MAX_USERNAME_LENGTH);

        }
    }
}

