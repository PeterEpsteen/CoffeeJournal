package com.example.peter.coffeekeeper;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by peter on 1/26/18.
 */

public class QuickStepTempaturesDialogFragment extends DialogFragment {
    public interface NoticeDialogListener {
        public void onConfirmTempatures(RoastStep step);
    }

    // Use this instance of the interface to deliver action events
    QuickStepTempaturesDialogFragment.NoticeDialogListener mListener;
    RoastStep step;
    String comment, time;
    int roastTemp, beanTemp;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (QuickStepTempaturesDialogFragment.NoticeDialogListener) activity;
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
        step = new RoastStep();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View v = inflater.inflate(R.layout.step_tempatures_dialog, null);
        builder.setView(v)
                // Add action buttons
                .setPositiveButton("Save Name", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (mListener != null) {
                            EditText beanEdit = v.findViewById(R.id.bean_tempature_edit);
                            EditText roasterEdit = v.findViewById(R.id.roast_tempature_edit);
                            beanTemp = (beanEdit.getText().toString().isEmpty()) ? 0 : Integer.parseInt(beanEdit.getText().toString());
                            roastTemp = (roasterEdit.getText().toString().isEmpty()) ? 0 : Integer.parseInt(roasterEdit.getText().toString());
                            step.setBeanTemp(beanTemp);
                            step.setTemp(roastTemp);
                            if(!getArguments().getString("comment").isEmpty())
                                step.setComment(getArguments().getString("comment"));
                            mListener.onConfirmTempatures(step);
                        }
                    }
                });
        builder.setTitle("Current Temperatures");
        return builder.create();
    }


}
