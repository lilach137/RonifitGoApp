package com.example.ronifitgo.ronifitgo.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ronifitgo.R;
import com.example.ronifitgo.ronifitgo.Activities.Activity_enter;
import com.example.ronifitgo.ronifitgo.Activities.Activity_main_coach;
import com.example.ronifitgo.ronifitgo.Activities.Activity_main_user;
import com.example.ronifitgo.ronifitgo.Activities.Activity_sign_up;
import com.example.ronifitgo.ronifitgo.Activities.Activity_trainer;
import com.example.ronifitgo.ronifitgo.Adapters.Adapter_trainer;
import com.example.ronifitgo.ronifitgo.Object.Measure;
import com.example.ronifitgo.ronifitgo.Object.User;
import com.example.ronifitgo.ronifitgo.Object.Weight;
import com.example.ronifitgo.ronifitgo.utils.DataManager;
import com.example.ronifitgo.ronifitgo.utils.KEYS;
import com.example.ronifitgo.ronifitgo.utils.MyDB;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class fragment_allTrainers extends Fragment {
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Fragment[] panel_fragments;

    private DataManager dataManager = DataManager.getInstance();
    private FirebaseFirestore db = dataManager.getDbFireStore();
    private User currentUser = dataManager.getCurrentUser();

    private DocumentReference myItemsRef;
    private String currentWeightUID;

    private AppCompatActivity activity;

    private RecyclerView fragment_RECYC_myUsers;
    private ArrayList<User> allTrainers = MyDB.getUsers();
    private Adapter_trainer adapter_trainer;

    public fragment_allTrainers() {
    }

    public Fragment setActivity(AppCompatActivity activity) {
        this.activity = activity;
        return this;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_trainers, container, false);

        findViews(view);
        initAdapter();
        return view;
    }


    private void findViews(View view) {
        fragment_RECYC_myUsers = view.findViewById(R.id.fragment_RECYC_myUsers);

    }

    private void initAdapter() {
        allTrainers = new ArrayList<>();
        allTrainers = MyDB.getUsers();

        adapter_trainer = new Adapter_trainer(activity, allTrainers);
        fragment_RECYC_myUsers.setLayoutManager(new LinearLayoutManager(activity));
        fragment_RECYC_myUsers.setHasFixedSize(true);
        fragment_RECYC_myUsers.setItemAnimator(new DefaultItemAnimator());
        fragment_RECYC_myUsers.setAdapter(adapter_trainer);

        adapter_trainer.setTrainerListener(new Adapter_trainer.TrainerListener() {
            @Override
            public void click(User trainer, int position) {
                DataManager.restart();
                Log.d("user", trainer.getUserId().toString());
                loadUserInfo(trainer.getUserId());


            }
        });

    }


    public void loadUserInfo(String id) {

        DocumentReference myRef = db.collection(KEYS.KEY_USERS).document(id);
        myRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Log.d("pttt", "DocumentSnapshot data: " + documentSnapshot.getData());
                    User loadedUser = documentSnapshot.toObject(User.class);
                    DataManager.getInstance().setCurrentUser(loadedUser);
                    myRef.collection(KEYS.KEY_MY_WEIGHTS).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    DataManager.getInstance().addNewWeight(document.toObject(Weight.class));
                                    Log.d("pttt", document.getId() + " => " + document.getData());
                                }
                            } else {
                                Log.d("pttt", "Error getting documents: ", task.getException());
                            }
                        }
                    });
                    myRef.collection(KEYS.KEY_MY_MEASURES).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    DataManager.getInstance().addNewMeasure(document.toObject(Measure.class));
                                    Log.d("pttt", document.getId() + " => " + document.getData());
                                }

                                startActivity(new Intent(getActivity(), Activity_trainer.class));
                        ((Activity) getActivity()).overridePendingTransition(0, 0);
                        getActivity().finish();
                            } else {
                                Log.d("pttt", "Error getting documents: ", task.getException());
                            }
                        }

                    });


                } else {
                    Log.d("pttt", "No such document");

                }

            }


        });

    }


}