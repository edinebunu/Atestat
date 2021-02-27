package com.example.atestat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> categories = new ArrayList<>();
    RecyclerView recyclerView;
    String mUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mUid = mAuth.getUid();


    }

    @Override
    protected void onStart() {
        super.onStart();
        getCategoryBuffer();
    }

    public void getCategoryBuffer(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Categories")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                categories.add(document.getId());
                            }
                            initRecyclerView();
                        } else {
                            Toast.makeText(MainActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void initRecyclerView(){

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(categories);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}