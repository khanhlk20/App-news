package com.example.myapplication;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class HomeActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    TextView navUsername ;
    TextView navUserMail ;
    ImageView navUserPhot,imagenews;
    FirebaseUser currentUser;

    private Button settig,back,button;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        button=findViewById(R.id.button);
        settig=findViewById(R.id.setting);
        back=findViewById((R.id.back));
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        imagenews = findViewById(R.id.imagenews);
        navUsername = findViewById(R.id.activity_main_tv_user_name);
        navUserMail = findViewById(R.id.activity_main_mail);
        navUserPhot = findViewById(R.id.activity_main_imv_avatar);
        Log.i("check",currentUser.getEmail().toString());
        navUserMail.setText(currentUser.getEmail().toString());
        navUsername.setText(currentUser.getDisplayName());
        Glide.with(this).load(currentUser.getPhotoUrl()).into(navUserPhot);

        settig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginactivity = new Intent(HomeActivity.this,PostActivity.class);
                startActivity(loginactivity);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent loginactivity = new Intent(HomeActivity.this,LoginActivity.class);
                startActivity(loginactivity);

            }
        });
        drawerLayout = findViewById(R.id.homeactivity);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent setup = new Intent(HomeActivity.this,SetupActivity.class);
                startActivity(setup);
            }
        });

    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add){
            startActivity(new Intent(HomeActivity.this, PostActivity.class));
        }


        if(drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }




        return super.onOptionsItemSelected(item);
    }
}

