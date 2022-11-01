package com.lacorp.simple_chat_app.presentation.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lacorp.simple_chat_app.data.entities.User;
import com.lacorp.simple_chat_app.databinding.ItemChatRoomBinding;

import java.util.List;

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ViewHolder> {

    private List<User> userList;
    private ItemClickListener itemClickListener;

    public ChatRoomAdapter(List<User> userList) {
        this.userList = userList;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView tvUsername;

        ViewHolder(@NonNull ItemChatRoomBinding itemChatRoomBinding) {
            super(itemChatRoomBinding.getRoot());

            tvUsername = itemChatRoomBinding.tvUsername;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(itemClickListener != null) {
                itemClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemChatRoomBinding itemChatRoomBinding = ItemChatRoomBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(itemChatRoomBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);
        holder.tvUsername.setText(user.getFullname());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void setOnClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
