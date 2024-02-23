package com.kaen.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.kaen.whatsappclone.adapter.FragmentsAdapter;
import com.kaen.whatsappclone.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Toolbar toolbar;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
        toolbar = binding.mainToolBar;
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        progressBar = binding.progressBar;

        binding.viewPagerMain.setAdapter(new FragmentsAdapter(getSupportFragmentManager()));

        binding.tabLayoutMain.setupWithViewPager(binding.viewPagerMain);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.toolbarSettings){
            Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.toolbarGroupChat) {
            Toast.makeText(this, "Group Chat", Toast.LENGTH_SHORT).show();
        }else if (id == R.id.toolbarLogout) {
            progressBar.setVisibility(View.VISIBLE);
            mAuth.signOut();
            googleSignOut();
            progressBar.setVisibility(View.GONE);
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void googleSignOut() {
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
    }
}