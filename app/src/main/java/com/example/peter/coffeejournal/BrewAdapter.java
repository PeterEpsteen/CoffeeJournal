package com.example.peter.coffeejournal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.peter.coffeejournal.BrewRecipe;
import com.example.peter.coffeejournal.R;

import java.util.ArrayList;

/**
 * Created by peter on 11/5/17.
 */

public class BrewAdapter extends RecyclerView.Adapter<BrewAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<BrewRecipe> brewList;
    private LayoutInflater inflater;

    @SuppressLint("ServiceCast")
    public BrewAdapter(Context c, ArrayList<BrewRecipe> list) {
        mContext = c;
        brewList = list;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ArrayList<BrewRecipe> getBrewList() {
        return brewList;
    }

    public void setBrewList(ArrayList<BrewRecipe> brewList) {
        this.brewList = brewList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView brewNameTv, brewMethodTv;
        public ImageView brewMethodIv;

        public ViewHolder(View v) {
            super(v);
            brewNameTv = v.findViewById(R.id.brew_title_text_view);
            brewMethodTv = v.findViewById(R.id.brew_method_text_view);
            brewMethodIv = v.findViewById(R.id.brew_method_icon_image_view);
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String brewName = brewList.get(position).getName();
        final String brewMethod = brewList.get(position).getBrewMethod();
        final int imageID = brewList.get(position).getIcon();
        holder.brewNameTv.setText(brewName);
        holder.brewMethodIv.setImageResource(imageID);
        holder.brewMethodTv.setText(brewMethod);
    }


    @Override
    public int getItemCount() {
        return brewList.size();
    }

}
