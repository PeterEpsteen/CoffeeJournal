package com.example.peter.coffeejournal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by peter on 11/21/17.
 */

public class RoastAdapter extends BaseAdapter {
    private Context mContext;
    ArrayList<Roast> roastList;
    private static LayoutInflater inflater;

    @SuppressLint("ServiceCast")
    public RoastAdapter(Context c, ArrayList<Roast> list) {
        roastList = list;
        mContext = c;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.i("Brew", "Inside Roast Adapter");

    }

    @Override
    public int getCount() {
        return roastList.size();
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
        if (convertView == null){
            // if it's not recycled, initialize some attributes
            convertView = LayoutInflater.from(mContext).inflate(R.layout.roast_list_item, parent, false);}

        TextView roastNameTv = (TextView) convertView.findViewById(R.id.roast_name_text_view);
        TextView dateAddedTv = (TextView) convertView.findViewById(R.id.date_added_text_view);


        Roast roast = new Roast();
        roast = roastList.get(position);
        Log.i("Brew", "Name " + position + ": " + roast.getName());
        roastNameTv.setText(roast.getName());
        dateAddedTv.setText(roast.getDate());

//        recipe = brewList.get(position);
//        brewNameTv.setText(recipe.getName());
//        brewMethodTv.setText(recipe.getBrewMethod());
//        brewMethodIv.setImageResource(recipe.getIcon());



        return convertView;
    }
}
