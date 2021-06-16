package com.samer.aljood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.samer.aljood.R;
import com.samer.aljood.model.Branche;

import java.util.ArrayList;

public class BrancheAdabter extends RecyclerView.Adapter<BrancheAdabter.ViewHolder>  {
    private Context context;
    ArrayList<Branche> brancheArrayList;

    public BrancheAdabter(Context context, ArrayList<Branche> brancheArrayList) {
        this.context = context;
        this.brancheArrayList = brancheArrayList;
    }

    @NonNull
    @Override
    public BrancheAdabter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BrancheAdabter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.branch_item, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull BrancheAdabter.ViewHolder holder, int position) {
        Branche branche = brancheArrayList.get(position);
        holder.bind(branche);
    }

    @Override
    public int getItemCount() {
        return  brancheArrayList.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,location,phone;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            location = itemView.findViewById(R.id.location);
            phone=itemView.findViewById(R.id.phone);

        }
        void  bind(final Branche branche){
            name.setText(branche.getName());
            location.setText(branche.getLocation());
            phone.setText(branche.getPhone());

        }

    }

}
