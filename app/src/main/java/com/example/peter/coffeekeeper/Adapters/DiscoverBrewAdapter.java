package com.example.peter.coffeekeeper.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.peter.coffeekeeper.Controllers.MainActivity;
import com.example.peter.coffeekeeper.Models.BrewRecipe;
import com.example.peter.coffeekeeper.R;
import com.example.peter.coffeekeeper.RestClients.UserRestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by peter on 2/24/18.
 */

public class DiscoverBrewAdapter extends RecyclerView.Adapter<DiscoverBrewAdapter.ViewHolder> {
    private ArrayList<BrewRecipe> brews;
    private UserBrewListAdapter.UserBrewListListener listener;
    private Context mContext;
    public DiscoverBrewAdapter(ArrayList<BrewRecipe> brews, Context context) {
        this.brews = brews;
        this.listener = listener;
        mContext = context;
    }

    public void setBrews(ArrayList<BrewRecipe> brews) {
        this.brews = brews;
        notifyDataSetChanged();
    }

    @Override
    public DiscoverBrewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_discover_list_item, parent, false);
        DiscoverBrewAdapter.ViewHolder viewHolder = new DiscoverBrewAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final DiscoverBrewAdapter.ViewHolder holder, int position) {
        final BrewRecipe brew = brews.get(position);
        holder.pointsCountTv.setText(String.valueOf(brew.getPoints()));
        holder.titleTextView.setText(brew.getName());
        holder.usernameTv.setText(brew.getUserName());
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.dropdown.getVisibility() == View.VISIBLE) {
                    holder.dropdown.setVisibility(View.GONE);
                }
                else
                    holder.dropdown.setVisibility(View.VISIBLE);
            }
        });
        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeBrew(brew);
                holder.pointsCountTv.setText(String.valueOf(Integer.parseInt(holder.pointsCountTv.getText().toString())+1));
            }
        });
//
    }

    private void likeBrew(BrewRecipe brew) {
        SharedPreferences preferences = mContext.getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
        int userID = preferences.getInt(MainActivity.PREFS_USER_ID, 0);
        RequestParams params = new RequestParams();
        UserRestClient.put(mContext, "brews/like/" + brew.getBrewID()+"/"+userID, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    Toast.makeText(mContext, response.get("message").toString(), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                try {
                    Toast.makeText(mContext, errorResponse.get("message").toString(), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return brews.size();
    }

    public ArrayList<BrewRecipe> getBrews() {
        return brews;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView, pointsCountTv, commentsCountTv, usernameTv;
        public ConstraintLayout container, dropdown;
        Button likeButton;
        public ViewHolder(View v) {
            super(v);
            likeButton = v.findViewById(R.id.like_button);
            usernameTv = v.findViewById(R.id.username_text_view);
            this.container = v.findViewById(R.id.main_container);
            this.dropdown = v.findViewById(R.id.dropdown_container);
            this.titleTextView = v.findViewById(R.id.recipe_title_textview);
            this.commentsCountTv = v.findViewById(R.id.comments_count);
            this.pointsCountTv= v.findViewById(R.id.points_count);
        }
    }

//    public interface UserBrewListListener {
//        void deleteBrew(BrewRecipe brew);
//    }
}
