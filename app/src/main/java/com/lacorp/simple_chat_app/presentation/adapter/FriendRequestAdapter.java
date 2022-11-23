package com.lacorp.simple_chat_app.presentation.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lacorp.simple_chat_app.R;
import com.lacorp.simple_chat_app.databinding.ItemChatRoomBinding;
import com.lacorp.simple_chat_app.databinding.ItemFriendRequestBinding;
import com.lacorp.simple_chat_app.domain.entities.Friend;
import com.lacorp.simple_chat_app.domain.entities.FriendRequest;

import java.util.List;

public class FriendRequestAdapter  extends RecyclerView.Adapter<FriendRequestAdapter.ViewHolder> {

    private final List<FriendRequest> friendRequestList;
    private ItemClickListener itemClickListener;

    public FriendRequestAdapter(List<FriendRequest> friendRequestList) {
        this.friendRequestList = friendRequestList;
    }

    public interface ItemClickListener {
        void onButtonAcceptClick(View view, FriendRequest friendRequest);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView tvUsername;

        ViewHolder(@NonNull ItemFriendRequestBinding itemFriendRequestBinding) {
            super(itemFriendRequestBinding.getRoot());

            tvUsername = itemFriendRequestBinding.tvUsername;
            Button btnAccept = itemFriendRequestBinding.btnAccept;
            btnAccept.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.btnAccept) {
                if(itemClickListener != null) {
                    itemClickListener.onButtonAcceptClick(view, friendRequestList.get(getAdapterPosition()));
                }
            }
        }
    }

    @NonNull
    @Override
    public FriendRequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFriendRequestBinding itemFriendRequestBinding = ItemFriendRequestBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new FriendRequestAdapter.ViewHolder(itemFriendRequestBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendRequestAdapter.ViewHolder holder, int position) {
        FriendRequest friendRequests = friendRequestList.get(position);
        holder.tvUsername.setText(friendRequests.getUsername());
    }

    @Override
    public int getItemCount() {
        return friendRequestList.size();
    }

    public void setOnClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
