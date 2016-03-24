package com.printz.guano.shoppingassistant.edit_list;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.printz.guano.shoppingassistant.R;

public class ShoppingListDialog extends DialogFragment {

    private static final String LOG_TAG = ShoppingListDialog.class.getSimpleName();
    private LayoutInflater mLayoutInflater;
    public static final String DIALOG_TYPE = "dialogType";
    public static final String DELETE_ALL_WARES = "deleteAllWares";
    public static final String DELETE_ALL_MARKED = "deleteAllMarked";

    @Override
    public Dialog onCreateDialog(@NonNull Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        mLayoutInflater = getActivity().getLayoutInflater();
        View dialogView = mLayoutInflater.inflate(R.layout.dialog_list, null);

        String commandType = getArguments().getString(DIALOG_TYPE);

        if (commandType.equals(DELETE_ALL_WARES)) {
            TextView dialogMessage = (TextView) dialogView.findViewById(R.id.textViewDialogShoppingList);
            dialogMessage.setText(R.string.delete_all_wares_dialog);
            dialogBuilder.setView(dialogView).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.d(LOG_TAG, "Clicked OK from shopping list dialog");
                    ShoppingListDialogListener caller = (ShoppingListDialogListener) getActivity();
                    caller.onDialogFinishes(DELETE_ALL_WARES, true);
                }
            })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(LOG_TAG, "Clicked CANCEL from shopping list dialog");
                        }
                    });

        } else if (commandType.equals(DELETE_ALL_MARKED)) {
            TextView dialogMessage = (TextView) dialogView.findViewById(R.id.textViewDialogShoppingList);
            dialogMessage.setText(R.string.delete_all_marked_dialog);
            dialogBuilder.setView(dialogView).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.d(LOG_TAG, "Clicked OK from shopping list dialog");
                    ShoppingListDialogListener caller = (ShoppingListDialogListener) getActivity();
                    caller.onDialogFinishes(DELETE_ALL_MARKED, true);
                }
            })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(LOG_TAG, "Clicked CANCEL from shopping list dialog");
                        }
                    });

        } else {
            Log.d(LOG_TAG, "Unknown dialog type: " + commandType);
        }

        return dialogBuilder.create();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        Log.d(LOG_TAG, "dismissed the dialog");
        super.onDismiss(dialog);
    }
}
