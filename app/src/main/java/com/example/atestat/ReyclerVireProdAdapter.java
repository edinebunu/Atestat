package com.example.atestat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;

public class ReyclerVireProdAdapter extends RecyclerView.Adapter<ReyclerVireProdAdapter.ViewHolder> {

    private Context context;

    public ReyclerVireProdAdapter(ArrayList<String> categories)
    {
        this.categories = categories;
    }

    @Override
    public ReyclerVireProdAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_preview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Products").document(categories.get((position)));
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                       holder.price.setText( document.get("Price").toString());
                       holder.item.setText(document.get("Name").toString());
                    }
                }
            }
        });

        PictureSetter p = new PictureSetter();
        try {
            p.setImage(categories.get(position),holder.pic);
        } catch (IOException e) {
            e.printStackTrace();
        }

        holder.parentLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(context,ProductActivity.class);
                intent.putExtra("ProductId", categories.get(position));
                context.startActivity(intent);
            }
        });

    }



    private ArrayList<String> categories;

    @Override
    public int getItemCount()
    {return categories.size();}


    public class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout parentLayout;
        TextView price;
        TextView item;
        ImageView pic;

        public ViewHolder(View itemView)
        {
            super(itemView);
            price = itemView.findViewById(R.id.textView4);
            pic = itemView.findViewById(R.id.imageView);
            item = itemView.findViewById(R.id.textView5);
            context = itemView.getContext();
            parentLayout = itemView.findViewById(R.id.cl1);

        }
    }

    public void openProduct(View view){
        Intent intent  = new Intent(context, ProductActivity.class);

    }


}
