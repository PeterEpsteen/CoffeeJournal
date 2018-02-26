package com.example.peter.coffeekeeper.Controllers;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.peter.coffeekeeper.Adapters.UploadBrewAdapter;
import com.example.peter.coffeekeeper.Database.DBOperator;
import com.example.peter.coffeekeeper.Models.BrewRecipe;
import com.example.peter.coffeekeeper.R;
import com.example.peter.coffeekeeper.RestClients.UserRestClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by peter on 2/21/18.
 */

public class UploadBrewDialogFragment extends DialogFragment implements UploadBrewAdapter.UploadBrewInterface {

    /* The activity that creates an instance of this dialog fragment must
    * implement this interface in order to receive event callbacks.
    * Each method passes the DialogFragment in case the host needs to query it. */


    // Use this instance of the interface to deliver action events


    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface

    }


    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View v = inflater.inflate(R.layout.upload_brew_dialog, null);
        RecyclerView brewRv = v.findViewById(R.id.brew_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        brewRv.setLayoutManager(layoutManager);
        DBOperator dbOperator = new DBOperator(getActivity());
        ArrayList<BrewRecipe> brews = dbOperator.getBrewRecipes();
        UploadBrewAdapter adapter = new UploadBrewAdapter(this, brews);
        brewRv.setAdapter(adapter);
        builder.setView(v);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        });

        builder.setTitle("Tap a brew to upload.");
        return builder.create();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if(activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener)activity).onDismiss(dialog);
        }
    }

    @Override
    public void upload(BrewRecipe brew) {
        Toast.makeText(getActivity().getApplicationContext(), "Uploading brew: "+ brew.getName(), Toast.LENGTH_LONG).show();
        int userID = getActivity().getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE).getInt(MainActivity.PREFS_USER_ID, 0);
        if (userID == 0) {

        }
        else {
            UserRestClient.postBrew(brew, getActivity(), new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    try {
                        Toast.makeText(getActivity(), response.get("message").toString(), Toast.LENGTH_LONG).show();
                        dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    try {
                        if(errorResponse.get("message").toString().contains("duplicate")) {
                            Toast.makeText(getActivity(), "You already uploaded a brew with this name.", Toast.LENGTH_LONG).show();
                        }
                        else
                        Toast.makeText(getActivity(), errorResponse.get("message").toString(), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            });
        }

    }
}
