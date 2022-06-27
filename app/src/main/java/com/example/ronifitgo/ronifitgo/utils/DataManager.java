package com.example.ronifitgo.ronifitgo.utils;

import android.util.Log;

import androidx.annotation.NonNull;


import com.example.ronifitgo.ronifitgo.Object.Measure;
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
    private static Weight lastWeight;


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

    public static DataManager initHelper() {
        if (single_instance == null) {
            single_instance = new DataManager();
            myWeights = new ArrayList<>();
            myMeasures = new ArrayList<>();
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

    public Weight getLastWeight() {
        return lastWeight;
    }

    public DataManager setLastWeight(Weight lastWeight) {
        DataManager.lastWeight = lastWeight;
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

    public void addNewWeight(Weight weight){
        myWeights.add(weight);
    }

    public void addNewMeasure(Measure measure){
        myMeasures.add(measure);
    }

    //MyDataManager Methods

    /**
     * Method will load the connected user's data from database. and update his current device token for Cloud messaging
     */
    public void loadUserFromDB() {
        // Successfully signed in

        FirebaseUser user = firebaseAuth.getCurrentUser();
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                token = s;
                DatabaseReference myRef = getRealTimeDB().getReference(KEYS.KEY_UID_TO_TOKENS).child(user.getUid());
                myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful()){
                            myRef.setValue(token);
                            Log.d("pttt", "token is : " + token);
                        }
                    }
                });
            }
        });


        DocumentReference docRef = dbFireStore.collection(KEYS.KEY_USERS).document(user.getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Log.d("pttt", "DocumentSnapshot data: " + documentSnapshot.getData());
                    User loadedUser = documentSnapshot.toObject(User.class);
                    setCurrentUser(loadedUser);

                } else {
                    Log.d("pttt", "No such document");
                    Log.d("pttt", user.getUid().toString());
                }

            }

        });
    }

    /**
     * Method which will be called whenever there is a change in the user's list of lists and need to update the database about the change
     */
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


}