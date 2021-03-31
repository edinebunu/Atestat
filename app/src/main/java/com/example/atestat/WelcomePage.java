package com.example.atestat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class WelcomePage extends AppCompatActivity {

    private static final String TAG = "WelcomePage";
    Button SignUp;
    Button LogIn;
    private long backPressedTime;
    private Toast backToast;
    FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser != null) {

            final String mUid = mAuth.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("Users").document(mUid);
            Toast.makeText(WelcomePage.this, mUid, Toast.LENGTH_SHORT).show();
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {

                            try {
                                if(document.getBoolean("isAdmin")) {
                                    Intent intent = new Intent(WelcomePage.this, AdminLandingActivity.class);
                                    intent.putExtra("UID", mUid);
                                    startActivity(intent);
                                    finish();
                                }
                            }catch(NullPointerException ignored){
                                Intent intent = new Intent(WelcomePage.this, MainActivity.class);
                                intent.putExtra("UID", mUid);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean login = prefs.getBoolean("login", false);

        SignUp = (Button) findViewById(R.id.signup);
        mAuth = FirebaseAuth.getInstance();

        SignUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(WelcomePage.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        LogIn = (Button) findViewById(R.id.login);

        LogIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(WelcomePage.this, LogInActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            return;
        }
        else{
            backToast = Toast.makeText(WelcomePage.this, "Press back again to exit",
                    Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }

}