package com.nader.starfeeds.ui.sections;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;


/**
 * Created by Nader on 14-May-17.
 */

public class SimpleDialogFragment extends DialogFragment {
    private OnDialogClicked listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setMessage("Are you sure you want to unfollow?")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onConfirmClicked();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onCancelClicked();
                    }
                }).create();
    }

    public void setListener(OnDialogClicked listener){
        this.listener = listener;
    }

    public interface OnDialogClicked {
        void onConfirmClicked();
        void onCancelClicked();
    }
}