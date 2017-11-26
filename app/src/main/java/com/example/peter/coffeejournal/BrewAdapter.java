package com.example.peter.coffeejournal;

import android.annotation.SuppressLint;
import android.content.Context;
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

public class BrewAdapter extends BaseAdapter {
    private Context mContext;
    ArrayList<BrewRecipe> brewList;
    private static LayoutInflater inflater;

    @SuppressLint("ServiceCast")
    public BrewAdapter(Context c, ArrayList<BrewRecipe> list) {
        mContext = c;
        brewList = list;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return brewList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            convertView = LayoutInflater.from(mContext).inflate(R.layout.brew_grid_item, parent, false);
        }

        TextView brewNameTv = (TextView) convertView.findViewById(R.id.brew_title_text_view);
        TextView brewMethodTv = (TextView) convertView.findViewById(R.id.brew_method_text_view);
        ImageView brewMethodIv = (ImageView) convertView.findViewById(R.id.brew_method_icon_image_view);

        BrewRecipe recipe = new BrewRecipe();

        recipe = brewList.get(position);
        brewNameTv.setText(recipe.getName());
        brewMethodTv.setText(recipe.getBrewMethod());
        brewMethodIv.setImageResource(recipe.getIcon());



        return convertView;

    }
}
