package com.example.ronifitgo.ronifitgo;



import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;


import com.example.ronifitgo.ronifitgo.Object.Measure;
import com.example.ronifitgo.ronifitgo.Object.User;
import com.example.ronifitgo.ronifitgo.Object.Weight;
import com.example.ronifitgo.ronifitgo.utils.DataManager;
import com.example.ronifitgo.R;
import com.example.ronifitgo.ronifitgo.utils.KEYS;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;


public class Activity_enter extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private FirebaseFirestore db;
    private MaterialButton enter_BTN_login;
    private MaterialButton enter_BTN_welcome;

    List<AuthUI.IdpConfig> providers = Arrays.asList(new AuthUI.IdpConfig.PhoneBuilder().build());

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);



        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mUser = mAuth.getCurrentUser();
        if (mUser != null) {
            reload();
        }
        initButton();
    }

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignInResult(result);
                }
            }
    );

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
            enter_BTN_login.setVisibility(View.INVISIBLE);
            enter_BTN_welcome.setVisibility(View.VISIBLE);
            reload();
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
        }
    }


    public void initButton(){
        enter_BTN_login = findViewById(R.id.enter_BTN_login);
        enter_BTN_welcome = findViewById(R.id.enter_BTN_welcome);
        enter_BTN_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
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
                        for (String weightId : loadedUser.getMyWeights()){
                            myRef.collection(KEYS.KEY_MY_WEIGHTS).document(weightId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    Weight weight = documentSnapshot.toObject(Weight.class);
                                    DataManager.getInstance().addNewWeight(weight);
                                }
                            });
                        }
                    for (String measureId : loadedUser.getMyMeasures()){
                        myRef.collection(KEYS.KEY_MY_MEASURES).document(measureId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Measure measure = documentSnapshot.toObject(Measure.class);
                                DataManager.getInstance().addNewMeasure(measure);
                            }
                        });
                    }

                    startActivity(new Intent(Activity_enter.this, MainActivity.class));
                } else {
                    Log.d("pttt", "No such document");
                    Log.d("pttt", user.getUid());
                    startActivity(new Intent(Activity_enter.this,Activity_sign_up.class));
                }
                finish();
            }

        });


    }

}



