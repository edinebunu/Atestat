package com.example.atestat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class FavouritesActivity extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseAuth auth;
    static ArrayList<String> categories;
    static RecyclerView recyclerView;
    static FavouritesAdapter adapter;
    static TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);


        message = findViewById(R.id.textView13);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        categories = new ArrayList<>();

        recyclerView = findViewById(R.id.favid);

        getProdBuffer();
    }

    private void getProdBuffer(){
        db.collection("Users").document(Objects.requireNonNull(auth.getUid())).collection("Favourites")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            categories.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                categories.add(document.getId());
                            }
                            if(categories.size()==0)message.setVisibility(View.VISIBLE);
                            else message.setVisibility(View.INVISIBLE);
                            loadRecomanded();
                        } else {
                            Toast.makeText(FavouritesActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void loadRecomanded(){
        adapter = new FavouritesAdapter(categories);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public static void ceplmeasta(int position){
        categories.remove(position);
        recyclerView.removeViewAt(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position, categories.size());
        if(categories.size()==0)message.setVisibility(View.VISIBLE);
        else message.setVisibility(View.INVISIBLE);

    }

}