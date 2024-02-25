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
import com.kaen.whatsappclone.databinding.ActivitySignUpBinding;
import com.kaen.whatsappclone.models.User;
import com.kaen.whatsappclone.singletons.UserStatusManager;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private ActivitySignUpBinding binding;
    private ProgressBar progressBar;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(SignUpActivity.this, R.layout.activity_sign_up);
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();

        progressBar = binding.progressBar;

        binding.signUpButton.setOnClickListener(v -> {
            String username = binding.signUpUserNameText.getText().toString();
            String email = binding.signUpEmailText.getText().toString();
            String password = binding.signUpPasswordText.getText().toString();
            if (!username.isEmpty() && !email.isEmpty() && !password.isEmpty()){
                progressBar.setVisibility(View.VISIBLE);

                firebaseCreateUser(username, email, password);

                progressBar.setVisibility(View.GONE);

                Toast.makeText(this, "Account Created Successfully!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();

            }
            else{
                Toast.makeText(this, "Fill in all fields!", Toast.LENGTH_SHORT).show();
            }
            
        });

        binding.alreadyHaveText.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
            startActivity(intent);
            finish();
        });


        binding.signUpWithGoogleButton.setOnClickListener(v -> {
            googleSignUpFunc();
        });

        if (mAuth.getCurrentUser() != null){

            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

        }
        
    }

    private void googleSignUpFunc() {

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

    private void firebaseCreateUser(String username, String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    User user = new User(username, email, password,null, true);
                    String userUid = authResult.getUser().getUid();
                    writeToDatabase(userUid, user);

                }).addOnFailureListener( e -> {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });


    }

    private void writeToDatabase(String userUid, User user) {
        mRef.child(userUid).setValue(user);
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

                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }


        }
    }

    private void googleFirebaseAuth(String idToken) {

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

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
                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                            startActivity(intent);
                        }

                    }
                });

    }
}