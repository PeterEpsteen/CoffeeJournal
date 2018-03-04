package com.example.peter.coffeekeeper.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.peter.coffeekeeper.Controllers.MainActivity;
import com.example.peter.coffeekeeper.Models.BrewComment;
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

public class BrewCommentsAdapter extends RecyclerView.Adapter<BrewCommentsAdapter.ViewHolder> {
    private ArrayList<BrewComment> comments;
    private UserBrewListAdapter.UserBrewListListener listener;
    private Context mContext;
    public BrewCommentsAdapter(ArrayList<BrewComment> comments, Context context) {
        this.comments = comments;
        this.listener = listener;
        mContext = context;
    }

    public void setComments(ArrayList<BrewComment> comments) {
        this.comments = comments;
        notifyDataSetChanged();
    }

    @Override
    public BrewCommentsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_comment_item, parent, false);
        BrewCommentsAdapter.ViewHolder viewHolder = new BrewCommentsAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final BrewCommentsAdapter.ViewHolder holder, int position) {
        BrewComment comment = comments.get(position);
        holder.usernameTv.setText(comment.getUsername());
        holder.dateTv.setText(comment.getDate());
        holder.commentTv.setText(comment.getComment());
    }


    @Override
    public int getItemCount() {
        return comments.size();
    }

    public ArrayList<BrewComment> getComments() {
        return comments;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView commentTv, dateTv, usernameTv;
        public ViewHolder(View v) {
            super(v);
            commentTv = v.findViewById(R.id.comment_text_view);
            dateTv = v.findViewById(R.id.date_added_text_view);
            usernameTv = v.findViewById(R.id.username_text_view);
        }
    }

}
