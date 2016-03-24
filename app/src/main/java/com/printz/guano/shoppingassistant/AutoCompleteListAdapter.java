package com.printz.guano.shoppingassistant;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

public class AutoCompleteListAdapter extends ArrayAdapter<String> {

    private static final String LOG_TAG = AutoCompleteListAdapter.class.getSimpleName();

    public AutoCompleteListAdapter(Context context, int resource, List<String> wareNames) {
        super(context, resource, wareNames);
    }

    /**
     * Populates the items of the auto complete box
     *
     * @param wareHistories
     */
    public void setData(List<String> wareHistories) {
        clear();
        if (wareHistories != null) {
            for (String wareHistory : wareHistories) {
                add(wareHistory);
            }
        }
        notifyDataSetChanged();
    }
}
