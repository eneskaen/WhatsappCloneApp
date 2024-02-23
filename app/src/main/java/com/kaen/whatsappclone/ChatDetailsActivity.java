package com.kaen.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kaen.whatsappclone.adapter.ChatAdapter;
import com.kaen.whatsappclone.databinding.ActivityChatDetailsBinding;
import com.kaen.whatsappclone.model.Message;
import com.kaen.whatsappclone.model.User;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ChatDetailsActivity extends AppCompatActivity implements ChatAdapter.OnDateChangeListener {

    ActivityChatDetailsBinding binding;
    FirebaseAuth mAuth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(ChatDetailsActivity.this, R.layout.activity_chat_details);

        database = FirebaseDatabase.getInstance();

        mAuth = FirebaseAuth.getInstance();


        final  String senderId = mAuth.getUid();
        String recievedId = getIntent().getStringExtra("userId");
        String userName = getIntent().getStringExtra("userName");
        String profileImage = getIntent().getStringExtra("userProfileImage");

        binding.userNameChatDetails.setText(userName);
        Picasso.get().load(profileImage).placeholder(R.drawable.avatar_default).into(binding.profileImageChatDetails);

        binding.backButton.setOnClickListener(v -> {

            Intent intent = new Intent(ChatDetailsActivity.this, MainActivity.class);
            startActivity(intent);

        });

        final ArrayList<Message> messageArrayList = new ArrayList<>();
        final ChatAdapter chatAdapter = new ChatAdapter(this, messageArrayList, recievedId);

        chatAdapter.setDateChangeListener(this);

        binding.chatDetailsRecyclerView.setAdapter(chatAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatDetailsRecyclerView.setLayoutManager(layoutManager);

        final String senderRoom = senderId + recievedId;
        final String recieverRoom = recievedId + senderId;

        database.getReference().child("Chats")
                        .child(senderRoom).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messageArrayList.clear();
                        for (DataSnapshot snapshot1 : snapshot.getChildren())
                        {
                            Message message = snapshot1.getValue(Message.class);
                            message.setMessageId(snapshot1.getKey());
                            messageArrayList.add(message);
                        }
                        chatAdapter.notifyDataSetChanged();
                        int itemCount = chatAdapter.getItemCount();
                        if (itemCount > 0) {
                            binding.chatDetailsRecyclerView.scrollToPosition(itemCount - 1);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.sendMessegaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageString = binding.typeMessageEditText.getText().toString().trim();
                final Message message = new Message(senderId, messageString);
                message.setTimestamp(new Date().getTime());
                binding.typeMessageEditText.setText("");

                database.getReference().child("Chats")
                        .child(senderRoom)
                        .push()
                        .setValue(message).addOnSuccessListener( unused -> {
                            database.getReference().child("Chats")
                                    .child(recieverRoom)
                                    .push()
                                    .setValue(message)
                                    .addOnSuccessListener(unused1 -> {

                                    });
                        });
            }
        });


    }

    public void onDateChanged(String newDate){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        String currentDate = dateFormat.format(Calendar.getInstance().getTime());
        Calendar yesterdayCal = Calendar.getInstance();
        yesterdayCal.add(Calendar.DAY_OF_MONTH, -1);
        String yesterdayDate = dateFormat.format(yesterdayCal.getTime());



        if (currentDate.equals(newDate))
        {
            binding.dateOfMessages.setVisibility(View.GONE);
        }
        else if (yesterdayDate.equals(newDate))
        {
            binding.dateOfMessages.setVisibility(View.VISIBLE);
            binding.dateOfMessages.setText("Yesterday");
        }
        else
        {
            binding.dateOfMessages.setVisibility(View.VISIBLE);
            binding.dateOfMessages.setText(newDate);
        }


    }


}
