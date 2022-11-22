package com.lacorp.simple_chat_app.presentation.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lacorp.simple_chat_app.domain.entities.Message;
import com.lacorp.simple_chat_app.databinding.ItemMessageReceiveBinding;
import com.lacorp.simple_chat_app.databinding.ItemMessageSentBinding;

import java.util.List;

public class ChatMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Message> chatMessageList;
    private final String user_id;

    public ChatMessageAdapter(List<Message> chatMessageList, String user_id) {
        this.chatMessageList = chatMessageList;
        this.user_id = user_id;
    }

    static class SentViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvMessage;

        SentViewHolder(@NonNull ItemMessageSentBinding itemMessageSentBinding) {
            super(itemMessageSentBinding.getRoot());

            tvMessage = itemMessageSentBinding.tvSentMessage;
        }

    }

    static class ReceiveViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvMessage;

        ReceiveViewHolder(@NonNull ItemMessageReceiveBinding itemMessageReceiveBinding) {
            super(itemMessageReceiveBinding.getRoot());

            tvMessage = itemMessageReceiveBinding.tvReceiveMessage;
        }
    }

    @Override
    public int getItemViewType(int position) {
        Message message = chatMessageList.get(position);

        if(user_id.equals(message.getSender_id())) {
            return 1;
        }
        else {
            return 2;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == 1) {
            ItemMessageSentBinding itemMessageSentBinding = ItemMessageSentBinding
                    .inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new SentViewHolder(itemMessageSentBinding);
        }
        else {
            ItemMessageReceiveBinding itemMessageReceiveBinding = ItemMessageReceiveBinding
                    .inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ReceiveViewHolder(itemMessageReceiveBinding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = chatMessageList.get(position);

        if(holder.getClass() == SentViewHolder.class) {
            SentViewHolder viewHolder = (SentViewHolder) holder;
            viewHolder.tvMessage.setText(message.getMessage());
        }
        else {
            ReceiveViewHolder viewHolder = (ReceiveViewHolder) holder;
            viewHolder.tvMessage.setText(message.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return chatMessageList.size();
    }
}
