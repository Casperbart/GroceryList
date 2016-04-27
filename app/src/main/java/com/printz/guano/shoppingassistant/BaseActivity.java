package com.printz.guano.shoppingassistant;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class BaseActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    protected Toolbar activateToolbar() {
        if(mToolbar == null) {
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            if(mToolbar != null) {
                setSupportActionBar(mToolbar);
            }
        }

        return mToolbar;
    }

    protected Toolbar activateToolbarWithHomeEnabled() {
        activateToolbar();
        if(mToolbar != null) {
            ActionBar actionBar = getSupportActionBar();
            if(actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }

        return mToolbar;
    }
}
