package com.example.peter.coffeekeeper.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.peter.coffeekeeper.Models.BrewRecipe;
import com.example.peter.coffeekeeper.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by root on 2/22/18.
 */

public class UserBrewListAdapter extends RecyclerView.Adapter<UserBrewListAdapter.ViewHolder> {
    private ArrayList<BrewRecipe> brews;
    private UserBrewListListener listener;
    public UserBrewListAdapter(ArrayList<BrewRecipe> brews, UserBrewListListener listener) {
        this.brews = brews;
        this.listener = listener;
    }

    public void setBrews(ArrayList<BrewRecipe> brews) {
        this.brews = brews;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_user_brew_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BrewRecipe brew = brews.get(position);
        holder.dateTv.setText(brew.getDateAdded());
        holder.brewTitleTextView.setText(brew.getName());
    }

    @Override
    public int getItemCount() {
        return brews.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView brewTitleTextView, commentCountTv, pointCountTv, dateTv;
        public ViewHolder(View v) {
            super(v);
            this.brewTitleTextView = v.findViewById(R.id.brew_title_text_view);
            this.commentCountTv = v.findViewById(R.id.comments_count_text_view);
            this.pointCountTv = v.findViewById(R.id.points_text_view);
            this.dateTv = v.findViewById(R.id.date_added_text_view);
        }
    }

    public interface UserBrewListListener {

    }
}
