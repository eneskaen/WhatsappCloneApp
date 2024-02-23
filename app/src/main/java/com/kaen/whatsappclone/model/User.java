package com.kaen.whatsappclone.model;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kaen.whatsappclone.R;
import com.squareup.picasso.Picasso;

public class User {

    public User() {
    }


    //Constructor for sign up.
    public User(String username, String email, String password, String profileImageUrl) {
        this.username = username;
        this.email = email;

        if (password != null) {
            this.password = password;
        }

        if (profileImageUrl != null) {
            this.profileImageUrl = profileImageUrl;
        }
    }

    //Constructor for sign in.
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    //Constructor all params.
    public User(String id, String username, String email, String password, String profileImageUrl, String lastMessage) {
        Id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.profileImageUrl = profileImageUrl;
        this.lastMessage = lastMessage;
    }

    @SerializedName("userId")
    @Expose
    private String Id;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("userEmail")
    @Expose
    private String email;

    @SerializedName("userPassword")
    @Expose
    private String password;

    @SerializedName("userProfileImageUrl")
    @Expose
    private String profileImageUrl;

    @SerializedName("userLastMessage")
    @Expose
    private String lastMessage;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    @BindingAdapter("android:loadImage")
    public static void loadImage(ImageView imageView, String profileImageUrl){
        Picasso.get()
                .load(profileImageUrl)
                .placeholder(R.drawable.avatar_default)
                .into(imageView);

    }
}
