package com.example.ronifitgo.ronifitgo.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ronifitgo.R;
import com.example.ronifitgo.ronifitgo.Fragments.fragment_allTrainers;
import com.example.ronifitgo.ronifitgo.Fragments.fragment_tips;
import com.example.ronifitgo.ronifitgo.utils.DataManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class Activity_main_coach extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private FloatingActionButton main_BTN_Add;
    private Fragment[] panel_fragments;
    private PopupWindow popupWindow;
    private TextInputEditText dialog_EDT_tip;
    private MaterialButton dialog_BTN_save;
    private LinearLayout main_Layout_addButtons;
    private ExtendedFloatingActionButton main_BTN_AddTip;
    private boolean isFBTOpen = false;
    private BottomNavigationView main_BottomNavigationView;
    public static final int TIPS = 0, TRAINERS = 1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acticity_main_coach);

        setFragments();
        replaceFragments(panel_fragments[1]);
        findview();
        closeFBT();
        initButton();
    }

    private void initButton() {
        main_BTN_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFBTOpen) {
                    showFBT();
                } else {
                    closeFBT();
                }
            }
        });

        main_BTN_AddTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonShowPopupWindowClick(v);
                closeFBT();
            }
        });

        main_BottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.tips:
                        replaceFragments(panel_fragments[TIPS]);
                        break;
                    case R.id.trainers:
                        replaceFragments(panel_fragments[TRAINERS]);
                        break;
                }
                    return true;
                }

        });


    }

    private void findview() {
        main_BTN_Add = findViewById(R.id.main_BTN_Add);
        main_Layout_addButtons = findViewById(R.id.main_Layout_addButtons);
        main_BTN_AddTip = findViewById(R.id.main_BTN_AddTip);
        main_BottomNavigationView = findViewById(R.id.main_BottomNavigationView);
    }
    private void setFragments() {
        panel_fragments = new Fragment[2];
        panel_fragments[0] = new fragment_tips().setActivity(this);
        panel_fragments[1] = new fragment_allTrainers().setActivity(this);


    }

    private void replaceFragments(Fragment fragment) {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainCoach_Fragment, fragment, null);
        fragmentTransaction.commit();
    }

    public void onBackPressed() {
        AlertDialog alertDialog = new AlertDialog.Builder(Activity_main_coach.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("זהירות!")
                .setMessage("אתה בטוח שאתה רוצה לצאת?")
                .setPositiveButton("כן", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        startActivity(new Intent(Activity_main_coach.this, Activity_goodbye.class));
                    }
                })
                .setNegativeButton("לא", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }


    public void onButtonShowPopupWindowClick(View view) {

        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.dialog_addtip, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        popupWindow = new PopupWindow(popupView, width, height, focusable);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        dialog_BTN_save =(MaterialButton) popupView.findViewById(R.id.dialog_BTN_save);
        dialog_EDT_tip = (TextInputEditText) popupView.findViewById(R.id.dialog_EDT_tip);;


        dialog_BTN_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tip = dialog_EDT_tip.getText().toString();
                saveTipInDb(tip);
                popupWindow.dismiss();

            }
        });


    }

    private void saveTipInDb(String tip) {
        DataManager.getInstance().storeTipInDB(tip);
    }

    private void showFBT() {
        isFBTOpen = true;

        TranslateAnimation animation = new TranslateAnimation(0, 0, main_Layout_addButtons.getHeight() + 1000, 0);
        animation.setDuration(500);
        animation.setFillAfter(true);
        main_Layout_addButtons.startAnimation(animation);
    }

    private void closeFBT() {
        isFBTOpen = false;
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, main_Layout_addButtons.getHeight() + 1000);
        animation.setDuration(500);
        animation.setFillAfter(true);
        main_Layout_addButtons.startAnimation(animation);

    }


}

