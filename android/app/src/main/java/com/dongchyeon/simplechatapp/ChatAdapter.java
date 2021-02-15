package com.dongchyeon.simplechatapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ChatItem> items = new ArrayList<ChatItem>();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (viewType == ChatType.CENTER_CONTENT) {
            view = inflater.inflate(R.layout.chat_center_item, parent, false);
            return new CenterViewHolder(view);
        } else if (viewType == ChatType.LEFT_CONTENT) {
            view = inflater.inflate(R.layout.chat_left_item, parent, false);
            return new LeftViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.chat_right_item, parent, false);
            return new RightViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof CenterViewHolder) {
            ChatItem item = items.get(position);
            ((CenterViewHolder) viewHolder).setItem(item);
        }
        else if (viewHolder instanceof LeftViewHolder) {
            ChatItem item = items.get(position);
            ((LeftViewHolder) viewHolder).setItem(item);
        }
        else {
            ChatItem item = items.get(position);
            ((RightViewHolder) viewHolder).setItem(item);
        }
    }

    public int getItemCount() {
        return items.size();
    }

    public void addItem(ChatItem item){
        items.add(item);
        notifyDataSetChanged();
    }

    public void setItems(ArrayList<ChatItem> items){ this.items = items; }

    public ChatItem getItem(int position){
        return items.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getViewType();
    }

    public class CenterViewHolder extends RecyclerView.ViewHolder{
        TextView contentText;

        public CenterViewHolder(View itemView) {
            super(itemView);

            contentText = itemView.findViewById(R.id.content_text);
        }

        public void setItem(ChatItem item){
            contentText.setText(item.getContent());
        }
    }

    public class LeftViewHolder extends RecyclerView.ViewHolder {
        TextView nameText;
        TextView contentText;
        TextView sendTimeText;

        public LeftViewHolder(View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.name_text);
            contentText = itemView.findViewById(R.id.msg_text);
            sendTimeText = itemView.findViewById(R.id.send_time_text);
        }

        public void setItem(ChatItem item){
            nameText.setText(item.getName());
            contentText.setText(item.getContent());
            sendTimeText.setText(item.getSendTime());
        }
    }

    public class RightViewHolder extends RecyclerView.ViewHolder{
        TextView contentText;
        TextView sendTimeText;

        public RightViewHolder(View itemView) {
            super(itemView);

            contentText = itemView.findViewById(R.id.msg_text);
            sendTimeText = itemView.findViewById(R.id.send_time_text);
        }

        public void setItem(ChatItem item){
            contentText.setText(item.getContent());
            sendTimeText.setText(item.getSendTime());
        }
    }
}
