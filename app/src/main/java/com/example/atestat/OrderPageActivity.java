package com.example.atestat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;

public class OrderPageActivity extends AppCompatActivity {

    TextView orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_page);
        orders = findViewById(R.id.textView17);

        getItems();
    }

    private void getItems(){
        String mId = null;

        Intent inent = getIntent();
        Bundle extras = inent.getExtras();

        if(extras != null)
        {
            mId = extras.getString("OrderId");
        }

        FirebaseAuth auth = FirebaseAuth.getInstance();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference docRef = db.collection("Users").document(auth.getUid()).collection("BuyedProducts").document(mId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String total = "";

                        ArrayList<String> mItems ;
                        mItems = (ArrayList<String>) document.get("ProductId");

                        assert mItems != null;
                        for(String item : mItems)
                            addStringEntry(item,orders);
                        orders.setText(total);
                    }

                }
            }
        });
    }

    private void addStringEntry(String id, final TextView orders){
        final String[] prod = new String[1];
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Products").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        prod[0] = document.get("Name").toString();
                        orders.append("- " + prod[0] + "\n");
                        //Toast.makeText(OrderPageActivity.this, prod[0], Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}