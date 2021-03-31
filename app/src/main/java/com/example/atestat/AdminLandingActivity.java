package com.example.atestat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class AdminLandingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_landing);

    }

    public void sout(View view){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent( AdminLandingActivity.this, WelcomePage.class);
        startActivity(intent);
        finish();
    }

    public void addP(View view){
        Intent intent = new Intent(AdminLandingActivity.this, AdminActivity.class);
        startActivity(intent);
    }

}