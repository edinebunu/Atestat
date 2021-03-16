package com.example.atestat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CartActivity extends AppCompatActivity {

    static RecyclerView recyclerView;
    static ArrayList<String> categories = new ArrayList<>();
    @SuppressLint("StaticFieldLeak")
    static CartAdapter adapter;
    Button order;
    static TextView message;

     FirebaseFirestore db = FirebaseFirestore.getInstance();
     FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.cartcontainer);
        order = findViewById(R.id.button12);
        message = findViewById(R.id.textView14);

        getProdBuffer();

    }

    private void getProdBuffer(){
        db.collection("Users").document(Objects.requireNonNull(auth.getUid())).collection("Cart")
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
                            Toast.makeText(CartActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void loadRecomanded(){
        adapter = new CartAdapter(categories);
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

     public void onOrder(View view){

         db.collection("Users").document(auth.getUid()).collection("Cart")
                 .get()
                 .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                     @Override
                     public void onComplete(@NonNull Task<QuerySnapshot> task) {
                         if (task.isSuccessful()) {
                             Map<String, Object> user = new HashMap<>();
                             user.put("TimeOfOrder", FieldValue.serverTimestamp());

                             ArrayList<String> ids = new ArrayList<>();

                             for (QueryDocumentSnapshot document : task.getResult()) {

                                ids.add(document.getId());
                                 db.collection("Users").document(auth.getUid()).collection("Cart").document(document.getId()).delete();
                             }

                             user.put("ProductId", ids);
                             db.collection("Users").document(Objects.requireNonNull(auth.getUid()))
                                     .collection("BuyedProducts").add(user);
                         }
                     }
                 });
         categories.clear();

         if(categories.size()==0)message.setVisibility(View.VISIBLE);
         else message.setVisibility(View.INVISIBLE);

         loadRecomanded();
     }

}