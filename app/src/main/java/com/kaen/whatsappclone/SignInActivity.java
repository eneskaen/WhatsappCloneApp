package com.kaen.whatsappclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kaen.whatsappclone.databinding.ActivitySignInBinding;
import com.kaen.whatsappclone.models.User;
import com.kaen.whatsappclone.singletons.UserStatusManager;

import java.util.Objects;

public class SignInActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private ActivitySignInBinding binding;
    private ProgressBar progressBar;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(SignInActivity.this, R.layout.activity_sign_in);

        database = FirebaseDatabase.getInstance();
        mRef = database.getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();

        progressBar = binding.progressBar;

        binding.signInButton.setOnClickListener(v -> {

            String email = binding.signInEmailText.getText().toString();
            String password = binding.signInPasswordText.getText().toString();
            if (!email.isEmpty() && !password.isEmpty()){
                progressBar.setVisibility(View.VISIBLE);

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener(authResult -> {
                            progressBar.setVisibility(View.GONE);
                            UserStatusManager.getInstance().setOnline(true);
                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                        }).addOnFailureListener(e -> {

                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);

                        });

            }

        });

        binding.createAccountText.setOnClickListener(v -> {

            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(intent);
            finish();

        });

        binding.signInWithGoogleButton.setOnClickListener(v -> {

            googleSignInFunc();

        });

        if (mAuth.getCurrentUser() != null){
            UserStatusManager.getInstance().setOnline(true);
            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

        }
    }

    private void googleSignInFunc() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.signOut().addOnCompleteListener(task -> {

            if (task.isSuccessful()){
                mAuth.signOut();
            }

        });
        Intent intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN, null);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RC_SIGN_IN){

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            progressBar.setVisibility(View.VISIBLE);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                googleFirebaseAuth(account.getIdToken());
            }
            catch (Exception e){
                progressBar.setVisibility(View.GONE);
                if (!e.getMessage().equals("12501: ")){
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }


        }
    }

    private void writeToDatabase(String userUid, User user) {
        mRef.child(userUid).setValue(user);
    }

    private void googleFirebaseAuth(String idToken) {

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            FirebaseUser googleUser = mAuth.getCurrentUser();
                            User user = new User(googleUser.getDisplayName(), googleUser.getEmail(), null ,googleUser.getPhotoUrl().toString(), true);
                            writeToDatabase(googleUser.getUid(), user);
                            UserStatusManager.getInstance().setOnline(true);
                            progressBar.setVisibility(View.GONE);
                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                }).addOnFailureListener(e -> {
                   progressBar.setVisibility(View.GONE);
                });
    }

}