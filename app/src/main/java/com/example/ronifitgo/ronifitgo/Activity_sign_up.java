package com.example.ronifitgo.ronifitgo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AutomaticZenRule;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;

import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

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


public class Activity_sign_up extends AppCompatActivity {

    private TextView female_text;
    private TextView male_text;
    private ImageView signup_IMG_user;
    private Button calculate;
    private CardView card_female;
    private CardView card_male;
    private TextView height_txt,age;
    private TextInputEditText login_LBL_name;
    private TextInputEditText login_LBL_goal;
    private FloatingActionButton signup_FAB_profile_pic;

    private final DataManager dataManager = DataManager.getInstance();
    private final FirebaseFirestore db = dataManager.getDbFireStore();
    private final FirebaseDatabase realtimeDB = dataManager.getRealTimeDB();


    NumberFormat formatter = new DecimalFormat("0.0");

    private User tempMyUser;

    private float height;

    float count_weight = 50;
    int count_age = 19;
    private RelativeLayout weight_plus, weight_minus, age_plus, age_minus;
    int gender = 2;
    boolean male_clk = true, female_clk = true, check1 = true, check2 = true;
    private String myDownloadUri;
    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

       findView();
       initButton();

       CheckSeekbarStatus();
       CheckWeight();
       CheckAge();

    }
    private void choseCover() {
        ImagePicker.with(Activity_sign_up.this)
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .crop(1f, 1f)
                .maxResultSize(1080, 1080)
                //Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        calculate.setEnabled(false);

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
                            //Set the profile URL to the object we created
                            myDownloadUri = uri.toString();
                            //View Indicates the process of the image uploading Done by making the button Enabled
                            
                            calculate.setEnabled(true);
                        }
                    });
                }
            }
        });

    }
    private void CheckAge() {

        age_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count_age++;
                age.setText(String.valueOf(count_age));
            }
        });

        age_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count_age--;
                age.setText(String.valueOf(count_age));
            }
        });
    }

    private void CheckWeight() {

        final TextView weight_txt = findViewById(R.id.weight);

        weight_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count_weight += 0.1;

                String formmatedFloatValue = formatter.format(count_weight);
                weight_txt.setText(formmatedFloatValue);
            }
        });

        weight_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count_weight -= 0.1;

                String formmatedFloatValue = formatter.format(count_weight);
                weight_txt.setText(formmatedFloatValue);
            }
        });

    }

    private void CheckSeekbarStatus() {

        SeekBar Seekbar = findViewById(R.id.Seekbar);
        Seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String ht = progress + getResources().getString(R.string.cm);
                height_txt.setText(ht);
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
        if(female_clk){
            gender = 1;
        }
        String userID = dataManager.getFirebaseAuth().getCurrentUser().getUid();
        String userName = login_LBL_name.getText().toString(); ;
        String userPhone = dataManager.getFirebaseAuth().getCurrentUser().getPhoneNumber();
        float goal =Float.parseFloat(login_LBL_goal.getText().toString());
        tempMyUser = new User(userID, userPhone,count_age,count_weight,height,userName,gender,goal);
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

        //Store the user in Firestore by UID when stored successfully move to Main Activity
        db.collection(KEYS.KEY_USERS)
                .document(userToStore.getUserId())
                .set(userToStore)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("pttt", "DocumentSnapshot Successfully written!");
                        startActivity(new Intent(Activity_sign_up.this, MainActivity.class));
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
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        card_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check1) {

                    if (male_clk) {

                        male_text.setTextColor(Color.parseColor("#FFFFFF"));
                        male_text.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.male_black,0,0);
                        male_clk = false;
                        check2 = false;

                    } else {

                        male_text.setTextColor(Color.parseColor("#8D8E99"));
                        male_text.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.male,0,0);
                        male_clk = true;
                        check2 = true;
                    }
                }
            }
        });
        card_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check2) {
                    if (female_clk) {
                        female_text.setTextColor(Color.parseColor("#FFFFFF"));
                        female_text.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.female_black,0,0);
                        female_clk = false;
                        check1 = false;
                    }
                    else  {

                        female_text.setTextColor(Color.parseColor("#8D8E99"));
                        female_text.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.female,0,0);
                        female_clk = true;
                        check1 = true;
                    }
                }
            }
        });
    }

    public void findView(){
        height_txt = findViewById(R.id.height_txt);
        signup_IMG_user =findViewById(R.id.signup_IMG_user);
        female_text = findViewById(R.id.female);
        male_text = findViewById(R.id.male);
        calculate = findViewById(R.id.calculate);
        card_female = findViewById(R.id.cardView_female);
        card_male = findViewById(R.id.cardView_male);
        signup_FAB_profile_pic = findViewById(R.id.signup_FAB_profile_pic);
        age_minus = findViewById(R.id.age_minus1);
        age_plus = findViewById(R.id.age_plus1);
        age = findViewById(R.id.age);
        weight_minus = findViewById(R.id.weight_minus);
        weight_plus = findViewById(R.id.weight_plus);
        login_LBL_name = findViewById(R.id.login_LBL_name);
        login_LBL_goal = findViewById(R.id.login_LBL_goal);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //TODO erase UID doc from DB
        dataManager.getFirebaseAuth().signOut();
    }
}
