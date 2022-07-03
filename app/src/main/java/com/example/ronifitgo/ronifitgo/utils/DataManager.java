package com.example.ronifitgo.ronifitgo.utils;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;


import com.example.ronifitgo.ronifitgo.Activities.Activity_enter;
import com.example.ronifitgo.ronifitgo.Activities.Activity_main_user;
import com.example.ronifitgo.ronifitgo.Activities.Activity_sign_up;
import com.example.ronifitgo.ronifitgo.Object.Measure;
import com.example.ronifitgo.ronifitgo.Object.Tip;
import com.example.ronifitgo.ronifitgo.Object.User;
import com.example.ronifitgo.ronifitgo.Object.Weight;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.UUID;

public class DataManager {

    private final FirebaseAuth firebaseAuth;
    private final FirebaseFirestore dbFireStore;
    private final FirebaseStorage storage;
    private final FirebaseDatabase realTimeDB;


    private User currentUser;
    private String currentWeightUid;
    private String token;

    private static ArrayList<Weight> myWeights;
    private static ArrayList<Measure> myMeasures;
    private static ArrayList<Tip> tips;



    private static DataManager single_instance = null;

    private DataManager(){
        firebaseAuth = FirebaseAuth.getInstance();
        dbFireStore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        realTimeDB = FirebaseDatabase.getInstance("https://ronifitgo-default-rtdb.firebaseio.com/");

    }

    public static DataManager getInstance() {
        return single_instance;
    }

    public static void restart(){
        myWeights = new ArrayList<>();
        myMeasures = new ArrayList<>();

    }

    public static DataManager initHelper() {
        if (single_instance == null) {
            single_instance = new DataManager();
            myWeights = new ArrayList<>();
            myMeasures = new ArrayList<>();
            tips = new ArrayList<>();
        }
        return single_instance;
    }

    //Firebase Getters
    public FirebaseFirestore getDbFireStore() {
        return dbFireStore;
    }

    public FirebaseDatabase getRealTimeDB() {
        return realTimeDB;
    }

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    public FirebaseStorage getStorage() {
        return storage;
    }


    //My Data Base Helpers
    public User getCurrentUser() {
        return currentUser;
    }

    public DataManager setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
        return this;
    }
    public String getCurrentWeightUid() {
        return currentWeightUid;
    }

    public void setCurrentWeightUid(String currentWeightUid) {
        this.currentWeightUid = currentWeightUid;
    }

    public ArrayList<Weight> getMyWeights() {
        return myWeights;
    }

    public void setMyWeights(ArrayList<Weight> myWeights) {
        this.myWeights = myWeights;
    }

    public ArrayList<Measure> getMyMeasures() {
        return myMeasures;
    }

    public void setMyMeasures(ArrayList<Measure> myMeasures) {
        this.myMeasures = myMeasures;
    }

    public ArrayList<Tip> getTips() {
        return tips;
    }

    public void setTips(ArrayList<Tip> tips) {
        DataManager.tips = tips;
    }

    public void addNewWeight(Weight weight){
        myWeights.add(weight);
    }

    public void addNewMeasure(Measure measure){
        myMeasures.add(measure);
    }

    public void addNewTip(Tip tip){
        tips.add(tip);
    }

    public Weight getLastWeightByID(String id) {
        int i;
        for (i = 0; i < myWeights.size(); i++) {
            if (myWeights.get(i).getWeightId().equals(id)) {
                break;
            }
        }
        return myWeights.get(i);
    }
    //MyDataManager Methods

    public void addMeasureToUser() {

        DocumentReference docRef = dbFireStore.collection(KEYS.KEY_USERS).document(currentUser.getUserId());
        docRef.update("myMeasures", currentUser.getMyMeasures())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("pttt", "Data Updated Successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("pttt", "Error updating document", e);
                    }
                });
    }

    public void addWeightToUser() {

        DocumentReference docRef = dbFireStore.collection(KEYS.KEY_USERS).document(currentUser.getUserId());
        docRef.update("myWeights", currentUser.getMyWeights())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("pttt", "Data Updated Successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("pttt", "Error updating document", e);
                    }
                });
    }

    public void storeTipInDB(String tip) {
       Tip tempTip = new Tip(tip);
        dbFireStore.collection(KEYS.KEY_TIPS).document(tempTip.getTipId()).set(tempTip);
        DataManager.getInstance().addNewTip(tempTip);

    }




    }

