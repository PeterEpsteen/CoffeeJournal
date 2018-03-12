package com.example.peter.coffeekeeper.Controllers;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

/**
 * Created by peter on 3/10/18.
 */

public class UpgradeDialogFragment extends android.support.v4.app.DialogFragment {
    public interface PurchaseListener {
        public void onConfirmPurchase();
    }

    PurchaseListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (UpgradeDialogFragment.PurchaseListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " Must implement NoticeDialogListener" );
        }
        //check if interface is implemented
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setTitle("Upgrade to Pro!").setMessage("Upgrading to pro will disable ads, and give users " +
                "access to pro features once released. \n\nIn the next few weeks, social features will be added like uploading brews, " +
                "discovering new recipes, and commenting/voting on the best recipes. Think reddit for coffee recipes! " +
                "\n\nAlso, upgrading to pro supports the continual development " +
                "of this app, and is greatly appreciated!").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).setPositiveButton("Upgrade", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mListener != null) {
                    mListener.onConfirmPurchase();
                }
            }
        });
        return builder.create();

    }
}
