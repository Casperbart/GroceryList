package com.printz.guano.shoppingassistant.edit_list;

import android.content.Context;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.printz.guano.shoppingassistant.R;
import com.printz.guano.shoppingassistant.misc.OnSwipeTouchListener;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListAdapter extends ArrayAdapter<Ware> {

    private static final String LOG_TAG = ShoppingListAdapter.class.getSimpleName();
    private LayoutInflater mLayoutInflater;
    private ListView mWaresListView;
    private List<Ware> mWares;

    public ShoppingListAdapter(Context context, ArrayList<Ware> wares, ListView waresListView) {
        super(context, android.R.layout.simple_list_item_2);
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mWares = wares;
        mWaresListView = waresListView;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final Ware ware = getItem(position);

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.item_ware, parent, false);

            holder.wareQuantity = (ImageButton) convertView.findViewById(R.id.imageButtonQuantity);
            holder.wareName = (TextView) convertView.findViewById(R.id.textViewWareName);
            holder.wareSpecialOffer = (ImageButton) convertView.findViewById(R.id.imageButtonSpecialOffer);
            holder.parentLayout = (RelativeLayout) convertView.findViewById(R.id.relativeLayoutWare);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.wareName.setText(ware.getName());
        if (ware.isMarked()) {
            holder.wareName.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.wareName.setTextColor(ContextCompat.getColor(getContext(), R.color.colorMarked));
            holder.wareSpecialOffer.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorMarked));
            holder.wareQuantity.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorMarked));
        } else {
            holder.wareName.setPaintFlags(0);
            holder.wareName.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
            holder.wareSpecialOffer.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
            holder.wareQuantity.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        }

        holder.parentLayout.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
            @Override
            public void onSwipeLeft() {
                Log.d(LOG_TAG, "Swiped from parent layout position " + position);

                Ware ware = getItem(position);
                remove(ware);
                notifyDataSetChanged();
            }
        });

        holder.wareName.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
            @Override
            public void onSwipeRight() {
                Ware ware = getItem(position);
                remove(ware);
                notifyDataSetChanged();
            }

            @Override
            public void onSwipeLeft() {
                Ware ware = getItem(position);
                remove(ware);
                notifyDataSetChanged();
            }

            @Override
            public void onClick() {
                Log.d(LOG_TAG, "Clicked position " + position);
                Ware ware = getItem(position);
                final boolean isMarked = ware.isMarked();

                if (isMarked) {
                    remove(ware);
                    insert(ware, 0);
                    ware.setMarked(false);
                } else {
                    remove(ware);
                    int index = getLastMarked();
                    insertAboveMarked(ware, index);
                    ware.setMarked(true);
                }

                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    /**
     * @param ware Inserts the item_ware into the unmarked wares as the last item
     */
    public void insertAboveMarked(Ware ware, int index) {
        int count = getCount();
        if (index == getCount()) {
            add(ware);
        } else {
            insert(ware, index);
        }
    }

    /**
     * @return The index of the first marked item
     */
    public int getLastMarked() {
        int listCount = getCount();
        for (int i = 0; i < listCount; i++) {
            if (getItem(i).isMarked()) {
                return i == 0 ? 0 : i;
            }
        }
        return listCount;
    }

    private List<Ware> getMarkesWares() {
        ArrayList<Ware> markedWares = new ArrayList<>();
        int listCount = getCount();
        for(int i = 0; i < listCount; i++) {
            Ware ware = getItem(i);
            if (ware.isMarked()) {
               markedWares.add(ware);
            }
        }

        return markedWares;
    }

    public void deleteMarkedWares() {
        for(Ware markedWare : getMarkesWares()) {
            remove(markedWare);
        }
        notifyDataSetChanged();
    }

    /**
     * @param wares Populates the items of the listview
     */
    public void setData(List<Ware> wares) {
        clear();
        if (wares != null) {
            for (Ware ware : wares) {
                add(ware);
            }
        }
    }

    public ArrayList<Ware> getValues() {
        ArrayList<Ware> returnWares = new ArrayList<>();
        int size = getCount();

        for(int i = 0; i < size; i++) {
            returnWares.add(getItem(i));
        }

        return returnWares;
    }

    private static class ViewHolder {
        TextView wareName;
        ImageButton wareQuantity;
        ImageButton wareSpecialOffer;
        RelativeLayout parentLayout;
    }
}
