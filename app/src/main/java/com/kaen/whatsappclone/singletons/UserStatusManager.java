package com.kaen.whatsappclone.singletons;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class UserStatusManager {
    private static final  UserStatusManager instance = new UserStatusManager();
    private boolean isOnline = false;

    private UserStatusManager() {

    }

    public static UserStatusManager getInstance(){
        return instance;
    }

    public boolean isOnline(){
        return isOnline;
    }
    public void setOnline(boolean online){
        isOnline = online;

        if (FirebaseAuth.getInstance()!=null){
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            String userId = mAuth.getCurrentUser().getUid();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            database.getReference().child("Users").child(userId).child("isOnline").setValue(online)
                    .addOnSuccessListener(unused -> {
                    }).addOnFailureListener(e -> {
                    });
        }
    }
}
