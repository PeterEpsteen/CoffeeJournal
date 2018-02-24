package com.example.peter.coffeekeeper.Controllers;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

import com.example.peter.coffeekeeper.R;

/**
 * Created by peter on 1/26/18.
 */

public class QuickStepDialogFragment extends DialogFragment {
    public interface NoticeDialogListener {
        public void onConfirmStep(String comment);
    }

    // Use this instance of the interface to deliver action events
    QuickStepDialogFragment.NoticeDialogListener mListener;
    String comment;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (QuickStepDialogFragment.NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        comment = "";
        builder.setTitle("Choose step type").setSingleChoiceItems(R.array.steps_array, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                 String[] arr = getResources().getStringArray(R.array.steps_array);
                 comment = arr[i];
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        }).setPositiveButton("Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (mListener != null) {
                    mListener.onConfirmStep(comment);
                }
            }
        });

        return builder.create();
    }

    private void showNextView() {

    }
}
