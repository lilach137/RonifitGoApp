package com.example.ronifitgo.ronifitgo.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ronifitgo.R;
import com.example.ronifitgo.ronifitgo.Adapters.Adapter_measures;
import com.example.ronifitgo.ronifitgo.Adapters.Adapter_weights;
import com.example.ronifitgo.ronifitgo.Object.Measure;
import com.example.ronifitgo.ronifitgo.Object.User;
import com.example.ronifitgo.ronifitgo.Object.Weight;
import com.example.ronifitgo.ronifitgo.utils.DataManager;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class fragment_measures extends Fragment{
    private static final String TAG = "pttt";
    private DataManager dataManager = DataManager.getInstance();
    private FirebaseFirestore db = dataManager.getDbFireStore();
    private User currentUser = dataManager.getCurrentUser();

    private DocumentReference myItemsRef;
    private String currentWeightUID;

    private AppCompatActivity activity;

    private RecyclerView fragment_RECYC_myMeasures;
    private ArrayList<Measure> myMeasuresArrayList;
    private Adapter_measures adapter_measures;


    public fragment_measures() {
    }

    public Fragment setActivity(AppCompatActivity activity) {
        this.activity = activity;
        return this;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_measures, container, false);

        findViews(view);
        initAdapter();
        return view;
    }


    private void findViews(View view) {
        fragment_RECYC_myMeasures = view.findViewById(R.id.fragment_RECYC_myMeasures);

    }
    private void initAdapter(){
        myMeasuresArrayList = new ArrayList<>();
        myMeasuresArrayList = dataManager.getMyMeasures();

        adapter_measures = new Adapter_measures(activity, myMeasuresArrayList);
        fragment_RECYC_myMeasures.setLayoutManager(new LinearLayoutManager(activity));
        fragment_RECYC_myMeasures.setHasFixedSize(true);
        fragment_RECYC_myMeasures.setItemAnimator(new DefaultItemAnimator());
        fragment_RECYC_myMeasures.setAdapter(adapter_measures);
    }
}
