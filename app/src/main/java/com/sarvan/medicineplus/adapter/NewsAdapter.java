package com.sarvan.medicineplus.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sarvan.medicineplus.R;
import com.sarvan.medicineplus.others.Helper;
import com.sarvan.medicineplus.realm.NewsModel;

import java.util.ArrayList;

/**
 * DepartmentListAdapter Class Definition
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    // Context
    private Context context;
    // List of the news
    private ArrayList<NewsModel> newsList;
    // Firebase database ref
    private DatabaseReference databaseReference;

    // Constructor
    public NewsAdapter(Context context, ArrayList<NewsModel> newsList, DatabaseReference databaseReference) {
        this.newsList = newsList;
        this.context = context;
        this.databaseReference = databaseReference;

    }

    // Item View
    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_item_layout, parent, false);
        return new NewsViewHolder(view);
    }

    // Bind the value to UI
    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        NewsModel news = newsList.get(position);
        holder.newsTextView.setText(news.getNewsText());
//        holder.newsImage.setImageDrawable(context.getResources().getDrawable(R.drawable.medical_symbol));
        String date = Helper.getChatTimeStamp(news.getTime());
        holder.timeTv.setText(date);
        holder.posterNameTv.setText(news.getDrName());
    }

    // Total size of list view
    @Override
    public int getItemCount() {
        return newsList.size();
    }

    // ViewHolder Class Definition
    class NewsViewHolder extends RecyclerView.ViewHolder {
        private TextView newsTextView, timeTv, posterNameTv;
        private ImageView newsImage;

        // Constructor
        NewsViewHolder(View view) {
            super(view);
            newsTextView = (TextView) view.findViewById(R.id.news_tv);
            newsImage = (ImageView) view.findViewById(R.id.news_image);
            timeTv = (TextView) view.findViewById(R.id.post_time_tv);
            posterNameTv = (TextView) view.findViewById(R.id.news_post_user_name_tv);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (Helper.isAdminUsers()) {
                        deleteNewsDialog(getAdapterPosition()).show();
                    }
                    return false;
                }
            });
        }
    }

    /**
     * UpdateNewsList
     */
    public void updateNewslist() {
        newsList = Helper.getAllNewsFromRealm();
        notifyDataSetChanged();
    }

    /**
     * deleteNewsDialog method
     *
     * @return dialog box
     */
    private AlertDialog deleteNewsDialog(final int position) {
        return new AlertDialog.Builder(context)
                .setTitle("Do you want to delete this news?")
                .setNegativeButton(context.getResources().getString(R.string.negative_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Cancel Dialogue Box
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(context.getResources().getString(R.string.positive_button_string), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Query query = databaseReference.orderByChild("id").equalTo(newsList.get(position).getId());
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Helper.clearNewsFromRealm();
                                for (DataSnapshot snapshots : dataSnapshot.getChildren()) {
                                    snapshots.getRef().removeValue();
                                    Toast.makeText(context, "Deleted...", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.i("News Adapter", databaseError.getMessage());
                            }
                        });
                        updateNewslist();
                        // dialog box dismiss
                        dialog.dismiss();
                    }
                }).create();
    }
}
