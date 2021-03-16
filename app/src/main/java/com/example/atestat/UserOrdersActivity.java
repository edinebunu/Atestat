package com.example.atestat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UserOrdersActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    ArrayList<String> orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_orders);

        mAuth = FirebaseAuth.getInstance();
        orders = new ArrayList<>();

        recyclerView = findViewById(R.id.orderslist);
        getOrderList();
    }

    private void getOrderList(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Users").document(mAuth.getUid()).collection("BuyedProducts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                orders.add(document.getId());
                            }
                            initRecycleView();
                        }
                    }
                });

    }

    private void initRecycleView(){

        OrderAdapter adapter = new OrderAdapter(orders);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }



}