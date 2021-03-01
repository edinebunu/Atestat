package com.example.atestat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;

public class ProductActivity extends AppCompatActivity {

    String mId;
    TextView title;
    TextView price;
    TextView desc;
    ImageView image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);


        Intent inent = getIntent();
        Bundle extras = inent.getExtras();
        if(extras != null)
             mId = extras.getString("ProductId");

        image = findViewById(R.id.imageView2);
        title = findViewById(R.id.textView6);
        price = findViewById(R.id.textView7);
        desc = findViewById(R.id.textView11);

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
    }

}