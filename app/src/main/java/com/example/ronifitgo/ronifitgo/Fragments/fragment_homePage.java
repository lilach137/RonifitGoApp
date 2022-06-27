package com.example.ronifitgo.ronifitgo.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.ronifitgo.R;
import com.example.ronifitgo.ronifitgo.Object.User;
import com.example.ronifitgo.ronifitgo.utils.DataManager;
import com.example.ronifitgo.ronifitgo.utils.KEYS;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class fragment_homePage extends Fragment{
    private DataManager dataManager = DataManager.getInstance();
    private FirebaseFirestore db = dataManager.getDbFireStore();
    private User currentUser = dataManager.getCurrentUser();

    private DocumentReference myWeightRef;
    private String currentWeightUID;
    private Activity currentActivity;

    private ProgressBar weight_txt_progressBar;
    private ProgressBar muscle_txt_progressBar;
    private ProgressBar fat_txt_progressBar;

    private TextView weight_txt;
    private TextView muscle_txt;
    private TextView fat_txt;

    private AppCompatActivity activity;

    public Fragment setActivity(AppCompatActivity activity){
        this.activity=activity;
        return this;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);

        //currentWeightUID = dataManager.getCurrentWeightUid();

        findViews(view);
        initViews();

        return view;
    }

    private void initViews() {
        int goalWeight= 0 ;
        float fat = 0;
        float muscle =0 ;
        if (dataManager.getLastWeight()!=null) {
            goalWeight = (int) ((dataManager.getLastWeight().getWeight()) / (currentUser.getFirstWeight() - currentUser.getGoal()));
            fat = dataManager.getLastWeight().getpFat();
            muscle = dataManager.getLastWeight().getpMuscle();
        }

       weight_txt_progressBar.setSecondaryProgress((int) goalWeight);
       weight_txt.setText(String.valueOf(goalWeight));
       muscle_txt_progressBar.setSecondaryProgress((int)muscle);
       muscle_txt.setText(String.valueOf(muscle));
       fat_txt_progressBar.setSecondaryProgress((int)fat);
       fat_txt.setText(String.valueOf(fat));
    }


    private void findViews(View view) {
        weight_txt_progressBar =view.findViewById(R.id.weight_txt_progressBar);
        muscle_txt_progressBar=view.findViewById(R.id.muscle_txt_progressBar);
        fat_txt_progressBar=view.findViewById(R.id.fat_txt_progressBar);
        weight_txt=view.findViewById(R.id.weight_txt);
        muscle_txt=view.findViewById(R.id.muscle_txt);
        fat_txt=view.findViewById(R.id.fat_txt);
    }


}

