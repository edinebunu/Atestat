package com.example.atestat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountActivity extends AppCompatActivity {

    CircleImageView profile;
    TextView name;
    String mUid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        Intent inent = getIntent();
        Bundle extras = inent.getExtras();
        if(extras != null)
            mUid = extras.getString("ProductId");

        profile = findViewById(R.id.profile_image2);
        name = findViewById(R.id.nameid);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Users").document(mUid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        name.setText(document.getString("FirstName") +" "+ document.getString("LastName"));
                    }
                }
            }
        });

        PictureSetter e = new PictureSetter();
        try {
            e.setImageRound(mUid,profile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }



    public void sugnOut( View view)
    {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent( AccountActivity.this, WelcomePage.class);
        startActivity(intent);
        finish();
    }
}