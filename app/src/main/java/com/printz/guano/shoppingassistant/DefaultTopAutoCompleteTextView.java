package com.printz.guano.shoppingassistant;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;

public class DefaultTopAutoCompleteTextView extends AutoCompleteTextView {

    private int shortHeight = (int) getResources().getDimension(R.dimen.shortDropDown);
    private int tallHeight = (int) getResources().getDimension(R.dimen.tallDropDown);
    private int filterCount;

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
        boolean handled = false;
        Log.d("tag", "keypreime triggered");
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN && hasFocus()) {
            InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputManager.hideSoftInputFromWindow(findFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS)) {
                handled = true;
            }
        }

        return handled;
    }
}
