package com.example.ronifitgo.ronifitgo.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;

import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ronifitgo.R;
import com.example.ronifitgo.ronifitgo.Object.User;
import com.example.ronifitgo.ronifitgo.utils.DataManager;
import com.example.ronifitgo.ronifitgo.utils.KEYS;
import com.github.dhaval2404.imagepicker.ImagePicker;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import de.hdodenhof.circleimageview.CircleImageView;


public class Activity_sign_up extends AppCompatActivity {


    private CircleImageView signup_IMG_user;
    private Button signUp_BTN_save;
    private TextView signUp_TXT_height;
    private TextInputEditText signUp_EDT_name,signUp_EDT_weight,signUp_EDT_goal;
    private TextInputEditText signUp_EDT_age;
    private FloatingActionButton signup_FAB_profile_pic;

    private final DataManager dataManager = DataManager.getInstance();
    private final FirebaseFirestore db = dataManager.getDbFireStore();
    private final FirebaseDatabase realtimeDB = dataManager.getRealTimeDB();


    NumberFormat formatter = new DecimalFormat("0.0");

    private User tempMyUser;

    private float height,weight,goal;
    private int age;

    private String myDownloadUri;
    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

       findView();
       initButton();

       CheckSeekbarStatus();

    }

    private void choseCover() {
        ImagePicker.with(Activity_sign_up.this)
                .compress(1024)
                .crop(1f, 1f)
                .maxResultSize(1080, 1080)
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        signUp_BTN_save.setEnabled(false);

        StorageReference userRef = dataManager.getStorage()
                .getReference()
                .child(KEYS.KEY_PROFILE_PICTURES)
                .child(dataManager.getFirebaseAuth().getCurrentUser().getUid());


        Uri uri = data.getData();
        
        signup_IMG_user.setImageURI(uri);
        signup_IMG_user.setDrawingCacheEnabled(true);
        signup_IMG_user.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) signup_IMG_user.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();

        //Start The upload task
        UploadTask uploadTask = userRef.putBytes(bytes);
        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    //If upload was successful, We want to get the image url from the storage
                    userRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            myDownloadUri = uri.toString();
                            signUp_BTN_save.setEnabled(true);
                        }
                    });
                }
            }
        });

    }


    private void CheckSeekbarStatus() {

        SeekBar Seekbar = findViewById(R.id.Seekbar);
        Seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String ht = progress + " " +getResources().getString(R.string.cm);
                signUp_TXT_height.setText(ht);
                height = (float)(progress)/100;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void saveData() {

        String userID = dataManager.getFirebaseAuth().getCurrentUser().getUid();
        String userName = signUp_EDT_name.getText().toString(); ;
        String userPhone = dataManager.getFirebaseAuth().getCurrentUser().getPhoneNumber();
        goal =Float.parseFloat(signUp_EDT_goal.getText().toString());
        weight =Float.parseFloat(signUp_EDT_weight.getText().toString());
        age = Integer.valueOf(signUp_EDT_age.getText().toString());
        tempMyUser = new User(userID, userPhone,age,weight,height,userName,goal);

        if(myDownloadUri != null){
            tempMyUser.setProfileImgUrl(myDownloadUri);
        }
        DataManager.getInstance().setCurrentUser(tempMyUser);
        storeUserInDB(tempMyUser);
    }

    private void storeUserInDB(User userToStore) {
        //Store the user UID by Phone number
        DatabaseReference myRef = realtimeDB.getReference(KEYS.KEY_PHONE_TO_UID).child(userToStore.getPhoneNumber());
        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    myRef.setValue(userToStore.getUserId());
                }
            }
        });

        db.collection(KEYS.KEY_USERS)
                .document(userToStore.getUserId())
                .set(userToStore)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("pttt", "DocumentSnapshot Successfully written!");
                        startActivity(new Intent(Activity_sign_up.this, Activity_main_user.class));
                        finish();
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("pttt", "Error adding document", e);
                    }
                });
    }

    public void initButton(){

      
        signup_FAB_profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choseCover();
            }
        });

        signup_IMG_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choseCover();
            }
        });
        signUp_BTN_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                Toast.makeText(getApplicationContext(),"הפרטים שלך נשמרו בהצלחה!", Toast.LENGTH_SHORT).show();

            }
        });


    }

    public void findView(){
        signup_IMG_user = findViewById(R.id. signup_IMG_user);
        signUp_BTN_save = findViewById(R.id. signUp_BTN_save);
        signUp_TXT_height = findViewById(R.id.signUp_TXT_height);
        signUp_EDT_age = findViewById(R.id.signUp_EDT_age);
        signUp_EDT_name = findViewById(R.id.signUp_EDT_name);
        signUp_EDT_weight = findViewById(R.id.signUp_EDT_weight);
        signUp_EDT_goal = findViewById(R.id.signUp_EDT_goal);
        signup_FAB_profile_pic = findViewById(R.id.signup_FAB_profile_pic);
    }


    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dataManager.getFirebaseAuth().signOut();
    }



}
