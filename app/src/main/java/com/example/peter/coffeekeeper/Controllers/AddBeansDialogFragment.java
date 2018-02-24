package com.example.peter.coffeekeeper.Controllers;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.peter.coffeekeeper.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by peter on 1/26/18.
 */

public class AddBeansDialogFragment extends DialogFragment {

    /* The activity that creates an instance of this dialog fragment must
    * implement this interface in order to receive event callbacks.
    * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NoticeDialogListener {
        public void onConfirmBeans(DialogFragment dialog, List<LinearLayout> linearLayouts);
    }

    // Use this instance of the interface to deliver action events
    NoticeDialogListener mListener;
    LinearLayout container;
    List<LinearLayout> beanLinearList;


    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
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

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View v = inflater.inflate(R.layout.add_beans_dialog_fragment, null);
        container = v.findViewById(R.id.container);
        beanLinearList = new ArrayList<LinearLayout>();
        addBeanRow();
        builder.setView(v)
                // Add action buttons
                .setPositiveButton("Save Beans", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (mListener != null) {
                            mListener.onConfirmBeans(AddBeansDialogFragment.this, beanLinearList );
                        }
                    }
                });
        builder.setTitle("What beans are you using?");
        final Button addBeansBtn = v.findViewById(R.id.add_bean_button);
        addBeansBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBeanRow();
            }
        });
        return builder.create();
    }

    public void addBeanRow() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        LinearLayout beanRow = (LinearLayout) inflater.inflate(R.layout.add_beans_dialog_row, null, false);
        container.addView(beanRow);
        LinearLayout beanRowLinear = beanRow.findViewById(R.id.bean_row_linear_layout);
        beanLinearList.add(beanRowLinear);
        Log.i("Row", "Adding bean row...");
    }
}
