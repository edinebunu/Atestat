package com.example.atestat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private Context context;

    public OrderAdapter(ArrayList<String> categories)
    {
        this.categories = categories;
    }

    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_date_receipt,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        FirebaseAuth auth = FirebaseAuth.getInstance();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Users").document(auth.getUid()).collection("BuyedProducts").document(categories.get(position));
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        Timestamp time = document.getTimestamp("TimeOfOrder");
                        Date date = time.toDate();

                        holder.date.setText(DateFormat.format("yyyy", date).toString()+"/"+DateFormat.format("MM",   date).toString()+"/"+DateFormat.format("dd",   date).toString()+
                                "/ "+DateFormat.format("hh",   date).toString()+":"+DateFormat.format("mm",   date).toString());

                    }
                }
            }
        });

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OrderPageActivity.class);
                intent.putExtra("OrderId",categories.get(position));
                context.startActivity(intent);
            }
        });

    }


    private ArrayList<String> categories;

    @Override
    public int getItemCount()
    {return categories.size();}


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView date;
        ConstraintLayout parent;

        public ViewHolder(View itemView)
        {
            super(itemView);
            parent = itemView.findViewById(R.id.onb);
            date = itemView.findViewById(R.id.textView16);
            context = itemView.getContext();

        }
    }


}
