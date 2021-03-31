package com.example.atestat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LogInActivity extends AppCompatActivity {

    private long backPressedTime;
    private Toast backToast;

    EditText email;
    EditText password;

    Button logIn;

    private FirebaseAuth mAuth;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        logIn = (Button) findViewById(R.id.login);

        email = (EditText) findViewById(R.id.emailAdress);
        password = (EditText) findViewById(R.id.password);

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputEmail = email.getText().toString();
                String inputPassword = password.getText().toString();
                signInAuth(inputEmail, inputPassword);
            }
        });
    }


    private void signInAuth (String inputEmail, String inputPassword){
        mAuth.signInWithEmailAndPassword(inputEmail, inputPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            final String mUid = user.getUid();

                            try
                            {
                                SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putBoolean("login", true);
                                editor.apply();

                                FirebaseFirestore db = FirebaseFirestore.getInstance();

                                DocumentReference docRef = db.collection("Users").document(mUid);
//                                Toast.makeText(LogInActivity.this, mUid, Toast.LENGTH_SHORT).show();
                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                    try {
                                                        if(document.getBoolean("isAdmin")) {
                                                            Intent intent = new Intent(LogInActivity.this, AdminLandingActivity.class);
                                                            intent.putExtra("UID", mUid);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    }catch(NullPointerException ignored){
                                                        Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                                                        intent.putExtra("UID", mUid);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                            }
                                        }
                                    }
                                });
                            }
                            catch(NullPointerException e){
                                Toast.makeText(LogInActivity.this, "Log In failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(LogInActivity.this, "Invalid credentials",
                                    Toast.LENGTH_SHORT).show();
                        }
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
            backToast = Toast.makeText(LogInActivity.this, "Press back again to exit",
                    Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}