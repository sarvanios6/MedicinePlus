package com.sarvan.medicineplus.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sarvan.medicineplus.R;
import com.sarvan.medicineplus.others.Helper;
import com.sarvan.medicineplus.realm.CommentsModel;

import java.util.ArrayList;

/**
 * CommentsAdapter class definition
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder> {
    private Context context;
    private ArrayList<CommentsModel> comments;

    // Constructor
    public CommentsAdapter(Context context, ArrayList<CommentsModel> comments) {
        this.context = context;
        this.comments = comments;
    }

    @Override
    public CommentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comments_item_layout, parent, false);
        return new CommentsViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    @Override
    public void onBindViewHolder(CommentsViewHolder holder, int position) {
        if (comments.size() > 0) {
            holder.comentsText.setText(comments.get(position).getComments());
            holder.userName.setText(comments.get(position).getUserName());
            String timeStamp = Helper.getChatTimeStamp(comments.get(position).getTimeStamp());
            holder.commentsTime.setText(timeStamp);
        }
    }

    class CommentsViewHolder extends RecyclerView.ViewHolder {
        private TextView comentsText;
        private TextView commentsTime;
        private TextView userName;

        CommentsViewHolder(View view) {
            super(view);
            comentsText = (TextView) view.findViewById(R.id.comments_tv);
            commentsTime = (TextView) view.findViewById(R.id.time_tv);
            userName = (TextView) view.findViewById(R.id.user_name_tv);
        }
    }

    /**
     * update adapter
     *
     * @param commentsModel text
     */
    public void updateCommets(CommentsModel commentsModel) {
        comments.add(commentsModel);
        notifyDataSetChanged();
    }

    public void clearComments() {
        comments.clear();
    }
}
