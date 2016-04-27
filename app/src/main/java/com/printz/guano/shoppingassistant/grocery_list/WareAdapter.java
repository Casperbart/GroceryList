package com.printz.guano.shoppingassistant.grocery_list;

import android.content.Context;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.printz.guano.shoppingassistant.R;
import com.printz.guano.shoppingassistant.database.Contract;
import com.printz.guano.shoppingassistant.database.DatabaseLib;
import com.printz.guano.shoppingassistant.misc.OnSwipeTouchListener;

import java.util.ArrayList;
import java.util.List;

class WareAdapter extends ArrayAdapter<Ware> {

    private static final String LOG_TAG = WareAdapter.class.getSimpleName();
    private LayoutInflater mLayoutInflater;
    private FragmentManager mFragmentManager;
    private DatabaseLib mDatabaseLib;

    /**
     * My way of keeping track of index positions in database
     */
    private static int FIRST_INDEX;

    public WareAdapter(Context context, int resource) {
        super(context, resource);

        mFragmentManager = ((FragmentActivity)context).getSupportFragmentManager();
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDatabaseLib = new DatabaseLib(context.getContentResolver());
        FIRST_INDEX = 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final Ware ware = getItem(position);

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.item_ware, parent, false);

            holder.wareName = (TextView) convertView.findViewById(R.id.textViewWareName);
            holder.viewQuantity = (LinearLayout) convertView.findViewById(R.id.quantityView);
            holder.wareQuantity = (ImageButton) convertView.findViewById(R.id.imageButtonQuantity);
            holder.wareAmount = (TextView) convertView.findViewById(R.id.textWareAmount);
            holder.wareType = (TextView) convertView.findViewById(R.id.textWareType);
            holder.wareSpecialOffer = (ImageButton) convertView.findViewById(R.id.imageButtonSpecialOffer);
            holder.parentLayout = (RelativeLayout) convertView.findViewById(R.id.relativeLayoutWare);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.wareName.setText(ware.getName());
        if (ware.isMarked()) {
            setViewMark(holder, Paint.STRIKE_THRU_TEXT_FLAG, ContextCompat.getColor(getContext(), R.color.colorMarked));
        } else {
            setViewMark(holder, 0, ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        }

        if(ware.isAmountSet()) {
            showAmountType(holder);

            holder.wareAmount.setText(ware.getAmount());
            holder.wareType.setText(ware.getType());
        } else {
            showQuantityImage(holder);
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

        holder.viewQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "Clicked quantity button");

                QuantityDialog quantityDialog = new QuantityDialog();

                int[] viewLocations = new int[2];
                holder.parentLayout.getLocationOnScreen(viewLocations);

                Bundle args = new Bundle();
                args.putInt(QuantityDialog.WARE_LIST_POSITION, position);
                args.putString(QuantityDialog.WARE_AMOUNT, ware.getAmount());
                args.putString(QuantityDialog.WARE_TYPE, ware.getType());
                args.putIntArray(QuantityDialog.WARE_VIEW_POSITION, viewLocations);
                quantityDialog.setArguments(args);

                quantityDialog.show(mFragmentManager, "ware-quantity");
            }
        });

        return convertView;
    }

    /**
     * Inserts Ware into the adapter and inserts it into the database.
     *
     * @param ware The ware to insert.
     */
    public void insertWare(Ware ware) {
        Log.d(LOG_TAG, "Adding new ware '" + ware.getName() + "'");

        insertAboveMarked(ware);

        Uri wareUri = mDatabaseLib.insertWare(ware);

        String wareId = Contract.Ware.getWareId(wareUri);
        ware.setId(Integer.valueOf(wareId));
    }

    /**
     * Deletes the ware from adapter and the database.
     *
     * @param position The position of the ware in the list view.
     */
    private void deleteWare(int position) {
        Ware ware = getItem(position);
        Log.d(LOG_TAG, "Deleting ware " + "'" + ware.getName() + "'");
        remove(ware);

        mDatabaseLib.deleteWare(ware);
    }

    public void setQuantity(int position, String amount, String type) {
        Ware ware = getItem(position);

        ware.setAmount(amount);
        ware.setType(type);
        notifyDataSetChanged();
        mDatabaseLib.updateWare(ware);
    }

    /**
     * Removes all wares in adapter and the database.
     * @return Returns the number of wares removed.
     */
    public int deleteAllWares() {
        int deleteCount = mDatabaseLib.deleteWares(getWares());
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

        return deleteCount;
    }

    /**
     * Swaps the mark on the clicked ware and updates its position in the adapter and the database.
     *
     * @param position The position of the ware in the list view.
     */
    private void swapMarked(int position) {
        Ware ware = getItem(position);

        Log.d(LOG_TAG, "Clicked ware " + "'" + ware.getName() + "'");

        if (ware.isMarked()) {
            remove(ware);
            insert(ware, 0);
            ware.setMarked(false);
            ware.setPosition(--FIRST_INDEX); // required to maintain logical position
        } else {
            remove(ware);
            insertAboveMarked(ware);
            ware.setMarked(true);
        }

        mDatabaseLib.updateWare(ware);
    }

    /**
     * @param ware Inserts the item_ware into the unmarked wares as the last item
     */
    private void insertAboveMarked(Ware ware) {
        int index = getFirstMarkedIndex();

        ware.setPosition(index);

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
     * Populates the wares in the adapter.
     * @param wares The list to populate the adapter with
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
     *
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
     * This method creates a list of all the marked wares and returns them.
     *
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

    private void showQuantityImage(ViewHolder holder) {
        holder.wareQuantity.setVisibility(View.VISIBLE);
        holder.wareAmount.setVisibility(View.GONE);
        holder.wareType.setVisibility(View.GONE);
    }

    private void showAmountType(ViewHolder holder) {
        holder.wareQuantity.setVisibility(View.GONE);
        holder.wareAmount.setVisibility(View.VISIBLE);
        holder.wareType.setVisibility(View.VISIBLE);
    }

    private void setViewMark(ViewHolder holder, int flags, int color) {
        holder.wareName.setPaintFlags(flags);
        holder.wareName.setTextColor(color);
        holder.wareQuantity.setColorFilter(color);
        holder.wareAmount.setTextColor(color);
        holder.wareType.setTextColor(color);
        holder.wareSpecialOffer.setColorFilter(color);
    }

    /**
     * Private class used for the view holder pattern in getView method
     */
    private static class ViewHolder {
        TextView wareName;
        LinearLayout viewQuantity;
        ImageButton wareQuantity;
        TextView wareAmount;
        TextView wareType;
        ImageButton wareSpecialOffer;
        RelativeLayout parentLayout;
    }
}
