package com.example.peter.coffeekeeper.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.peter.coffeekeeper.Models.BrewRecipe;
import com.example.peter.coffeekeeper.R;

import java.util.ArrayList;

/**
 * Created by root on 2/22/18.
 */

public class UploadBrewAdapter extends RecyclerView.Adapter<UploadBrewAdapter.ViewHolder> {
    private ArrayList<BrewRecipe> brews;
    private UploadBrewInterface listener;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.upload_brew_item, parent, false);

        ViewHolder vh = new ViewHolder(v, listener, brews);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.brewTitle.setText(brews.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return brews.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView brewTitle, brewDate;
        private ArrayList<BrewRecipe> brews;
        private UploadBrewInterface listener;
        public ViewHolder(View v, UploadBrewInterface listener, ArrayList<BrewRecipe> brews) {
            super(v);
            this.listener = listener;
            this.brews = brews;
            v.setOnClickListener(this);
            brewTitle = v.findViewById(R.id.brew_title_text_view);
        }

        @Override
        public void onClick(View view) {
            listener.upload(brews.get(getAdapterPosition()));
        }
    }

    public UploadBrewAdapter(UploadBrewInterface listener, ArrayList<BrewRecipe> brews) {
        this.brews = brews;
        this.listener = listener;
    }
    public interface UploadBrewInterface{
         void upload(BrewRecipe brew);
    }
}
