package com.example.ronifitgo.ronifitgo.Adapters;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ronifitgo.R;
import com.example.ronifitgo.ronifitgo.Object.Tip;
import com.example.ronifitgo.ronifitgo.Object.Weight;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class Adapter_tip extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Activity activity;
    private ArrayList<Tip> tips = new ArrayList<>();


    public Adapter_tip(Activity activity, ArrayList<Tip> tips) {
        this.activity = activity;
        this.tips = tips;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_tip, parent, false);
        TipHolder holder = new TipHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final TipHolder holder = (TipHolder) viewHolder;
        Tip tip = getTip(position);


        holder.listTip_LBL_tip.setText(tip.getDescription());

    }

    @Override
    public int getItemCount() {
        return tips.size();
    }

    public Tip getTip(int position){
        return tips.get(position);
    }

    class TipHolder extends RecyclerView.ViewHolder {

        private MaterialTextView listTip_LBL_tip;


        public TipHolder(@NonNull View itemView) {
            super(itemView);
            listTip_LBL_tip =itemView.findViewById(R.id.listTip_LBL_tip);


        }
    }
}