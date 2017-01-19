package com.sarvan.medicineplus.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sarvan.medicineplus.R;
import com.sarvan.medicineplus.activity.AskDoctorActivity;
import com.sarvan.medicineplus.others.Helper;
import com.sarvan.medicineplus.realm.ChatMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * ConversationAdapter Class Definition
 */

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder> {
    private ArrayList<ChatMessage> messages;
    private Context context;
    private String timeStamp;
    private String departmentName;

    public ConversationAdapter(Context context, ArrayList<ChatMessage> messages, String departmentName) {
        this.messages = messages;
        this.context = context;
        this.departmentName = departmentName;
    }

    @Override
    public ConversationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_item, parent, false);
        return new ConversationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ConversationViewHolder holder, int position) {
        ChatMessage message = messages.get(position);
        if (message.getMessage() != null) {
            holder.chatMessageTv.setText(message.getMessage());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy hh:mm");
            Date resultDate = new Date(message.getMessageDate());
            timeStamp = simpleDateFormat.format(resultDate);
            holder.dateTv.setText(timeStamp);
        } else {
            holder.chatMessageTv.setText("");
            holder.dateTv.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class ConversationViewHolder extends RecyclerView.ViewHolder {
        private TextView chatMessageTv;
        private TextView dateTv;

        ConversationViewHolder(View itemView) {
            super(itemView);
            chatMessageTv = (TextView) itemView.findViewById(R.id.chat_message_tv);
            dateTv = (TextView) itemView.findViewById(R.id.date_show_tv);
        }
    }
}
