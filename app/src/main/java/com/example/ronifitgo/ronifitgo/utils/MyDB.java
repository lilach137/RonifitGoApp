package com.example.ronifitgo.ronifitgo.utils;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ronifitgo.ronifitgo.Activities.Activity_enter;
import com.example.ronifitgo.ronifitgo.Activities.Activity_main_user;
import com.example.ronifitgo.ronifitgo.Activities.Activity_sign_up;
import com.example.ronifitgo.ronifitgo.Object.Measure;
import com.example.ronifitgo.ronifitgo.Object.User;
import com.example.ronifitgo.ronifitgo.Object.Weight;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MyDB {

    private static ArrayList<Weight> myWeights;
    private static ArrayList<Measure> myMeasures;
    private static ArrayList<User> users;


    private static MyDB single_instance = null;

    public static MyDB getInstance() {
        return single_instance;
    }

    public static MyDB initHelper() {
        if (single_instance == null) {
            single_instance = new MyDB();
            myWeights = new ArrayList<>();
            myMeasures = new ArrayList<>();
            users =new ArrayList<>();
        }
        return single_instance;
    }

    public static ArrayList<User> getUsers() {
        return users;
    }

    public static void setUsers(ArrayList<User> users) {
        MyDB.users = users;
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


    public void loadUsers(){

        FirebaseFirestore db =DataManager.getInstance().getDbFireStore();
        CollectionReference myRef = db.collection(KEYS.KEY_USERS);
                myRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                User loadedUser = document.toObject(User.class);
                                users.add(loadedUser);
//                                myRef.document(loadedUser.getUserId()).collection(KEYS.KEY_MY_WEIGHTS)
//                                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                        if (task.isSuccessful()) {
//                                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                                Weight weight = document.toObject(Weight.class);
//                                                DataManager.getInstance().addNewWeight(weight);
//                                            }
//                                        } else {
//                                            Log.d("pttt", "Error getting documents: ", task.getException());
//                                        }
//                                    }
//                                });
//                                myRef.document(loadedUser.getUserId()).collection(KEYS.KEY_MY_MEASURES)
//                                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                        if (task.isSuccessful()) {
//                                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                               Measure measure = document.toObject(Measure.class);
//                                               DataManager.getInstance().addNewMeasure(measure);
//                                            }
//                                        } else {
//                                            Log.d("pttt", "Error getting documents: ", task.getException());
//                                        }
//                                    }
//                                });
                            }} else{
                                Log.d("pttt", "Error getting documents: ", task.getException());
                            }
                        }
                    });
    }
}