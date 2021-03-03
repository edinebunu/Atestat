package com.example.atestat;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private Context context;
    public ArrayList<String> categories;

    public CartAdapter(ArrayList<String> categories)
    {
        this.categories = categories;
    }

    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        PictureSetter p = new PictureSetter();
        try {
            p.setImage(categories.get(position), holder.pic);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Products").document(categories.get(position));
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    assert document != null;
                    if (document.exists()) {
                        holder.price.setText(Objects.requireNonNull(document.get("Price")).toString());
                        holder.name.setText(document.get("Name").toString());
                    }
                }
            }
        });

//        holder.parentLayout.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//
//            }
//        });

    }



    @Override
    public int getItemCount()
    {return categories.size();}


    public class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout parentLayout;
        TextView price;
        TextView name;
        ImageView pic;
        Button remove;

        public ViewHolder(View itemView)
        {
            super(itemView);
            price = itemView.findViewById(R.id.textView12);
            pic = itemView.findViewById(R.id.imageView4);
            context = itemView.getContext();
            parentLayout = itemView.findViewById(R.id.ccitm);
            remove = itemView.findViewById(R.id.button13);
            name = itemView.findViewById(R.id.textView2);

        }
    }
}
