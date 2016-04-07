package com.printz.guano.shoppingassistant;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;

public class DefaultTopAutoCompleteTextView extends AutoCompleteTextView {

    private final static String LOG_TAG = DefaultTopAutoCompleteTextView.class.getSimpleName();

    public DefaultTopAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DefaultTopAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DefaultTopAutoCompleteTextView(Context context) {
        super(context);
    }



    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        Log.d(LOG_TAG, "Key pre ime triggered");
        boolean handled = false;
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            if(isPopupShowing()) {
                InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputManager.hideSoftInputFromWindow(findFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS)) {
                    handled = true;
                }
            }
        }

        return handled;
    }
}
