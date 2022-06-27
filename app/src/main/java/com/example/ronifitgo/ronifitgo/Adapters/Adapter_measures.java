package com.example.ronifitgo.ronifitgo.Adapters;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ronifitgo.R;
import com.example.ronifitgo.ronifitgo.Object.Measure;
import com.example.ronifitgo.ronifitgo.Object.Weight;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class Adapter_measures extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Activity activity;
    private ArrayList<Measure> measures = new ArrayList<>();


    public Adapter_measures(Activity activity, ArrayList<Measure> measures) {
        this.activity = activity;
        this.measures = measures;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_measure, parent, false);
        MeasuresHolder holder = new MeasuresHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final MeasuresHolder holder = (MeasuresHolder) viewHolder;
        Measure measure= getMeasure(position);

        holder.listMeasure_TXT_dateTitle.setText(measure.getDate().toString());
        //holder.listMeasure_IMG_pic;
        holder.listMeasure_TXT_hipCirc.setText(String.valueOf(measure.getHipCirc()));
        holder.listMeasure_TXT_buttockCirc.setText(String.valueOf(measure.getButtockCirc()));
        holder.listMeasure_TXT_navelCirc.setText(String.valueOf(measure.getNavelCirc()));
        holder.listMeasure_TXT_waistCirc.setText(String.valueOf(measure.getWaistCirc()));
        holder.listMeasure_TXT_bustCirc.setText(String.valueOf(measure.getBustCirc()));
        holder.listMeasure_TXT_armCirc.setText(String.valueOf(measure.getArmCirc()));


    }

    @Override
    public int getItemCount() {
        return measures.size();
    }

    public Measure getMeasure(int position){
        return measures.get(position);
    }

    class MeasuresHolder extends RecyclerView.ViewHolder {

        private MaterialTextView listMeasure_TXT_dateTitle;
        private AppCompatImageView listMeasure_IMG_pic;
        private MaterialTextView listMeasure_TXT_hipCirc;
        private MaterialTextView listMeasure_TXT_buttockCirc;
        private MaterialTextView listMeasure_TXT_navelCirc;
        private MaterialTextView listMeasure_TXT_waistCirc;
        private MaterialTextView listMeasure_TXT_bustCirc;
        private MaterialTextView listMeasure_TXT_armCirc;

        public MeasuresHolder(@NonNull View itemView) {
            super(itemView);
            listMeasure_TXT_dateTitle =itemView.findViewById(R.id.listMeasure_TXT_dateTitle);
            listMeasure_IMG_pic =itemView.findViewById(R.id.listMeasure_IMG_pic);
            listMeasure_TXT_hipCirc = itemView.findViewById(R.id.listMeasure_TXT_hipCirc);
            listMeasure_TXT_buttockCirc =itemView.findViewById(R.id.listMeasure_TXT_buttockCirc);
            listMeasure_TXT_navelCirc =itemView.findViewById(R.id.listMeasure_TXT_navelCirc);
            listMeasure_TXT_waistCirc =itemView.findViewById(R.id.listMeasure_TXT_waistCirc);
            listMeasure_TXT_bustCirc =itemView.findViewById(R.id.listMeasure_TXT_bustCirc);
            listMeasure_TXT_armCirc = itemView.findViewById(R.id.listMeasure_TXT_armCirc );

        }
    }
}