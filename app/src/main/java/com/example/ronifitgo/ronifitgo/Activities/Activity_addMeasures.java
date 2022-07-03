package com.example.ronifitgo.ronifitgo.Activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.ronifitgo.R;
import com.example.ronifitgo.ronifitgo.Object.Date;
import com.example.ronifitgo.ronifitgo.Object.Measure;
import com.example.ronifitgo.ronifitgo.Object.User;
import com.example.ronifitgo.ronifitgo.utils.DataManager;
import com.example.ronifitgo.ronifitgo.utils.KEYS;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

public class Activity_addMeasures extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private Calendar calendar;
    private TextInputEditText addMeasure_EDT_calender;
    private Button newMeasure_BTN_Save;
    private AppCompatImageView newMeasure_IMG_photo;
    private TextInputEditText newMeasure_TIEL_m1;
    private TextInputEditText newMeasure_TIEL_m2;
    private TextInputEditText newMeasure_TIEL_m3;
    private TextInputEditText newMeasure_TIEL_m4;
    private TextInputEditText newMeasure_TIEL_m5;
    private TextInputEditText newMeasure_TIEL_m6;

    private Date date;
    private final DataManager dataManager = DataManager.getInstance();
    private final FirebaseFirestore db = dataManager.getDbFireStore();
    private final User currentUser = DataManager.getInstance().getCurrentUser();
    private Measure tempMeasure = new Measure();
    private StorageReference storageRef;

    private String myDownloadUri;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmeasure);
        findViews();
        initButtons();


    }

    private void initButtons() {
        addMeasure_EDT_calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        newMeasure_IMG_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choseCover();
            }
        });

        newMeasure_BTN_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float armCirc = Float.parseFloat(newMeasure_TIEL_m1.getText().toString());
                float bustCirc = Float.parseFloat(newMeasure_TIEL_m2.getText().toString());
                float waistCirc = Float.parseFloat(newMeasure_TIEL_m3.getText().toString());
                float navelCirc = Float.parseFloat(newMeasure_TIEL_m4.getText().toString());
                float buttockCirc = Float.parseFloat(newMeasure_TIEL_m5.getText().toString());
                float hipCirc = Float.parseFloat(newMeasure_TIEL_m6.getText().toString());


                //tempMeasure = new Measure(armCirc, bustCirc, waistCirc, navelCirc, buttockCirc, hipCirc, date);
                tempMeasure.setArmCirc(armCirc).setBustCirc(bustCirc).setWaistCirc(waistCirc)
                        .setNavelCirc(navelCirc).setButtockCirc(buttockCirc).setHipCirc(hipCirc).setDate(date);

                if(myDownloadUri != null){
                    tempMeasure.setImgUrl(myDownloadUri);
                }
                currentUser.addToMeasuresUid(tempMeasure.getMeasureId());
                dataManager.addMeasureToUser();
                dataManager.addNewMeasure(tempMeasure);
                storeMeasureInDB(tempMeasure);
                Toast.makeText(getApplicationContext(), "מדידת ההקפים החדשה שלך נשמרה בהצלחה", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(Activity_addMeasures.this, Activity_main_user.class));

            }
        });
    }

    private void storeMeasureInDB(Measure tempMeasure) {
        //Add the item to the current grocery list
        db.collection(KEYS.KEY_USERS)
                .document(currentUser.getUserId())
                .collection(KEYS.KEY_MY_MEASURES)
                .document(tempMeasure.getMeasureId())
                .set(tempMeasure)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("pttt", "DocumentSnapshot Successfully written!");
                        //startActivity(new Intent(CreateListActivity.this, MainActivity.class));
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("pttt", "Error adding document", e);
                    }
                });

    }

    private void findViews() {
        addMeasure_EDT_calender = findViewById(R.id.addMeasure_EDT_calender);

        newMeasure_TIEL_m1 = findViewById(R.id.newMeasure_TIEL_m1);
        newMeasure_TIEL_m2 = findViewById(R.id.newMeasure_TIEL_m2);
        newMeasure_TIEL_m3 = findViewById(R.id.newMeasure_TIEL_m3);
        newMeasure_TIEL_m4 = findViewById(R.id.newMeasure_TIEL_m4);
        newMeasure_TIEL_m5 = findViewById(R.id.newMeasure_TIEL_m5);
        newMeasure_TIEL_m6 = findViewById(R.id.newMeasure_TIEL_m6);
        newMeasure_IMG_photo = findViewById(R.id.newMeasure_IMG_photo);
        newMeasure_BTN_Save = findViewById(R.id.newMeasure_BTN_Save);
    }

    public void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        month = month+1;
        String Sdate = dayOfMonth + "/" + month + "/" + year;
        date = new Date(month, year, dayOfMonth);
        addMeasure_EDT_calender.setText(Sdate);
    }

    @Override
    public void onBackPressed() {
        AlertDialog alertDialog = new AlertDialog.Builder(Activity_addMeasures.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("זהירות - לא שמרת!")
                .setMessage("אתה בטוח שאתה רוצה לצאת בלי לשמור את ההיקפים החדשים שלך?")
                .setPositiveButton("כן", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        startActivity(new Intent(Activity_addMeasures.this, Activity_main_user.class));

                    }
                })
                .setNegativeButton("לא", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }

    private void choseCover() {
        ImagePicker.with(Activity_addMeasures.this)
                .compress(1024)
                .crop(1f, 1f)
                .maxResultSize(1080, 1080)
                .start();
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        newMeasure_BTN_Save.setEnabled(false);

        StorageReference userRef = dataManager.getStorage()
                .getReference()
                .child(KEYS.KEY_MEASURES_PICTURES)
                .child(tempMeasure.getMeasureId());


        Uri uri = data.getData();

        newMeasure_IMG_photo.setImageURI(uri);
        newMeasure_IMG_photo.setDrawingCacheEnabled(true);
        newMeasure_IMG_photo.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) newMeasure_IMG_photo.getDrawable()).getBitmap();
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
                            newMeasure_BTN_Save.setEnabled(true);
                        }
                    });
                }
            }
        });
    }
}




