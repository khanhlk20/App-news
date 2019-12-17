package com.example.myapplication;



import android.net.Uri;


import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;



public class SetupActivity extends AppCompatActivity {


    private Button accept;

    FirebaseAuth mAuth;

    FirebaseUser currentUser;

    EditText navUsername;
    EditText navUserMail;
    ImageView navUserPhot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        navUsername = findViewById(R.id.nav_username);
        navUserMail = findViewById(R.id.nav_user_mail);
        navUserPhot = findViewById(R.id.nav_user_photo);
        navUserMail.setText(currentUser.getEmail());
        navUsername.setText(currentUser.getDisplayName());
        accept = findViewById(R.id.accept);


        // now we will use Glide to load user image
        // first we need to import the library
        Glide.with(this).load(currentUser.getPhotoUrl()).into(navUserPhot);
    }
}



