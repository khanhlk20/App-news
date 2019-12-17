package com.example.myapplication;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import io.grpc.Compressor;



public class SetupActivity extends AppCompatActivity {

    private ImageView setupImage;
    private Uri mainImageURI = null;

    private String user_id;

    private boolean isChanged = false;
    FirebaseAuth mAuth;

    FirebaseUser currentUser;

    TextView navUsername ;
    TextView navUserMail ;
    ImageView navUserPhot ;


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


        // now we will use Glide to load user image
        // first we need to import the library

        Glide.with(this).load(currentUser.getPhotoUrl()).into(navUserPhot);
    }
}



