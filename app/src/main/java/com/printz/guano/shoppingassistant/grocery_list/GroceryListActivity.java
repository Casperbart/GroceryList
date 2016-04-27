package com.printz.guano.shoppingassistant.grocery_list;


import android.accounts.Account;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.printz.guano.shoppingassistant.BaseActivity;
import com.printz.guano.shoppingassistant.R;
import com.printz.guano.shoppingassistant.TableObserver;
import com.printz.guano.shoppingassistant.UserSession;
import com.printz.guano.shoppingassistant.database.Contract;
import com.printz.guano.shoppingassistant.login.LoginActivity;
import com.printz.guano.shoppingassistant.sharing.ShareActivity;
import com.printz.guano.shoppingassistant.account.SyncAccount;

import java.util.ArrayList;
import java.util.List;

public class GroceryListActivity extends BaseActivity
        implements OptionsDialogListener, UserSession.SessionChangedListener,
        QuantityDialogListener {

    private final static String LOG_TAG = GroceryListActivity.class.getSimpleName();

    private TopAutoCompleteTextView mTopAutoCompleteTextView;
    private Button mCloseButton;
    private WareHistoryAdapter mWareHistoryAdapter;
    private WareAdapter mWareAdapter;
    private FragmentManager mFragmentManager;
    private TableObserver wareObserver;
    private Account mAccount;

    /**
     * Call back LoaderManagers to handle loading of WareHistory's and Ware's
     */
    private LoaderManager.LoaderCallbacks<List<WareHistory>> mWareHistoryLoaderListener;
    private LoaderManager.LoaderCallbacks<List<Ware>> mWareLoaderListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    Log.d(LOG_TAG, "I am now created");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        activateToolbar();

        UserSession.initializeUserSession(this);


        mFragmentManager = getSupportFragmentManager();

        ListView mWareListView = (ListView) findViewById(R.id.listViewWares);
        mTopAutoCompleteTextView = (TopAutoCompleteTextView) findViewById(R.id.autoCompleteAddWare);

        mCloseButton = (Button) findViewById(R.id.buttonCloseDropDown);

        mWareAdapter = new WareAdapter(this, R.layout.item_ware);
        mWareHistoryAdapter = new WareHistoryAdapter(this, R.layout.item_dropdown);

        mWareListView.setAdapter(mWareAdapter);
        mTopAutoCompleteTextView.setAdapter(mWareHistoryAdapter);

        mTopAutoCompleteTextView.setThreshold(1);

        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTopAutoCompleteTextView.isPopupShowing()) {
                    mTopAutoCompleteTextView.dismissDropDown();
                } else {
                    mTopAutoCompleteTextView.clearFocus();
                }
            }
        });

        mTopAutoCompleteTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mCloseButton.setVisibility(View.VISIBLE);
                    if (mTopAutoCompleteTextView.enoughToFilter()) {
                        mTopAutoCompleteTextView.showDropDown();
                    }
                } else {
                    mCloseButton.setVisibility(View.GONE);
                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });

        mTopAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WareHistory wareHistory = (WareHistory) parent.getItemAtPosition(position);
                if (isValidName(wareHistory.getName())) {
                    addWare(wareHistory.getName());
                }
            }
        });

        mTopAutoCompleteTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    handled = true;
                    String wareName = mTopAutoCompleteTextView.getText().toString();
                    if (isValidName(wareName)) {
                        addWare(wareName);
                    }
                }
                return handled;
            }
        });

        setupLoaderListeners();

        // create account required to syncadapter.xml framework
        mAccount = SyncAccount.createSyncAccount(this);

        wareObserver = new TableObserver(null, mAccount);

        if (savedInstanceState == null) {
            loadApplicationData();
        }

//        ListerDatabase.deleteDatabase(this);
//        UserSession userSession = UserSession.getUserSession();
//        userSession.clearSession();
    }

    @Override
    public void onQuantityDialogFinished(int position, String amount, String type) {
        Log.d(LOG_TAG, "Selected '" + amount + " " + type + "'");
        mWareAdapter.setQuantity(position, amount, type);
    }

    private void setupLoaderListeners() {
        mWareHistoryLoaderListener = new LoaderManager.LoaderCallbacks<List<WareHistory>>() {
            @Override
            public Loader<List<WareHistory>> onCreateLoader(int id, Bundle args) {
                Context context = getBaseContext();
                Log.d(LOG_TAG, "Creating ware history loader");
                return new WareHistoryLoader(context);
            }

            @Override
            public void onLoadFinished(Loader<List<WareHistory>> loader, List<WareHistory> wareHistories) {
                Log.d(LOG_TAG, "Finished loading ware history");
                mWareHistoryAdapter.setWareHistories(wareHistories);
            }

            @Override
            public void onLoaderReset(Loader<List<WareHistory>> loader) {

            }
        };

        mWareLoaderListener = new LoaderManager.LoaderCallbacks<List<Ware>>() {
            @Override
            public Loader<List<Ware>> onCreateLoader(int id, Bundle args) {
                Context context = getBaseContext();
                Log.d(LOG_TAG, "Creating ware loader");
                return new WareLoader(context);
            }

            @Override
            public void onLoadFinished(Loader<List<Ware>> loader, List<Ware> wares) {
                Log.d(LOG_TAG, "Finished loading wares");
                mWareAdapter.setWares(wares);
            }

            @Override
            public void onLoaderReset(Loader<List<Ware>> loader) {

            }
        };
    }

    private boolean isValidName(String wareName) {
        return wareName.length() != 0;
    }

    private void loadApplicationData() {
        Log.d(LOG_TAG, "Loading application data");
        getLoaderManager().initLoader(WareHistoryLoader.LOADER_ID, null, mWareHistoryLoaderListener);
        getLoaderManager().initLoader(WareLoader.LOADER_ID, null, mWareLoaderListener);
    }

    /**
     * This method restarts the Ware and WareHistory loader.. This is required when the managed data changes.
     * The managed data changes whenever onSessionChanged is triggered, that is whenever a user
     * logs in or out.
     */
    private void reloadApplicationData() {
        Log.d(LOG_TAG, "Reloading application data");
        getLoaderManager().restartLoader(WareHistoryLoader.LOADER_ID, null, mWareHistoryLoaderListener);
        getLoaderManager().restartLoader(WareLoader.LOADER_ID, null, mWareLoaderListener);
    }

    /**
     * Invalidate menu and load new user data
     */
    @Override
    public void onSessionChanged() {
        Log.d(LOG_TAG, "Session changed");
        resetFocus();
        resetAddWareText();
        setActivityTitle();
        reloadApplicationData();
        invalidateOptionsMenu();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(LOG_TAG, "Saving activity state");

        outState.putParcelableArrayList("wares", mWareAdapter.getWares());
        outState.putParcelableArrayList("wareHistories", mWareHistoryAdapter.getWareHistories());

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "Restoring activity state");

        ArrayList<Ware> savedWares = savedInstanceState.getParcelableArrayList("wares");
        ArrayList<WareHistory> savedWareHistories = savedInstanceState.getParcelableArrayList("wareHistories");
        mWareAdapter.setWares(savedWares);
        mWareHistoryAdapter.setWareHistories(savedWareHistories);

        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * Adds ware to shopping list adapter as well as the ware history.
     * @param wareName The name of the ware to insert
     */
    private void addWare(String wareName) {
        Ware ware = new Ware(0, wareName, 0, false, "-", "-");
        WareHistory wareHistory = new WareHistory(1, wareName, 1);

        mWareAdapter.insertWare(ware);
        mWareHistoryAdapter.insertWareHistory(wareHistory);

        mTopAutoCompleteTextView.setText("");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        resetFocus();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        MenuItem share = menu.findItem(R.id.action_share);
        MenuItem loginSignUp = menu.findItem(R.id.action_login_signup);
        MenuItem logout = menu.findItem(R.id.action_logout);

        UserSession userSession = UserSession.getUserSession();
        if (userSession.isSessionActive()) {
            share.setVisible(true);
            loginSignUp.setVisible(false);
            logout.setVisible(true);
        } else {
            share.setVisible(false);
            loginSignUp.setVisible(true);
            logout.setVisible(false);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                Log.d(LOG_TAG, "Starting share activity");
                Intent intentShare = new Intent(GroceryListActivity.this, ShareActivity.class);
                startActivity(intentShare);
                break;
            case R.id.action_delete_wares:
                ShoppingListDialog dialogDeleteAll = new ShoppingListDialog();
                Bundle argsDeleteWares = new Bundle();
                argsDeleteWares.putString(ShoppingListDialog.DIALOG_TYPE, ShoppingListDialog.DELETE_ALL_WARES);
                dialogDeleteAll.setArguments(argsDeleteWares);
                dialogDeleteAll.show(mFragmentManager, "delete-all-wares");
                break;
            case R.id.action_delete_marked:
                ShoppingListDialog dialogDeleteMarked = new ShoppingListDialog();
                Bundle argsDeleteMarked = new Bundle();
                argsDeleteMarked.putString(ShoppingListDialog.DIALOG_TYPE, ShoppingListDialog.DELETE_ALL_MARKED);
                dialogDeleteMarked.setArguments(argsDeleteMarked);
                dialogDeleteMarked.show(mFragmentManager, "delete-all-marked");
                break;
            case R.id.action_login_signup:
                Log.d(LOG_TAG, "Starting Login activity");
                Intent intentLogin = new Intent(GroceryListActivity.this, LoginActivity.class);
                startActivity(intentLogin);
                break;
            case R.id.action_logout:
                Log.d(LOG_TAG, "Logged out of session");
                UserSession userSession = UserSession.getUserSession();
                userSession.clearSession();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        Log.d(LOG_TAG, "I am now stopped");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(LOG_TAG, "I am now destroyed");
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        Log.d(LOG_TAG, "I am now paused");

        getContentResolver().unregisterContentObserver(wareObserver);

        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.d(LOG_TAG, "I am now resumed, registering");

        getContentResolver().registerContentObserver(Contract.Ware.CONTENT_URI, true, wareObserver);

        setActivityTitle();
        super.onResume();
    }

    @Override
    protected void onStart() {
        Log.d(LOG_TAG, "I am now started");
        super.onStart();
    }

    @Override
    protected void onRestart() {
        Log.d(LOG_TAG, "I am now restarted");
        super.onRestart();
    }

    /**
     * Sets the title of the activity. The user name is added to the
     * title if a user is logged in.
     */
    private void setActivityTitle() {
        UserSession userSession = UserSession.getUserSession();
        String activityTitle = "Shopping List";

        if(userSession.isSessionActive()) {
            String userName = userSession.getUserName();
            activityTitle = userName + "'s " + activityTitle;
        }

        setTitle(activityTitle);
    }

    private void resetFocus() {
        View view = getCurrentFocus();
        if(view != null) {
            view.clearFocus();
        }
    }

    private void resetAddWareText() {
        mTopAutoCompleteTextView.setText("");
    }

    @Override
    public void onOptionsDialogFinished(final String DIALOG_TYPE, final boolean ANSWER) {
        switch (DIALOG_TYPE) {
            case ShoppingListDialog.DELETE_ALL_WARES:
                if (ANSWER) {
                    int deleteCount = mWareAdapter.deleteAllWares();
                    Log.d(LOG_TAG, "Deleted " + deleteCount + " wares");
                }
                break;
            case ShoppingListDialog.DELETE_ALL_MARKED:
                if (ANSWER) {
                    int deleteCount = mWareAdapter.deleteMarkedWares();
                    Log.d(LOG_TAG, "Deleted " + deleteCount + " marked wares");
                }
                break;
            default:
                Log.d(LOG_TAG, "Unknown dialog type: " + DIALOG_TYPE);
                break;
        }
    }
}
