package com.example.ronifitgo.ronifitgo;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ronifitgo.R;
import com.example.ronifitgo.ronifitgo.Object.Date;
import com.example.ronifitgo.ronifitgo.Object.Measure;
import com.example.ronifitgo.ronifitgo.Object.User;
import com.example.ronifitgo.ronifitgo.utils.DataManager;
import com.example.ronifitgo.ronifitgo.utils.KEYS;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Activity_addMeasures extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private Calendar calendar;
    private EditText newMeasure_TIL_calender;
    private MaterialButton newMeasure_BTN_Save;

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
    private Measure tempMeasure;
    private StorageReference storageRef;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmeasure);
        findViews();
        initButtons();



    }

    private void initButtons() {
        newMeasure_TIL_calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        newMeasure_BTN_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float armCirc = Float.parseFloat(newMeasure_TIEL_m1.getText().toString());
                float bustCirc= Float.parseFloat(newMeasure_TIEL_m2.getText().toString());
                float waistCirc=Float.parseFloat(newMeasure_TIEL_m3.getText().toString());
                float navelCirc =Float.parseFloat(newMeasure_TIEL_m4.getText().toString());
                float buttockCirc =Float.parseFloat(newMeasure_TIEL_m5.getText().toString());
                float hipCirc =Float.parseFloat(newMeasure_TIEL_m6.getText().toString());


                tempMeasure = new Measure(armCirc,bustCirc,waistCirc,navelCirc,buttockCirc,hipCirc,date);

                currentUser.addToMeasuresUid(tempMeasure.getMeasureId());
                dataManager.addMeasureToUser();
                dataManager.addNewMeasure(tempMeasure);
                storeMeasureInDB(tempMeasure);
                Toast.makeText(getApplicationContext(),"מדידת ההקפים החדשה שלך נשמרה בהצלחה", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Activity_addMeasures.this, MainActivity.class));
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
        newMeasure_TIL_calender = findViewById(R.id.newMeasure_TIL_calender);

        newMeasure_TIEL_m1=findViewById(R.id.newMeasure_TIEL_m1);
        newMeasure_TIEL_m2=findViewById(R.id.newMeasure_TIEL_m2);
        newMeasure_TIEL_m3=findViewById(R.id.newMeasure_TIEL_m3);
        newMeasure_TIEL_m4=findViewById(R.id.newMeasure_TIEL_m4);
        newMeasure_TIEL_m5=findViewById(R.id.newMeasure_TIEL_m5);
        newMeasure_TIEL_m6=findViewById(R.id.newMeasure_TIEL_m6);

        newMeasure_BTN_Save = findViewById(R.id.newMeasure_BTN_Save);
    }

    public void showDatePickerDialog(){
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
        String Sdate = dayOfMonth + "/" + month+ "/" + year;
        date = new Date(month,year,dayOfMonth);
        newMeasure_TIL_calender.setText(Sdate);
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
                        startActivity(new Intent(Activity_addMeasures.this, MainActivity.class));
                        finish();
                    }
                })
                .setNegativeButton("לא", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }
}





