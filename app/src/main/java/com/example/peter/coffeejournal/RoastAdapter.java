package com.example.peter.coffeejournal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by peter on 11/21/17.
 */

public class RoastAdapter extends RecyclerView.Adapter<RoastAdapter.ViewHolder> {
    private Context mContext;
    ArrayList<Roast> roastList;
    private static LayoutInflater inflater;
    private DBOperator dbOperator;
    private RoastAdapter adapter;

    @SuppressLint("ServiceCast")
    public RoastAdapter(Context c, ArrayList<Roast> list) {
        roastList = list;
        mContext = c;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.i("Brew", "Inside Roast Adapter");
        dbOperator = new DBOperator(mContext);
        adapter = this;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView roastNameTv, dateAddedTv;
        private ImageButton imageButton;

        public ViewHolder(View v) {
            super(v);
            roastNameTv = v.findViewById(R.id.roast_name_text_view);
            dateAddedTv = v.findViewById(R.id.date_added_text_view);
            imageButton = v.findViewById(R.id.roast_context_menu_button);

        }
    }

    @Override
    public long getItemId(int position) {
        return roastList.get(position).getID();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.roast_list_item, parent, false);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = v.findViewById(R.id.roast_name_text_view);
                String roastName = tv.getText().toString();
                TextView tv1 = v.findViewById(R.id.date_added_text_view);
                String date = tv1.getText().toString();
                Intent myIntent = new Intent(v.getContext(), RoastActivity.class);
                myIntent.putExtra("Name", roastName);
                myIntent.putExtra("Date", date);
                v.getContext().startActivity(myIntent);
                ((Activity)mContext).finish();
            }
        });
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    public ArrayList<Roast> getRoastList() {
        return roastList;
    }

    public void setRoastList(ArrayList<Roast> roastList) {
        this.roastList = roastList;
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(final RoastAdapter.ViewHolder holder, final int position) {
        Roast roast = new Roast();
        roast = roastList.get(holder.getAdapterPosition());
        holder.roastNameTv.setText(roast.getName());
        holder.dateAddedTv.setText(roast.getDate());
        final Roast finalRoast = roast;
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
                                deleteRoast(finalRoast.getName(), finalRoast.getDate(), holder.getAdapterPosition());
                                return true;
                            case R.id.menu_edit:
                                editRoast(finalRoast.getName(), finalRoast.getDate());
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

    public void deleteRoast(String name, String dateString, int position) {
        boolean returnR = dbOperator.deleteRoast(name, dateString);
        roastList.remove(position);
        adapter.notifyDataSetChanged();
    }

    public void editRoast(String name, String dateString) {
        Intent myIntent = new Intent(mContext, AddRoast.class);
        Log.i("Roast", "Launching edit of roast: " + name);
        myIntent.putExtra("Name", name);
        myIntent.putExtra("Date", dateString);
        ((Activity)mContext).startActivityForResult(myIntent, 1);
    }


    @Override
    public int getItemCount() {
        return roastList.size();
    }

}
