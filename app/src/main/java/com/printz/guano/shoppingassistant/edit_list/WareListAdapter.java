package com.printz.guano.shoppingassistant.edit_list;

import android.content.Context;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.printz.guano.shoppingassistant.R;
import com.printz.guano.shoppingassistant.database.DatabaseLib;
import com.printz.guano.shoppingassistant.database.WareContract;
import com.printz.guano.shoppingassistant.misc.OnSwipeTouchListener;

import java.util.ArrayList;
import java.util.List;

public class WareListAdapter extends ArrayAdapter<Ware> {

    private static final String LOG_TAG = WareListAdapter.class.getSimpleName();
    private LayoutInflater mLayoutInflater;
    private DatabaseLib mDatabaseLib;

    public WareListAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_2);
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDatabaseLib = new DatabaseLib(context.getContentResolver());
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

        holder.wareName.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
            @Override
            public void onSwipeRight() {
                deleteWare(position);
            }

            @Override
            public void onSwipeLeft() {
                deleteWare(position);
            }

            @Override
            public void onClick() {
                swapMarked(position);
            }
        });

        return convertView;
    }

    /**
     * Inserts Ware into the adapter and inserts it into the database.
     * @param ware The ware to insert.
     */
    public void insertWare(Ware ware) {
        Log.d(LOG_TAG, "Adding new ware '" + ware.getName() + "'");

        insertAboveMarked(ware);

        updateWarePositions();

        Uri wareUri = mDatabaseLib.insertWare(ware);
        String wareId = WareContract.Ware.getWareId(wareUri);
        ware.setId(Integer.valueOf(wareId));
        mDatabaseLib.updateWares(getWares());
    }

    /**
     * Removes all wares in adapter and the database.
     * @return Returns the number of wares removed.
     */
    public int deleteAllWares() {
        int deleteCount = 0;

        for(Ware ware : getWares()) {
            deleteCount += mDatabaseLib.deleteWare(ware);
        }

        clear();

        return deleteCount;
    }

    /**
     * Removes all marked wares in adapter and the database.
     * @return Returns the number of wares removed.
     */
    public int deleteMarkedWares() {
        int deleteCount = 0;

        for (Ware ware : getMarkedWares()) {
            remove(ware);
            deleteCount += mDatabaseLib.deleteWare(ware);
        }

        updateWarePositions();

        mDatabaseLib.updateWares(getWares());

        return deleteCount;
    }

    /**
     * @param wares Populates the wares in the adapter.
     */
    public void setWares(List<Ware> wares) {
        clear();
        if (wares != null) {
            for (Ware ware : wares) {
                add(ware);
            }
        }
    }

    /**
     * This method retrieves all the wares in the adapter and returns them.
     * @return Returns all the wares in the adapter.
     */
    public ArrayList<Ware> getWares() {
        ArrayList<Ware> returnWares = new ArrayList<>();
        int size = getCount();

        for (int i = 0; i < size; i++) {
            returnWares.add(getItem(i));
        }

        return returnWares;
    }

    /**
     * Deletes the ware from adapter and the database.
     * @param position The position of the ware in the list view.
     */
    private void deleteWare(int position) {
        Ware ware = getItem(position);
        Log.d(LOG_TAG, "Deleting ware " + "'" + ware.getName() + "'");
        remove(ware);

        updateWarePositions();

        mDatabaseLib.deleteWare(ware);
        mDatabaseLib.updateWares(getWares());
    }

    /**
     * Swaps the mark on the clicked ware and updates its position in the adapter and the database.
     * @param position The position of the ware in the list view.
     */
    private void swapMarked(int position) {
        Ware ware = getItem(position);

        Log.d(LOG_TAG, "Clicked ware " + "'" + ware.getName() + "'");

        if (ware.isMarked()) {
            remove(ware);
            insert(ware, 0);
            ware.setMarked(false);
        } else {
            remove(ware);
            insertAboveMarked(ware);
            ware.setMarked(true);
        }

        updateWarePositions();

        mDatabaseLib.updateWares(getWares());
    }

    /**
     * @param ware Inserts the item_ware into the unmarked wares as the last item
     */
    private void insertAboveMarked(Ware ware) {
        int index = getFirstMarkedIndex();

        if (index == getCount()) {
            add(ware);
        } else {
            insert(ware, index);
        }
    }

    /**
     * @return The index of the first marked item
     */
    private int getFirstMarkedIndex() {
        int listCount = getCount();

        for (int index = 0; index < listCount; index++) {
            if (getItem(index).isMarked()) {
                return index;
            }
        }

        return listCount;
    }

    /**
     * This method retrieves all the marked wares in the adapter and returns them.
     * @return Returns all the marked wares in the adapter.
     */
    private List<Ware> getMarkedWares() {
        ArrayList<Ware> markedWares = new ArrayList<>();

        for (int i = 0; i < getCount(); i++) {
            Ware ware = getItem(i);

            if (ware.isMarked()) {
                markedWares.add(ware);
            }
        }

        return markedWares;
    }

    /**
     * This method is used updates the list view position value on each ware.
     */
    private void updateWarePositions() {
        for(Ware ware : getWares()) {
            int position = getPosition(ware);
            ware.setPosition(position);
        }
    }

    /**
     * Private class used for the view holder pattern in getView method
     */
    private static class ViewHolder {
        TextView wareName;
        ImageButton wareQuantity;
        ImageButton wareSpecialOffer;
        RelativeLayout parentLayout;
    }
}
