package com.printz.guano.shoppingassistant.grocery_list;

import android.app.Activity;
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

    public static final String DIALOG_TYPE = "dialogType";
    public static final String DELETE_ALL_WARES = "deleteAll";
    public static final String DELETE_ALL_MARKED = "deleteMarked";

    private OptionsDialogListener mOptionsDialogListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@NonNull Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.dialog_list, null);

        String commandType = getArguments().getString(DIALOG_TYPE);

        if (commandType != null) {
            switch (commandType) {
                case DELETE_ALL_WARES: {
                    TextView dialogMessage = (TextView) dialogView.findViewById(R.id.textViewDialogShoppingList);
                    dialogMessage.setText(R.string.delete_all_wares_dialog);
                    dialogBuilder.setView(dialogView).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(LOG_TAG, "Clicked OK from shopping list dialog");
                            mOptionsDialogListener.onOptionsDialogFinished(DELETE_ALL_WARES, true);
                        }
                    })
                            .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.d(LOG_TAG, "Clicked CANCEL from shopping list dialog");
                                    mOptionsDialogListener.onOptionsDialogFinished(DELETE_ALL_WARES, false);
                                }
                            });

                    break;
                }
                case DELETE_ALL_MARKED: {
                    TextView dialogMessage = (TextView) dialogView.findViewById(R.id.textViewDialogShoppingList);
                    dialogMessage.setText(R.string.delete_all_marked_dialog);
                    dialogBuilder.setView(dialogView).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(LOG_TAG, "Clicked OK from shopping list dialog");
                            mOptionsDialogListener.onOptionsDialogFinished(DELETE_ALL_MARKED, true);
                        }
                    })
                            .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.d(LOG_TAG, "Clicked CANCEL from shopping list dialog");
                                    mOptionsDialogListener.onOptionsDialogFinished(DELETE_ALL_MARKED, false);
                                }
                            });

                    break;
                }
                default:
                    Log.d(LOG_TAG, "Unknown dialog type: " + commandType);
                    break;
            }
        }

        return dialogBuilder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mOptionsDialogListener = (OptionsDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OptionsDialogListener");
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        Log.d(LOG_TAG, "dismissed the options dialog");
        super.onDismiss(dialog);
    }
}
