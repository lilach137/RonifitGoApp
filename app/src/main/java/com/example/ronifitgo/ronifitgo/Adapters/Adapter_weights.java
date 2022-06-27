package com.example.ronifitgo.ronifitgo.Adapters;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.ronifitgo.R;
import com.example.ronifitgo.ronifitgo.Fragments.fragment_weights;
import com.example.ronifitgo.ronifitgo.Object.Weight;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class Adapter_weights extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Activity activity;
    private ArrayList<Weight> weights = new ArrayList<>();


    public Adapter_weights(Activity activity, ArrayList<Weight> weights) {
        this.activity = activity;
        this.weights = weights;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_weight, parent, false);
        WeightHolder holder = new WeightHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final WeightHolder holder = (WeightHolder) viewHolder;
        Weight weight = getWeight(position);


        holder.listWeight_TXT_date.setText(weight.getDate().toString());
        holder.listWeight_TXT_weight.setText(String.valueOf(weight.getWeight()));
        holder.listWeight_TXT_pFat.setText(String.valueOf(weight.getpFat()));
        holder.listWeight_TXT_pMuscle.setText(String.valueOf(weight.getpMuscle()));

    }

    @Override
    public int getItemCount() {
        return weights.size();
    }

    public Weight getWeight(int position){
        return weights.get(position);
    }

    class WeightHolder extends RecyclerView.ViewHolder {

        private MaterialTextView listWeight_TXT_pFat;
        private MaterialTextView listWeight_TXT_pMuscle;
        private MaterialTextView listWeight_TXT_weight;
        private MaterialTextView listWeight_TXT_date;
        private AppCompatImageView listWeight_IMG_pic;

        public WeightHolder(@NonNull View itemView) {
            super(itemView);
            listWeight_TXT_pFat =itemView.findViewById(R.id.listWeight_TXT_pFat);
            listWeight_TXT_pMuscle = itemView.findViewById(R.id.listWeight_TXT_pMuscle);
            listWeight_TXT_weight =itemView.findViewById(R.id.listWeight_TXT_weight);
            listWeight_TXT_date =itemView.findViewById(R.id.listWeight_TXT_date);
            listWeight_IMG_pic= itemView.findViewById(R.id.listWeight_IMG_pic);

        }
    }
}