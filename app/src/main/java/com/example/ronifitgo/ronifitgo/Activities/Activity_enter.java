package com.example.ronifitgo.ronifitgo.Activities;



import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.airbnb.lottie.LottieAnimationView;
import com.example.ronifitgo.ronifitgo.Object.Measure;
import com.example.ronifitgo.ronifitgo.Object.Tip;
import com.example.ronifitgo.ronifitgo.Object.User;
import com.example.ronifitgo.ronifitgo.Object.Weight;
import com.example.ronifitgo.ronifitgo.utils.DataManager;
import com.example.ronifitgo.R;
import com.example.ronifitgo.ronifitgo.utils.KEYS;
import com.example.ronifitgo.ronifitgo.utils.MyDB;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.List;


public class Activity_enter extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private FirebaseFirestore db;
    private MaterialButton enter_BTN_trainer;
    private MaterialButton enter_BTN_coach;
    private LottieAnimationView loading_animation;
    private PopupWindow popupWindow;
    private MaterialButton dialog_BTN_signIn;
    private TextInputEditText dialog_EDT_pass;
    private String password;
    List<AuthUI.IdpConfig> providers = Arrays.asList(new AuthUI.IdpConfig.PhoneBuilder().build());

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignInResult(result);
                }
            }
    );

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);
        findView();
        loading_animation.setVisibility(View.INVISIBLE);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        if(DataManager.getInstance().getTips().size()==0)
            loadTips();

        mUser = mAuth.getCurrentUser();
        if (mUser != null) {
            enter_BTN_trainer.setText("כיף שחזרת אלינו!");
            enter_BTN_coach.setVisibility(View.INVISIBLE);
            reload();

        }
        initButton();
    }



    private void signIn(){

    Intent signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setLogo(R.drawable.icon)
            .setTheme(R.style.Theme_RonifitGo)
            .build();
    signInLauncher.launch(signInIntent);
}

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            loading_animation.setVisibility(View.VISIBLE);
            enter_BTN_coach.setVisibility(View.INVISIBLE);
            enter_BTN_trainer.setVisibility(View.INVISIBLE);
            Handler handler = new Handler();

            handler.postDelayed(new Runnable() {
                public void run() {
                    finish();
                }
            }, 1500);
            reload();


        } else {
        }
    }

    public void findView(){
        enter_BTN_trainer = findViewById(R.id.enter_BTN_trainer);
        enter_BTN_coach = findViewById(R.id.enter_BTN_coach);
        loading_animation = findViewById(R.id.loading_animation);
    }

    public void initButton(){

        enter_BTN_trainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        enter_BTN_coach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDB.getInstance().loadUsers();
                onButtonShowPopupWindowClick(v);

            }
        });
    }

    public void reload(){
        //Store the user UID by Phone number
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference myRef = db.collection(KEYS.KEY_USERS).document(user.getUid());
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
                                finish();
                                startActivity(new Intent(Activity_enter.this, Activity_main_user.class));
                            } else {
                                Log.d("pttt", "Error getting documents: ", task.getException());
                            }
                        }

                    });


                } else {
                    Log.d("pttt", "No such document");
                    Log.d("pttt", user.getUid());
                    finish();
                    startActivity(new Intent(Activity_enter.this,Activity_sign_up.class));
                }

            }


        });


    }


    public void onButtonShowPopupWindowClick(View view) {

        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.dialog_password, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        popupWindow = new PopupWindow(popupView, 100, 100, focusable);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        dialog_BTN_signIn =(MaterialButton) popupView.findViewById(R.id.dialog_BTN_signIn);
        dialog_EDT_pass = (TextInputEditText) popupView.findViewById(R.id.dialog_EDT_pass);;


        dialog_BTN_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password = dialog_EDT_pass.getText().toString();
                if (password.equals("ronigilad")){
                    startActivity(new Intent(Activity_enter.this, Activity_main_coach.class));
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(),"הסיסמא שגויה, נסה שוב", Toast.LENGTH_SHORT).show();

            }
            }
        });


    }


    public void loadTips(){
        db.collection(KEYS.KEY_TIPS).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        DataManager.getInstance().addNewTip(document.toObject(Tip.class));
                        Log.d("pttt", document.getId() + " => " + document.getData());
                    }
                } else {
                    Log.d("pttt", "Error getting documents: ", task.getException());
                }
            }
        });

    }
}



