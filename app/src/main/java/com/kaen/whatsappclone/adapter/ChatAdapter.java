package com.kaen.whatsappclone.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.kaen.whatsappclone.R;
import com.kaen.whatsappclone.databinding.ActivityChatDetailsBinding;
import com.kaen.whatsappclone.databinding.SampleRecieverBinding;
import com.kaen.whatsappclone.databinding.SampleSenderBinding;
import com.kaen.whatsappclone.model.Message;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ChatAdapter extends RecyclerView.Adapter{
    Context context;
    ArrayList<Message> messageArrayList = new ArrayList<>();
    String recId;
    int SENDER_VIEW_TYPE = 1;
    int RECIEVER_VIEW_TYPE = 2;

    public interface OnDateChangeListener {
        void onDateChanged(String newDate);
    }

    private OnDateChangeListener dateChangeListener;

    public void setDateChangeListener(OnDateChangeListener listener) {
        this.dateChangeListener = listener;
    }
    public ChatAdapter(Context context, ArrayList<Message> messageArrayList, String recId) {
        this.context = context;
        this.messageArrayList = messageArrayList;
        this.recId = recId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        if (viewType == SENDER_VIEW_TYPE){
            SampleSenderBinding binding = SampleSenderBinding.inflate(layoutInflater, parent, false);
            return new SenderViewHolder(binding);
        }
        else
        {
            SampleRecieverBinding binding = SampleRecieverBinding.inflate(layoutInflater, parent, false);
            return new RecieverViewHolder(binding);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (messageArrayList.get(position).getuId().equals(FirebaseAuth.getInstance().getUid()))
        {
            return SENDER_VIEW_TYPE;
        }
        else
        {
            return RECIEVER_VIEW_TYPE;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Message message = messageArrayList.get(position);

        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete")
                    .setMessage("Are you sure to want to delete this message?")
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            String senderRoom = FirebaseAuth.getInstance().getUid() + recId;
                            database.getReference().child("Chats").child(senderRoom)
                                    .child(message.getMessageId())
                                    .setValue(null);

                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
            return false;
        });

        if (holder.getClass() == SenderViewHolder.class){
            ((SenderViewHolder) holder).binding.setMessage(message);
        }
        else if (holder.getClass() == RecieverViewHolder.class)
        {
            ((RecieverViewHolder)holder).binding.setMessage(message);
        }

        if (position > 0) {

            Message currentMessage = messageArrayList.get(position);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
            String messageDate = dateFormat.format(currentMessage.getTimestamp());
            dateChangeListener.onDateChanged(messageDate);

        }


    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }


    public class RecieverViewHolder extends RecyclerView.ViewHolder{
        SampleRecieverBinding binding;
        public RecieverViewHolder(SampleRecieverBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

    public class SenderViewHolder extends RecyclerView.ViewHolder{
        SampleSenderBinding binding;
        public SenderViewHolder(SampleSenderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
