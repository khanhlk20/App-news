package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
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
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import io.grpc.Compressor;

import static com.example.myapplication.RegisterActivity.PReqCode;

public class PostActivity extends AppCompatActivity {
    private ImageButton imageButton;
    private static final int REQUESCODE=1;
    private Button submitbtn;
    private EditText edtTitle,edtDes;
    private StorageReference storageReference;
    Uri pickedImgUri=null ;
    static int PReqCode = 1 ;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;


    private StorageReference storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        storageReference = FirebaseStorage.getInstance().getReference();
        super.onCreate(savedInstanceState);
        storage = FirebaseStorage.getInstance().getReference();
        setContentView(R.layout.activity_post);
        imageButton = findViewById(R.id.imageButton3);
        edtDes=findViewById(R.id.edtDes);
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        edtTitle=findViewById(R.id.edtTitle);
        firebaseFirestore = FirebaseFirestore.getInstance();
        submitbtn=findViewById(R.id.submitbtn);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (ContextCompat.checkSelfPermission(PostActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(PostActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                            Toast.makeText(PostActivity.this,"Please accept for required permission",Toast.LENGTH_SHORT).show();

                        }

                        else
                        {
                            ActivityCompat.requestPermissions(PostActivity.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    PReqCode);
                        }

                    }
                    else{
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .start(PostActivity.this);

                    }
                }

            }
        });




        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                submitbtn.setVisibility(View.INVISIBLE);


                // we need to test all input fields (Title and description ) and post image

                if (!edtTitle.getText().toString().isEmpty()
                        && !edtDes.getText().toString().isEmpty()
                        && pickedImgUri != null ) {

                    //everything is okey no empty or null value
                    // TODO Create Post Object and add it to firebase database
                    // first we need to upload post Image
                    // access firebase storage
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("blog_images");
                    final StorageReference imageFilePath = storageReference.child(pickedImgUri.getLastPathSegment());
                    imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageDownlaodLink = uri.toString();
                                    // create post Object
                                    Post post = new Post(edtTitle.getText().toString(),
                                            edtDes.getText().toString(),
                                            imageDownlaodLink,
                                            currentUser.getUid(),
                                            currentUser.getPhotoUrl().toString());

                                    // Add post to firebase database

                                    addPost(post);



                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // something goes wrong uploading picture



                                    imageButton.setVisibility(View.VISIBLE);


                                }
                            });


                        }
                    });





                }
                else {
                    showMessage("Please verify all input fields and choose Post Image") ;
                    imageButton.setVisibility(View.VISIBLE);

                }



            }
        });




    }
    private void showMessage(String message) {

        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();

    }

    private void addPost(Post post) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Posts").push();

        // get post unique ID and upadte post key
        String key = myRef.getKey();
        post.setPostKey(key);


        // add post data to firebase database

        myRef.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showMessage("Post Added successfully");

                imageButton.setVisibility(View.VISIBLE);
                PostActivity.super.finish();
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                pickedImgUri = result.getUri();
                imageButton.setImageURI(pickedImgUri);


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }


    }






}

