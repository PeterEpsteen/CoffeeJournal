package com.example.peter.coffeekeeper.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.peter.coffeekeeper.Models.BrewRecipe;
import com.example.peter.coffeekeeper.R;

import java.util.ArrayList;

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
//
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
        public ViewHolder(View v) {
            super(v);
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
