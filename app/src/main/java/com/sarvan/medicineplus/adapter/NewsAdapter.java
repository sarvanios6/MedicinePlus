package com.sarvan.medicineplus.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sarvan.medicineplus.R;

import java.util.ArrayList;

/**
 * DepartmentListAdapter Class Definition
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private Context context;
    private ArrayList<String> NewsList;

    public NewsAdapter(Context context, ArrayList<String> doctorNameList) {
        this.NewsList = doctorNameList;
        this.context = context;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_item_layout, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        holder.newsTextView.setText(NewsList.get(position));
        holder.newsImage.setImageDrawable(context.getResources().getDrawable(R.drawable.medical_symbol));
    }

    @Override
    public int getItemCount() {
        return NewsList.size();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {
        private TextView newsTextView;
        private ImageView newsImage;

        NewsViewHolder(View view) {
            super(view);
            newsTextView = (TextView) view.findViewById(R.id.news_tv);
            newsImage = (ImageView) view.findViewById(R.id.news_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, newsTextView.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
