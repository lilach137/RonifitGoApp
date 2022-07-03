package com.example.ronifitgo.ronifitgo.Fragments;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.ronifitgo.R;
import com.example.ronifitgo.ronifitgo.Activities.Activity_main_user;
import com.example.ronifitgo.ronifitgo.Object.User;
import com.example.ronifitgo.ronifitgo.Object.Weight;
import com.example.ronifitgo.ronifitgo.utils.DataManager;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textview.MaterialTextView;
import com.google.api.Context;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

public class fragment_homePage extends Fragment{

    private DataManager dataManager = DataManager.getInstance();
    private FirebaseFirestore db = dataManager.getDbFireStore();
    private User currentUser = dataManager.getCurrentUser();



    private MaterialTextView profile_TXT_age, profile_TXT_height, profile_TXT_first , profile_TXT_goal,profile_TXT_weight;

    private ProgressBar weight_txt_progressBar;
    private ProgressBar muscle_txt_progressBar;
    private ProgressBar fat_txt_progressBar;

    private MaterialTextView profile_TXT_name;
    private TextView weight_txt;
    private TextView muscle_txt;
    private TextView fat_txt;
    private CircleImageView homePage_IMG_user;
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
        float goalWeight = 0;
        float fat = 0;
        float muscle = 0;

        profile_TXT_name.setText(dataManager.getCurrentUser().getName());
        Weight temp = new Weight();

        if (currentUser.getLastWeightId() != null) {
            temp = dataManager.getLastWeightByID(currentUser.getLastWeightId());
            goalWeight = ((currentUser.getFirstWeight() - temp.getWeight()) / (currentUser.getFirstWeight() - currentUser.getGoal()))*100;
            fat = temp.getpFat();
            muscle = temp.getpMuscle();
        }
        String newValue = Integer.toString((int)goalWeight);


        weight_txt_progressBar.setSecondaryProgress((int)goalWeight);
        weight_txt.setText(newValue + "%");
        muscle_txt_progressBar.setSecondaryProgress((int) muscle);
        muscle_txt.setText(String.valueOf(muscle) + "%");
        fat_txt_progressBar.setSecondaryProgress((int) fat);
        fat_txt.setText(String.valueOf(fat) + "%");
        profile_TXT_age.setText(profile_TXT_age.getText() + "  " + (currentUser.getAge()));
        profile_TXT_height.setText(profile_TXT_height.getText() + "  " + (currentUser.getHeight()));
        profile_TXT_first.setText(profile_TXT_first.getText() + "  " + (currentUser.getFirstWeight()));
        profile_TXT_goal.setText(profile_TXT_goal.getText() + "  " + (currentUser.getGoal()));
        if (currentUser.getLastWeightId() != null) {
            profile_TXT_weight.setText(profile_TXT_weight.getText() + "  " + (temp.getWeight()));
        } else {
            profile_TXT_weight.setText(profile_TXT_weight.getText() + "  " + String.valueOf(currentUser.getFirstWeight()));
        }
        if (getActivity() == null) {
            return;
        }
            Glide.with(getActivity())
                    .load(dataManager.getCurrentUser().getProfileImgUrl())
                    .into(homePage_IMG_user);
        }


    private void findViews(View view) {
        weight_txt_progressBar =view.findViewById(R.id.weight_txt_progressBar);
        muscle_txt_progressBar=view.findViewById(R.id.muscle_txt_progressBar);
        fat_txt_progressBar=view.findViewById(R.id.fat_txt_progressBar);
        weight_txt=view.findViewById(R.id.weight_txt);
        muscle_txt=view.findViewById(R.id.muscle_txt);
        fat_txt=view.findViewById(R.id.fat_txt);
        profile_TXT_name=view.findViewById(R.id.profile_TXT_name);
        profile_TXT_age =view.findViewById(R.id. profile_TXT_age);
        profile_TXT_height =view.findViewById(R.id.profile_TXT_height);
        profile_TXT_first = view.findViewById(R.id.profile_TXT_first);
        profile_TXT_goal =view.findViewById(R.id.profile_TXT_goal);
        profile_TXT_weight =view.findViewById(R.id.profile_TXT_weight);
        homePage_IMG_user = view.findViewById((R.id.homePage_IMG_user));


    }




}

