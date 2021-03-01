package com.example.atestat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class AccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
    }

    public void sugnOut( View view)
    {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent( AccountActivity.this, WelcomePage.class);
        startActivity(intent);
        finish();
    }
}