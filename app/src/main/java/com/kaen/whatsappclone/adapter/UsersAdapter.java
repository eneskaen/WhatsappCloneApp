package com.kaen.whatsappclone.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kaen.whatsappclone.ChatDetailsActivity;
import com.kaen.whatsappclone.databinding.UserListItemBinding;
import com.kaen.whatsappclone.model.User;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    ArrayList<User> userArrayList;
    Context context;

    public UsersAdapter(ArrayList<User> userArrayList, Context context) {
        this.userArrayList = userArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        UserListItemBinding binding = UserListItemBinding.inflate(layoutInflater, parent, false);
        return new UserViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userArrayList.get(position);
        holder.binding.setUser(user);
        holder.binding.executePendingBindings();

        holder.itemView.setOnClickListener(v -> {

            if (holder.getAdapterPosition() != RecyclerView.NO_POSITION){

                User clickedUser = userArrayList.get(holder.getAdapterPosition());
                Intent intent = new Intent(context, ChatDetailsActivity.class);
                intent.putExtra("userId", clickedUser.getId());
                intent.putExtra("userProfileImage", clickedUser.getProfileImageUrl());
                intent.putExtra("userName", clickedUser.getUsername());
                context.startActivity(intent);

            }


        });


    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{

        UserListItemBinding binding;
        public UserViewHolder(UserListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }
}
