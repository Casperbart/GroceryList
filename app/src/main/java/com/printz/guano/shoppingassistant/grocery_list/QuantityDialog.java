package com.printz.guano.shoppingassistant.grocery_list;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.printz.guano.shoppingassistant.R;

public class QuantityDialog extends DialogFragment {

    private static final String LOG_TAG = QuantityDialog.class.getSimpleName();

    public static final String WARE_LIST_POSITION = "wareListPosition";
    public static final String WARE_VIEW_POSITION = "wareViewPosition";
    public static final String WARE_AMOUNT = "wareAmount";
    public static final String WARE_TYPE = "wareType";

    private QuantityDialogListener mQuantityDialogListener;

    private String amount;
    private String type;

    private final String[] amounts = {
            "-", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
            "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
            "30", "40", "50", "60", "70", "80", "90", "100", "150", "200",
            "250", "300", "350", "400", "450", "500", "550", "600", "650", "700",
            "750", "800", "850", "900", "950", "1000"
    };
    private final String[] types = {
            "-", "stk", "kg", "gram", "liter", "poser", "pakker", "bundt", "dåser", "glas", "flasker"
    };

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();

        View dialogView = layoutInflater.inflate(R.layout.dialog_quantity, null);
        final ListView amountListView = (ListView) dialogView.findViewById(R.id.listViewAmount);
        ListView typeListView = (ListView) dialogView.findViewById(R.id.listViewType);

        final ArrayAdapter<String> amountAdapter = new ArrayAdapter<>(getActivity(), R.layout.item_quantity, amounts);
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(getActivity(), R.layout.item_quantity, types);

        amountListView.setAdapter(amountAdapter);
        typeListView.setAdapter(typeAdapter);

        final int position = getArguments().getInt(WARE_LIST_POSITION);
        amount = getArguments().getString(WARE_AMOUNT);
        type = getArguments().getString(WARE_TYPE);

        //TODO: handle dialog position
        int[] viewPositions = getArguments().getIntArray(WARE_VIEW_POSITION);

        amountListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                amount = (String)parent.getItemAtPosition(position);
            }
        });

        typeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                type = (String)parent.getItemAtPosition(position);
            }
        });

        dialogBuilder.setView(dialogView).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mQuantityDialogListener.onQuantityDialogFinished(position, amount, type);
            }
        });

        return dialogBuilder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mQuantityDialogListener = (QuantityDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    " must implement QuantityDialogListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        window.setLayout(450, 700);
        window.setGravity(Gravity.END);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);

        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        Log.d(LOG_TAG, "dismissed the quantity dialog");
        super.onDismiss(dialog);
    }
}
