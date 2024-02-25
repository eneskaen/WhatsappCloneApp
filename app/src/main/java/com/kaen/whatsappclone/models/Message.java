package com.kaen.whatsappclone.models;

import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Message {

    String uId, message, messageId;
    Long timestamp;

    public Message(String uId, String message, Long timestamp) {
        this.uId = uId;
        this.message = message;
        this.timestamp = timestamp;
    }

    public Message(String uId, String message) {
        this.uId = uId;
        this.message = message;
    }



    public Message() {
    }


    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @BindingAdapter("timestampString")
    public static void getTimestampString(TextView textView, long timestamp){
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String formattedDate = dateFormat.format(new Date(timestamp));
        textView.setText(formattedDate);

    }
}
