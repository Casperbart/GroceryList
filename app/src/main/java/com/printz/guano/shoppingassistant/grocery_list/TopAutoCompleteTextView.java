package com.printz.guano.shoppingassistant.grocery_list;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;

public class TopAutoCompleteTextView extends AutoCompleteTextView {

    private final static String LOG_TAG = TopAutoCompleteTextView.class.getSimpleName();

    public TopAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TopAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TopAutoCompleteTextView(Context context) {
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
