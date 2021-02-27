package com.example.atestat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

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
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.cat.setText(categories.get(position));
    }

    private ArrayList<String> categories = new ArrayList<>();

    @Override
    public int getItemCount()
    {return categories.size();}


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView cat;

        public ViewHolder(View itemView)
        {
            super(itemView);
            cat = itemView.findViewById(R.id.textView);

        }
    }


}
