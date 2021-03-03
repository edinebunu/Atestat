package com.example.atestat;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;

    public RecyclerViewAdapter(ArrayList<String> categories)
    {
        this.categories = categories;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_list_items,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.cat.setText(categories.get(position));

        final ArrayList<String> prodIds = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Categories").document(categories.get(position)).collection("Produse")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                prodIds.add(document.getId());
                            }
                            String type = categories.get(position);
                            initProdList(prodIds,holder,type);
                        }
                    }
                });
    }

    private void initProdList(ArrayList<String> mIds, ViewHolder holder,String type){
        ReyclerVireProdAdapter adapter = new ReyclerVireProdAdapter(mIds, type);
        holder.prod.setAdapter(adapter);
        holder.prod.setLayoutManager(new LinearLayoutManager(context));
    }

    private ArrayList<String> categories;

    @Override
    public int getItemCount()
    {return categories.size();}


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView cat;
        RecyclerView prod;

        public ViewHolder(View itemView)
        {
            super(itemView);
            cat = itemView.findViewById(R.id.textView);
            prod = itemView.findViewById(R.id.prlist);
            context = itemView.getContext();

        }
    }


}
