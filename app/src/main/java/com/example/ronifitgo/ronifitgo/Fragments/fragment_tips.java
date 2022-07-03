package com.example.ronifitgo.ronifitgo.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ronifitgo.R;
import com.example.ronifitgo.ronifitgo.Adapters.Adapter_tip;
import com.example.ronifitgo.ronifitgo.Adapters.Adapter_weights;
import com.example.ronifitgo.ronifitgo.Object.Tip;
import com.example.ronifitgo.ronifitgo.Object.User;
import com.example.ronifitgo.ronifitgo.Object.Weight;
import com.example.ronifitgo.ronifitgo.utils.DataManager;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class fragment_tips extends Fragment {


    private DataManager dataManager = DataManager.getInstance();
    private FirebaseFirestore db = dataManager.getDbFireStore();
    private User currentUser = dataManager.getCurrentUser();

    private MaterialTextView listTip_LBL_tip;
    private AppCompatActivity activity;
    private RecyclerView fragment_RECYC_tips;
    private ArrayList<Tip> tips;
    private Adapter_tip adapter_tip;

    public fragment_tips() {
    }

    public Fragment setActivity(AppCompatActivity activity){
        this.activity=activity;
        return this;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tips, container, false);

        findViews(view);
        initViews();
        return view;
    }

    private void initViews() {
        tips = new ArrayList<>();
        tips = dataManager.getTips();


        adapter_tip = new Adapter_tip(activity,tips);
        fragment_RECYC_tips.setLayoutManager(new LinearLayoutManager(activity));
        fragment_RECYC_tips.setHasFixedSize(true);
        fragment_RECYC_tips.setItemAnimator(new DefaultItemAnimator());
        fragment_RECYC_tips.setAdapter(adapter_tip);
    }

    private void findViews(View view) {
        fragment_RECYC_tips = view.findViewById(R.id.fragment_RECYC_tips);

    }

}
