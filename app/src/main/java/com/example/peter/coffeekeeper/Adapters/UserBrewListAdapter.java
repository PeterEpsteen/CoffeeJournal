package com.example.peter.coffeekeeper.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
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
    private Context mContext;
    public UserBrewListAdapter(ArrayList<BrewRecipe> brews, UserBrewListListener listener, Context context) {
        this.brews = brews;
        this.listener = listener;
        mContext = context;
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
        final BrewRecipe brew = brews.get(position);
        holder.dateTv.setText(brew.getDateAdded());
        holder.brewTitleTextView.setText(brew.getName());
        holder.container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle(brew.getName());
                builder.setItems(R.array.user_brew_list_options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.deleteBrew(brew);
                    }

                });
                builder.show();
                return true;
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
        public TextView brewTitleTextView, commentCountTv, pointCountTv, dateTv;
        public ConstraintLayout container;
        public ViewHolder(View v) {
            super(v);
            this.container = v.findViewById(R.id.container);
            this.brewTitleTextView = v.findViewById(R.id.brew_title_text_view);
            this.commentCountTv = v.findViewById(R.id.comments_count_text_view);
            this.pointCountTv = v.findViewById(R.id.points_text_view);
            this.dateTv = v.findViewById(R.id.date_added_text_view);
        }
    }

    public interface UserBrewListListener {
        void deleteBrew(BrewRecipe brew);
    }
}
