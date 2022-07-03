package com.example.ronifitgo.ronifitgo.Activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ronifitgo.R;
import com.example.ronifitgo.ronifitgo.Object.Date;
import com.example.ronifitgo.ronifitgo.Object.User;
import com.example.ronifitgo.ronifitgo.Object.Weight;
import com.example.ronifitgo.ronifitgo.utils.DataManager;
import com.example.ronifitgo.ronifitgo.utils.KEYS;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;

public class Activity_addWeight extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private Calendar calendar;
    private TextInputEditText newWeight_EDT_calender,newWeight_EDT_weight;
    private TextInputEditText newWeight_EDT_pFat, newWeight_EDT_pMuscle;
    private Button newWeight_BTN_Save;
    float count_weight = 60;
    float count_pFat = 53;
    float count_pMuscle = 25;

    private Date date;
    private final DataManager dataManager = DataManager.getInstance();
    private final FirebaseFirestore db = dataManager.getDbFireStore();
    private final User currentUser = DataManager.getInstance().getCurrentUser();
    private Weight tempWeight;

    NumberFormat formatter = new DecimalFormat("0.0");
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addweight);
        findViews();
        initButtons();
    }

    private void initButtons() {
        newWeight_EDT_calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        newWeight_BTN_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }

    private void storeWeightInDB(Weight tempWeight) {
        db.collection(KEYS.KEY_USERS)
                .document(currentUser.getUserId())
                .collection(KEYS.KEY_MY_WEIGHTS)
                .document(tempWeight.getWeightId())
                .set(tempWeight)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("pttt", "DocumentSnapshot Successfully written!");
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("pttt", "Error adding document", e);
                    }
                });
    }

        private void storeLastWeightIDInDB(String weightId){
            db.collection(KEYS.KEY_USERS)
                    .document(currentUser.getUserId())
                    .update("lastWeightId",weightId)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("pttt", "DocumentSnapshot Successfully written!");
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


        newWeight_EDT_calender = findViewById(R.id.newWeight_EDT_calender);
        newWeight_BTN_Save = findViewById(R.id.newWeight_BTN_save);
        newWeight_EDT_pMuscle= findViewById(R.id.newWeight_EDT_pMuscle);
        newWeight_EDT_pFat= findViewById(R.id.newWeight_EDT_pFat);
        newWeight_EDT_weight = findViewById(R.id.newWeight_EDT_weight);


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
        month = month+1;
        String Sdate = dayOfMonth + "/" + month + "/" + year;
        date = new Date(month,year,dayOfMonth);
        newWeight_EDT_calender.setText(Sdate);
    }

    @Override
    public void onBackPressed() {
        AlertDialog alertDialog = new AlertDialog.Builder(Activity_addWeight.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("זהירות - לא שמרת!")
                .setMessage("אתה בטוח שאתה רוצה לצאת בלי לשמור את השקילה החדשה שלך?")
                .setPositiveButton("כן", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        startActivity(new Intent(Activity_addWeight.this, Activity_main_user.class));

                    }
                })
                .setNegativeButton("לא", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }

    public void saveData(){
        newWeight_EDT_weight.setInputType(InputType.TYPE_CLASS_NUMBER);
        newWeight_EDT_pFat.setInputType(InputType.TYPE_CLASS_NUMBER);
        newWeight_EDT_pMuscle.setInputType(InputType.TYPE_CLASS_NUMBER);
        count_weight = Float.parseFloat(newWeight_EDT_weight.getText().toString());
        count_pFat = Float.parseFloat( newWeight_EDT_pFat.getText().toString());
        count_pMuscle = Float.parseFloat( newWeight_EDT_pMuscle.getText().toString());

        tempWeight = new Weight(count_pFat,count_pMuscle,count_weight,date);


        currentUser.setLastWeightId(tempWeight.getWeightId());
        currentUser.addToWeightsUid(tempWeight.getWeightId());
        dataManager.addWeightToUser();
        dataManager.addNewWeight(tempWeight);
        storeLastWeightIDInDB(tempWeight.getWeightId());
        storeWeightInDB(tempWeight);
        Toast.makeText(getApplicationContext(),"השקילה השבועית החדשה שלך נשמרה בהצלחה", Toast.LENGTH_SHORT).show();
        finish();
        startActivity(new Intent(Activity_addWeight.this, Activity_main_user.class));
    }


}





