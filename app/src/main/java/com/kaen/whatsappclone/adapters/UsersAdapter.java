package com.kaen.whatsappclone.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kaen.whatsappclone.ChatDetailsActivity;
import com.kaen.whatsappclone.databinding.UserListItemBinding;
import com.kaen.whatsappclone.models.User;

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
        holder.binding.lastMessage.setVisibility(View.GONE);
        FirebaseDatabase.getInstance().getReference().child("Chats")
                        .child(FirebaseAuth.getInstance().getUid()+ user.getId())
                        .orderByChild("timestamp")
                        .limitToLast(1)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.hasChildren()){
                                    for (DataSnapshot snapshot1 : snapshot.getChildren()){
                                        holder.binding.lastMessage.setText(snapshot1.child("message").getValue().toString());
                                        holder.binding.lastMessage.setVisibility(View.VISIBLE);
                                    }
                                }
                                else {
                                    holder.binding.lastMessage.setText("Tap to start chatting.");
                                    holder.binding.lastMessage.setVisibility(View.VISIBLE);
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

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

    public static class UserViewHolder extends RecyclerView.ViewHolder{

        UserListItemBinding binding;
        public UserViewHolder(UserListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }
}
