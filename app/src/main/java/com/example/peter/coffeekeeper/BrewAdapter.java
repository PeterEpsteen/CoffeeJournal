package com.example.peter.coffeekeeper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by peter on 11/5/17.
 */

public class BrewAdapter extends RecyclerView.Adapter<BrewAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<BrewRecipe> brewList;
    private LayoutInflater inflater;
    private DBOperator dbOperator;
    private BrewAdapter adapter;

    @SuppressLint("ServiceCast")
    public BrewAdapter(Context c, ArrayList<BrewRecipe> list) {
        mContext = c;
        brewList = list;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dbOperator = new DBOperator(mContext);
        adapter = this;
    }

    public ArrayList<BrewRecipe> getBrewList() {
        return brewList;
    }

    public void setBrewList(ArrayList<BrewRecipe> brewList) {
        this.brewList = brewList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView brewNameTv, brewMethodTv;
        public ImageView brewMethodIv;
        ImageButton imageButton;

        public ViewHolder(View v) {
            super(v);
            brewNameTv = v.findViewById(R.id.brew_title_text_view);
            brewMethodTv = v.findViewById(R.id.brew_method_text_view);
            brewMethodIv = v.findViewById(R.id.brew_method_icon_image_view);
            imageButton = v.findViewById(R.id.brew_context_menu_button);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.brew_grid_item, parent, false);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = v.findViewById(R.id.brew_title_text_view);
                String brewName = tv.getText().toString();
                Intent myIntent = new Intent(v.getContext(), BrewRecipeActivity.class);
                myIntent.putExtra("Brew Name", brewName);
                v.getContext().startActivity(myIntent);
            }
        });
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final String brewName = brewList.get(holder.getAdapterPosition()).getName();
        final String brewMethod = brewList.get(holder.getAdapterPosition()).getBrewMethod();
        final String brewDate = brewList.get(holder.getAdapterPosition()).getDateAdded();
        final int imageID = brewList.get(holder.getAdapterPosition()).getIcon();
        holder.brewNameTv.setText(brewName);
        holder.brewMethodIv.setImageResource(imageID);
        holder.brewMethodTv.setText(brewMethod);
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(mContext, holder.imageButton);
                //inflating menu from xml resource
                popup.inflate(R.menu.brew_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_delete:
                                deleteBrew(brewName, holder.getAdapterPosition());
                                return true;
                            case R.id.menu_edit:
                                editBrew(brewName, brewDate);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                //displaying the popup
                popup.show();
            }
        });
    }

    public void deleteBrew(String brewName, int position) {
        boolean returnB = dbOperator.deleteBrew(brewName);
        brewList.remove(position);
        adapter.notifyDataSetChanged();
    }

    public void editBrew(String brewName, String brewDate) {
        Intent myIntent = new Intent(mContext, AddBrew.class);
        myIntent.putExtra("Brew Name", brewName);
        myIntent.putExtra("Brew Date", brewDate);
        ((Activity)mContext).startActivityForResult(myIntent, 1);
    }


    @Override
    public int getItemCount() {
        if(brewList == null) {
            brewList = dbOperator.getBrewRecipes();
        }
            return brewList.size();
    }

}
