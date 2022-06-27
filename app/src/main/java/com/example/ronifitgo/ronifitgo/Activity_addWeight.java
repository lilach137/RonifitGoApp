package com.example.ronifitgo.ronifitgo;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ronifitgo.R;
import com.example.ronifitgo.ronifitgo.Object.Date;
import com.example.ronifitgo.ronifitgo.Object.Measure;
import com.example.ronifitgo.ronifitgo.Object.User;
import com.example.ronifitgo.ronifitgo.Object.Weight;
import com.example.ronifitgo.ronifitgo.utils.DataManager;
import com.example.ronifitgo.ronifitgo.utils.KEYS;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;

public class Activity_addWeight extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private Calendar calendar;
    private EditText newWeight_TIL_calender,newWeight_TIL_weight;
    private TextInputEditText newWeight_LBL_muscleTitle, newWeight_LBL_fatTitle;
    private MaterialButton newWeight_BTN_Save;
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
        newWeight_TIL_calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        newWeight_BTN_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newWeight_TIL_weight.setInputType(InputType.TYPE_CLASS_NUMBER);
                newWeight_LBL_fatTitle.setInputType(InputType.TYPE_CLASS_NUMBER);
                newWeight_LBL_muscleTitle.setInputType(InputType.TYPE_CLASS_NUMBER);
                count_weight = Float.parseFloat(newWeight_TIL_weight.getText().toString());
                count_pFat = Float.parseFloat(newWeight_LBL_fatTitle.getText().toString());
                count_pMuscle = Float.parseFloat(newWeight_LBL_muscleTitle.getText().toString());

                tempWeight = new Weight(count_pFat,count_pMuscle,count_weight,date);
                dataManager.setLastWeight(tempWeight);
                currentUser.addToWeightsUid(tempWeight.getWeightId());
                dataManager.addWeightToUser();
                dataManager.addNewWeight(tempWeight);
                storeWeightInDB(tempWeight);
                Toast.makeText(getApplicationContext(),"השקילה השבועית החדשה שלך נשמרה בהצלחה", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Activity_addWeight.this, MainActivity.class));
            }
        });
    }

    private void storeWeightInDB(Weight tempWeight) {
        //Add the item to the current grocery list
        db.collection(KEYS.KEY_USERS)
                .document(currentUser.getUserId())
                .collection(KEYS.KEY_MY_WEIGHTS)
                .document(tempWeight.getWeightId())
                .set(tempWeight)
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
        newWeight_TIL_calender = findViewById(R.id.newWeight_TIL_calender);
        newWeight_BTN_Save = findViewById(R.id.newWeight_BTN_Save);

        newWeight_LBL_muscleTitle= findViewById(R.id.newWeight_LBL_muscleTitle);
        newWeight_LBL_fatTitle= findViewById(R.id.newWeight_LBL_fatTitle);
        newWeight_TIL_weight = findViewById(R.id.newWeight_TIL_weight);


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
        newWeight_TIL_calender.setText(Sdate);
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
                        startActivity(new Intent(Activity_addWeight.this, MainActivity.class));
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





