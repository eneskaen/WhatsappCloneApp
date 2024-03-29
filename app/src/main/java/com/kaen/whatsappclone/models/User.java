package com.kaen.whatsappclone.models;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kaen.whatsappclone.R;
import com.squareup.picasso.Picasso;

public class User {

    public User() {
    }



    //Constructor for sign up or sign in GOOGLE.
    public User(String username, String email, String password, String profileImageUrl, boolean isOnline) {
        this.username = username;
        this.email = email;
        this.isOnline = isOnline;
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
        this.isOnline = false;
    }

    //Constructor all params.
    public User(String id, String username, String email, String password, String profileImageUrl, boolean isOnline) {
        this.Id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.profileImageUrl = profileImageUrl;
        this.isOnline = isOnline;
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


    @SerializedName("userIsOnline")
    @Expose
    private boolean isOnline;

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

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    @BindingAdapter("android:loadImage")
    public static void loadImage(ImageView imageView, String profileImageUrl){
        Picasso.get()
                .load(profileImageUrl)
                .placeholder(R.drawable.avatar_default)
                .into(imageView);

    }
}
