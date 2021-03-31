package com.example.atestat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductActivity extends AppCompatActivity {

    String mId;
    String type;
    TextView title;
    TextView price;
    TextView desc;
    ImageView image;
    RecyclerView recomanded;

    ArrayList<String> categories = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);


        Intent inent = getIntent();
        Bundle extras = inent.getExtras();
        if(extras != null)
        {
            mId = extras.getString("ProductId");
            type = extras.getString("Type");
        }

        image = findViewById(R.id.imageView2);
        title = findViewById(R.id.textView6);
        price = findViewById(R.id.textView7);
        desc = findViewById(R.id.textView11);
        recomanded = findViewById(R.id.recomamnddedact);

        loadData();
    }

    private void loadData(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Products").document(mId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        PictureSetter p = new PictureSetter();
                        try {
                            p.setImage(mId,image);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        title.setText(document.get("Name").toString());
                        price.setText(document.get("Price").toString());
                        desc.setText(document.get("Description").toString());

                    }
                }
            }
        });

        db.collection("Categories").document(type).collection("Produse")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            categories.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(!document.getId().equals(mId))
                                categories.add(document.getId());
                            }
                            loadRecomanded();
                        }
                    }
                });
    }

    private void loadRecomanded(){
        ReyclerVireProdAdapter adapter = new ReyclerVireProdAdapter(categories,type);
        recomanded.setAdapter(adapter);
        recomanded.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL,false));
    }

    public void addCart(View view){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> nestedData = new HashMap<>();

        db.collection("Users").document(auth.getUid())
                .collection("Cart").document(mId).set(nestedData);
    }

}