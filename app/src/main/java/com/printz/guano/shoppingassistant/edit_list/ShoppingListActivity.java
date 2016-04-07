package com.printz.guano.shoppingassistant.edit_list;


import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.printz.guano.shoppingassistant.AutoCompleteListAdapter;
import com.printz.guano.shoppingassistant.BaseActivity;
import com.printz.guano.shoppingassistant.DefaultTopAutoCompleteTextView;
import com.printz.guano.shoppingassistant.R;
import com.printz.guano.shoppingassistant.UserSession;
import com.printz.guano.shoppingassistant.database.DatabaseLib;
import com.printz.guano.shoppingassistant.database.WareHistoryContract;
import com.printz.guano.shoppingassistant.login.LoginActivity;
import com.printz.guano.shoppingassistant.share_list.ShareActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShoppingListActivity extends BaseActivity
        implements ShoppingListDialogListener, UserSession.SessionChangedListener {

    private final static String LOG_TAG = ShoppingListActivity.class.getSimpleName();

    private ArrayList<WareHistory> mWareHistories;
    private DefaultTopAutoCompleteTextView mAutoCompleteTextView;
    private Button mCloseDropdownButton;
    private AutoCompleteListAdapter mAutoCompleteAdapter;
    private WareListAdapter mShoppingListAdapter;
    private FragmentManager mFragmentManager;
    private ContentResolver mContentResolver;
    private DatabaseLib mDatabaseLib;

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
//        ListerDatabase.deleteDatabase(this);

        mFragmentManager = getSupportFragmentManager();
        mContentResolver = getContentResolver();
        mDatabaseLib = new DatabaseLib(mContentResolver);
        ListView mWareListView = (ListView) findViewById(R.id.listViewWares);
        mShoppingListAdapter = new WareListAdapter(this);

        mAutoCompleteTextView = (DefaultTopAutoCompleteTextView) findViewById(R.id.autoCompleteAddWare);
        mCloseDropdownButton = (Button) findViewById(R.id.buttonCloseDropDown);

        mAutoCompleteAdapter = new AutoCompleteListAdapter(
                this, R.layout.item_dropdown, new ArrayList<String>()
        );

        mWareListView.setAdapter(mShoppingListAdapter);
        mAutoCompleteTextView.setAdapter(mAutoCompleteAdapter);
        mAutoCompleteTextView.setThreshold(1);

        mCloseDropdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAutoCompleteTextView.isPopupShowing()) {
                    mAutoCompleteTextView.dismissDropDown();
                } else {
                    mAutoCompleteTextView.clearFocus();
                }
            }
        });

        mAutoCompleteTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mCloseDropdownButton.setVisibility(View.VISIBLE);
                    if (mAutoCompleteTextView.enoughToFilter()) {
                        mAutoCompleteTextView.showDropDown();
                    }
                } else {
                    mCloseDropdownButton.setVisibility(View.GONE);
                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });

        mAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String wareName = (String) parent.getItemAtPosition(position);
                if (isValidName(wareName)) {
                    addWare(wareName);
                }
            }
        });

        mAutoCompleteTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    handled = true;
                    String wareName = mAutoCompleteTextView.getText().toString();
                    if (isValidName(wareName)) {
                        addWare(wareName);
                    }
                }
                return handled;
            }
        });

        setupLoaderListeners();

        if (savedInstanceState == null) {
            loadApplicationData();
        }
    }

    private void setupLoaderListeners() {
        mWareHistoryLoaderListener = new LoaderManager.LoaderCallbacks<List<WareHistory>>() {
            @Override
            public Loader<List<WareHistory>> onCreateLoader(int id, Bundle args) {
                Context context = getBaseContext();
                Log.d(LOG_TAG, "Creating ware history loader");
                return new WareHistoryLoader(context, mContentResolver);
            }

            @Override
            public void onLoadFinished(Loader<List<WareHistory>> loader, List<WareHistory> wareHistories) {
                Log.d(LOG_TAG, "Finished loading ware history");
                addNamesToWareAutoComplete(wareHistories);
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
                return new WareLoader(context, mContentResolver);
            }

            @Override
            public void onLoadFinished(Loader<List<Ware>> loader, List<Ware> wares) {
                Log.d(LOG_TAG, "Finished loading wares");
                Collections.sort(wares); // sorts to maintain position of wares in listview
                mShoppingListAdapter.setWares(wares);
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

    private void addNamesToWareAutoComplete(List<WareHistory> wareHistories) {
        Collections.sort(wareHistories);
        Collections.reverse(wareHistories);
        mWareHistories = (ArrayList) wareHistories;

        List<String> wareNames = new ArrayList<>();
        for (WareHistory wareHistory : wareHistories) {
            wareNames.add(wareHistory.getName());
        }

        mAutoCompleteAdapter.setData(wareNames);
    }

    /**
     * Invalidate menu and load new user data
     */
    @Override
    public void onSessionChanged() {
        Log.d(LOG_TAG, "Session changed");
        reloadApplicationData();
        setActivityTitle();
        mAutoCompleteTextView.setText("");
        invalidateOptionsMenu();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(LOG_TAG, "Saving activity state");
        ArrayList<Ware> wares = mShoppingListAdapter.getWares();
        outState.putParcelableArrayList("wares", wares);
        outState.putParcelableArrayList("wareHistories", mWareHistories);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "Restoring activity state");

        ArrayList<Ware> savedWares = savedInstanceState.getParcelableArrayList("wares");
        mShoppingListAdapter.setWares(savedWares);
        ArrayList<WareHistory> savedWareHistories = savedInstanceState.getParcelableArrayList("wareHistories");
        addNamesToWareAutoComplete(savedWareHistories);
        super.onRestoreInstanceState(savedInstanceState);
    }


    /**
     * Adds ware to shopping list adapter as well as the ware history.
     * @param wareName The name of the ware to insert
     */
    private void addWare(String wareName) {
        Ware ware = new Ware(0, wareName, 0, false, "not set", "not set");
        mShoppingListAdapter.insertWare(ware);
        addHistory(wareName);
        mAutoCompleteTextView.setText("");
    }

    /**
     * This updates the WareHistory if it already exists and inserts
     * a new WareHistory if it is null. The method both updates the existing
     * WareHistory list mWaresHistories and local database and likewise for inserts.
     *
     * @param wareName name of ware
     */
    private void addHistory(String wareName) {
        WareHistory wareHistory = isExistingWareHistory(wareName);

        if (wareHistory == null) { // first time this ware is added
            Uri returnedUri = mDatabaseLib.insertWareHistory(wareName);
            addToExistingWareHistories(wareName, returnedUri);
        } else {
            int count = updateExistingWareHistories(wareHistory);
            mDatabaseLib.updateWareHistory(wareHistory.getId(), count + 1);
        }
    }

    /**
     * Checks and returns the WareHistory if it exists and null if it does not
     *
     * @param wareName The name of the ware to check if it already exists
     * @return The WareHistory if it exists, and null if it does not
     */
    @Nullable
    private WareHistory isExistingWareHistory(String wareName) {
        WareHistory wareHistory = null;

        for (WareHistory wHistory : mWareHistories) {
            if (wHistory.getName().equals(wareName)) {
                wareHistory = wHistory;
                break;
            }
        }
        return wareHistory;
    }

    /**
     * Inserts a new WareHistory to the mWaresHistories and updates the
     * WareHistory names autocomplete with the new bigger list
     *
     * @param wareName    name of ware
     * @param returnedUri returned Uri by provider on new ware insert,
     *                    all wares have an associated id so its used to create ware isntance
     */
    private void addToExistingWareHistories(String wareName, Uri returnedUri) {
        String id = WareHistoryContract.WareHistory.getWareHistoryId(returnedUri);
        WareHistory newWareHistory = new WareHistory(Integer.valueOf(id), wareName, 1);
        mWareHistories.add(newWareHistory);
        addNamesToWareAutoComplete(mWareHistories);
    }

    /**
     * Updates an existing WareHistory name with a new count
     *
     * @param wareHistory the warehistory to insert into the existing auto complete
     * @return the count value later used by to insert warehistory into localdb
     */
    private int updateExistingWareHistories(WareHistory wareHistory) {
        int count = wareHistory.getCount();
        wareHistory.setCount(count + 1);
        addNamesToWareAutoComplete(mWareHistories);
        return count;
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
                Intent intentShare = new Intent(ShoppingListActivity.this, ShareActivity.class);
                startActivity(intentShare);
                break;
            case R.id.action_delete_wares:
                ShoppingListDialog dialogDeleteAll = new ShoppingListDialog();
                Bundle args1 = new Bundle();
                args1.putString(ShoppingListDialog.DIALOG_TYPE, ShoppingListDialog.DELETE_ALL_WARES);
                dialogDeleteAll.setArguments(args1);
                dialogDeleteAll.show(mFragmentManager, "delete-all-wares");
                break;
            case R.id.action_delete_marked:
                ShoppingListDialog dialogDeleteMarked = new ShoppingListDialog();
                Bundle args2 = new Bundle();
                args2.putString(ShoppingListDialog.DIALOG_TYPE, ShoppingListDialog.DELETE_ALL_MARKED);
                dialogDeleteMarked.setArguments(args2);
                dialogDeleteMarked.show(mFragmentManager, "delete-all-marked");
                break;
            case R.id.action_login_signup:
                Log.d(LOG_TAG, "Starting Login activity");
                Intent intentLogin = new Intent(ShoppingListActivity.this, LoginActivity.class);
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
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.d(LOG_TAG, "I am now resumed");
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

    @Override
    public void onDialogFinishes(final String DIALOG_TYPE, final boolean ANSWER) {
        switch (DIALOG_TYPE) {
            case ShoppingListDialog.DELETE_ALL_WARES:
                if (ANSWER) {
                    int deleteCount = mShoppingListAdapter.deleteAllWares();
                    Log.d(LOG_TAG, "Deleted " + deleteCount + " wares");
                }
                break;
            case ShoppingListDialog.DELETE_ALL_MARKED:
                if (ANSWER) {
                    int deleteCount = mShoppingListAdapter.deleteMarkedWares();
                    Log.d(LOG_TAG, "Deleted " + deleteCount + " marked wares");
                }
                break;
            default:
                Log.d(LOG_TAG, "Unknown dialog type: " + DIALOG_TYPE);
                break;
        }
    }
}
