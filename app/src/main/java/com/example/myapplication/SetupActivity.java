package com.example.myapplication;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;


import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Set;

import static com.example.myapplication.RegisterActivity.PReqCode;

public class SetupActivity extends AppCompatActivity {

    private ImageView setupImage;


    private EditText setupName,showmail;
    private Button setupBtn;
    Uri avatar=null;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();


        setupImage = findViewById(R.id.nav_user_photo);
        setupName = findViewById(R.id.nav_username);
        setupBtn = findViewById(R.id.accept);
        showmail=findViewById(R.id.nav_user_mail);
        setupName.setText(mAuth.getCurrentUser().getDisplayName());
        showmail.setText(mAuth.getCurrentUser().getEmail());
        Glide.with(this).load(mAuth.getCurrentUser().getPhotoUrl()).into(setupImage);

        setupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (ContextCompat.checkSelfPermission(SetupActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(SetupActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                            Toast.makeText(SetupActivity.this,"Please accept for required permission",Toast.LENGTH_SHORT).show();

                        }

                        else
                        {
                            ActivityCompat.requestPermissions(SetupActivity.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    PReqCode);
                        }

                    }
                    else{
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .start(SetupActivity.this);

                    }
                }

            }
        });




        setupBtn.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {


                    // first we need to upload user photo to firebase storage and get url

                    StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("users_photos");
                    final StorageReference imageFilePath = mStorage.child(avatar.getLastPathSegment());
                    imageFilePath.putFile(avatar).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(setupName.getText().toString())
                                            .setPhotoUri(uri)
                                            .build();

                                    currentUser.updateProfile(profileUpdates)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        showMessage("Account created");
                                                        Intent done = new Intent(SetupActivity.this, HomeActivity.class);
                                                        startActivity(done);

                                                    }
                                                }
                                            });

                                }
                            });
                        }
                    });
            }
        });
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                avatar = result.getUri();
                setupImage.setImageURI(avatar);


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }


    }


}



