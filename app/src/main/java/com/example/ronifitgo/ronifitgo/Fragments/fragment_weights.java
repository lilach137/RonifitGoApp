package com.example.ronifitgo.ronifitgo.Fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.example.ronifitgo.R;
import com.example.ronifitgo.ronifitgo.Adapters.Adapter_weights;
import com.example.ronifitgo.ronifitgo.Object.User;
import com.example.ronifitgo.ronifitgo.Object.Weight;
import com.example.ronifitgo.ronifitgo.utils.DataManager;
import com.example.ronifitgo.ronifitgo.utils.KEYS;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class fragment_weights extends Fragment {

    private static final String TAG = "pttt";
    private DataManager dataManager = DataManager.getInstance();
    private FirebaseFirestore db = dataManager.getDbFireStore();
    private User currentUser = dataManager.getCurrentUser();

    private DocumentReference myItemsRef;
    private String currentWeightUID;

    private AppCompatActivity activity;

    private RecyclerView fragment_RECYC_myWeights;
    private ArrayList<Weight> myWeightsArrayList;
    private Adapter_weights adapter_weights;


    public fragment_weights() {
    }

    public Fragment setActivity(AppCompatActivity activity) {
        this.activity = activity;
        return this;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_weights, container, false);

        findViews(view);
        initAdapter();
        return view;
    }


    private void findViews(View view) {
        fragment_RECYC_myWeights = view.findViewById(R.id.fragment_RECYC_myWeights);

    }
    private void initAdapter(){
        myWeightsArrayList = new ArrayList<>();
        myWeightsArrayList = dataManager.getMyWeights();

        adapter_weights = new Adapter_weights(activity, myWeightsArrayList);
        fragment_RECYC_myWeights.setLayoutManager(new LinearLayoutManager(activity));
        fragment_RECYC_myWeights.setHasFixedSize(true);
        fragment_RECYC_myWeights.setItemAnimator(new DefaultItemAnimator());
        fragment_RECYC_myWeights.setAdapter(adapter_weights);
    }


}