package com.printz.guano.shoppingassistant.edit_list;


import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
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
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;

import com.printz.guano.shoppingassistant.AutoCompleteListAdapter;
import com.printz.guano.shoppingassistant.BaseActivity;
import com.printz.guano.shoppingassistant.DefaultTopAutoCompleteTextView;
import com.printz.guano.shoppingassistant.R;
import com.printz.guano.shoppingassistant.WareHistory;
import com.printz.guano.shoppingassistant.WareHistoryContract;
import com.printz.guano.shoppingassistant.WareHistoryLoader;
import com.printz.guano.shoppingassistant.login.LoginActivity;
import com.printz.guano.shoppingassistant.share_list.ShareActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShoppingListActivity extends BaseActivity
        implements ShoppingListDialogListener, LoaderManager.LoaderCallbacks<List<WareHistory>> {

    private final static String LOG_TAG = ShoppingListActivity.class.getSimpleName();
    private final static int LOADER_ID = 1;
    private ArrayList<WareHistory> mWareHistories;
    private ListView mShoppingListView;
    private AutoCompleteTextView mAutoCompleteTextView;
    private AutoCompleteListAdapter mAutoCompleteAdapter;
    private ShoppingListAdapter mShoppingListAdapter;
    private FragmentManager mFragmentManager;
    private ContentResolver mContentResolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "I am now created");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        activateToolbar();

        mFragmentManager = getSupportFragmentManager();
        mContentResolver = getContentResolver();
//        mShoppingListView = (ListView) findViewById(R.id.listViewWares);
//        mShoppingListAdapter = new ShoppingListAdapter(this, new ArrayList<Ware>(), mShoppingListView);

        mAutoCompleteTextView = (DefaultTopAutoCompleteTextView) findViewById(R.id.autoCompleteAddWare);

        mAutoCompleteAdapter = new AutoCompleteListAdapter(
                this, R.layout.item_dropdown, new ArrayList<String>()
        );

//        mShoppingListView.setAdapter(mShoppingListAdapter);
        mAutoCompleteTextView.setAdapter(mAutoCompleteAdapter);

        mAutoCompleteTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Log.d(LOG_TAG, "Auto complete has focus");
                    mAutoCompleteTextView.showDropDown();
                } else {
                    Log.d(LOG_TAG, "Auto complete lost focus");
                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    mAutoCompleteTextView.dismissDropDown();
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
                // hitting green checkmark button on keyboard
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

        if (savedInstanceState == null) {
            PopulateWareAutoComplete();
        }
    }

    private void PopulateWareAutoComplete() {
        Log.d(LOG_TAG, "Populating autocomplete");
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(LOG_TAG, "I am now saving state");
        ArrayList<Ware> wares = mShoppingListAdapter.getValues();
        outState.putParcelableArrayList("wares", wares);
        outState.putParcelableArrayList("wareHistories", mWareHistories);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "I am now restoring state");
        ArrayList<Ware> savedWares = savedInstanceState.getParcelableArrayList("wares");
        mShoppingListAdapter.setData(savedWares);
        ArrayList<WareHistory> savedWareHistories = savedInstanceState.getParcelableArrayList("wareHistories");
        addNamesToWareAutoComplete(savedWareHistories);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public Loader<List<WareHistory>> onCreateLoader(int id, Bundle args) {
        return new WareHistoryLoader(this, mContentResolver);
    }

    @Override
    public void onLoadFinished(Loader<List<WareHistory>> loader, List<WareHistory> wareHistories) {
        addNamesToWareAutoComplete(wareHistories);
    }

    @Override
    public void onLoaderReset(Loader<List<WareHistory>> loader) {

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

    private void addWare(String wareName) {
        Ware ware = new Ware(wareName);
        int index = mShoppingListAdapter.getLastMarked();
        mShoppingListAdapter.insertAboveMarked(ware, index);
        mAutoCompleteTextView.setText("");

        insertWareIntoHistory(wareName);
    }

    /**
     * This updates the WareHistory if it already exists and inserts
     * a new WareHistory if it is null. The method both updates the existing
     * WareHistory list mWaresHistories and local database and likewise for inserts.
     *
     * @param wareName name of ware
     */
    private void insertWareIntoHistory(String wareName) {
        WareHistory wareHistory = null;

        for (WareHistory wHistory : mWareHistories) {
            if (wHistory.getName().equals(wareName)) {
                wareHistory = wHistory;
                break;
            }
        }

        if (wareHistory == null) {
            Uri returnedUri = insertWareHistoryIntoDatabase(wareName);
            addToExistingWareHistories(wareName, returnedUri);
        } else {
            int count = updateExistingWareHistories(wareHistory);
            updateWareHistoryInDatabase(count, wareHistory.getId());
        }
    }

    /**
     * Inserts a new WareHistory into the database.
     *
     * @param wareName name of ware
     * @return the uri of the new inserted WareHistory row
     */
    private Uri insertWareHistoryIntoDatabase(String wareName) {
        ContentValues values = new ContentValues();
        values.put(WareHistoryContract.WareHistoryColumns.WARE_HISTORY_NAME, wareName);
        values.put(WareHistoryContract.WareHistoryColumns.WARE_HISTORY_COUNT, 1);
        return mContentResolver.insert(WareHistoryContract.TABLE_URI, values);
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

    /**
     * Updates the WareHistory in the database with a +1 count
     *
     * @param count the count to update the ware in the database with
     * @param id    the id to construct the warehistory Uri
     */
    private void updateWareHistoryInDatabase(int count, int id) {
        Uri uri = WareHistoryContract.WareHistory.buildWareHistoryUri(String.valueOf(id));
        ContentValues values = new ContentValues();
        values.put(WareHistoryContract.WareHistoryColumns.WARE_HISTORY_COUNT, count + 1);
        mContentResolver.update(uri, values, null, null);
    }

    private boolean isValidName(String wareName) {
        return wareName.length() != 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                Log.d(LOG_TAG, "starting share activity");
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
                Intent intentLogin = new Intent(ShoppingListActivity.this, LoginActivity.class);
                startActivity(intentLogin);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        Log.d(LOG_TAG, "Im am now restoring persistance state");
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

    @Override
    public void onDialogFinishes(final String DIALOG_TYPE, final boolean RESPONSE) {
        switch (DIALOG_TYPE) {
            case ShoppingListDialog.DELETE_ALL_WARES:
                if (RESPONSE) {
                    mShoppingListAdapter.setData(null);
                }
                break;
            case ShoppingListDialog.DELETE_ALL_MARKED:
                if (RESPONSE) {
                    mShoppingListAdapter.deleteMarkedWares();
                }
                break;
            default:
                Log.d(LOG_TAG, "Unknown dialog type: " + DIALOG_TYPE);
                break;
        }
    }
}
