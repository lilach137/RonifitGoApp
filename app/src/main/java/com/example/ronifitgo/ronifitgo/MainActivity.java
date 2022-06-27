package com.example.ronifitgo.ronifitgo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.example.ronifitgo.R;
//import com.example.ronifitgo.ronifitgo.Fragments.fragment_homePage;
import com.example.ronifitgo.ronifitgo.Fragments.fragment_homePage;
import com.example.ronifitgo.ronifitgo.Fragments.fragment_measures;
import com.example.ronifitgo.ronifitgo.Fragments.fragment_profile;
import com.example.ronifitgo.ronifitgo.Fragments.fragment_tips;
import com.example.ronifitgo.ronifitgo.Fragments.fragment_weights;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private static final int SIZE = 5;
    private LinearLayout main_Layout_addButtons;
    private ExtendedFloatingActionButton main_BTN_AddWeight;
    private ExtendedFloatingActionButton main_BTN_AddMeasure;
    private FloatingActionButton main_BTN_Add;
    private boolean isFBTOpen = false;
    private BottomNavigationView main_BottomNavigationView;


    public static final int TIPS = 0, MEASURES = 1, WEIGHTS = 2, PROFILE = 3 , MAIN = 4;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Fragment[] panel_fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //replaceFragments(panel_fragments[MAIN]);
        setFragments();
        findViews();
        initButton();
        closeFBT();
        
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

        main_BTN_AddMeasure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Activity_addMeasures.class));
                finish();
            }
        });


        main_BTN_AddWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Activity_addWeight.class));
                finish();
            }
        });

        main_BottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.tips:
                        //panel_TextTitle.setText(getString(R.string.closet));
                        replaceFragments(panel_fragments[TIPS]);
                        break;
                    case R.id.measures:
                        //panel_TextTitle.setText(getString(R.string.favorites));
                        replaceFragments(panel_fragments[MEASURES]);
                        break;
                    case R.id.data:
                        //panel_TextTitle.setText(getString(R.string.Outfits));
                        replaceFragments(panel_fragments[WEIGHTS]);
                        break;
                    case R.id.profile:
                        //panel_TextTitle.setText(getString(R.string.profile));
                        replaceFragments(panel_fragments[MAIN]);
                        break;
                }
                return true;
            }
        });
    }

    private void replaceFragments(Fragment fragment) {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_Fragment, fragment, null);
        fragmentTransaction.commit();
    }

    private void setFragments() {
        panel_fragments = new Fragment[SIZE];
//        panel_fragments[TIPS] = new fragment_tips().setActivity(this);
        panel_fragments[MEASURES] = new fragment_measures().setActivity(this);
        panel_fragments[WEIGHTS] = new fragment_weights().setActivity(this);
//        panel_fragments[PROFILE] = new fragment_profile().setActivity(this);
        panel_fragments[MAIN] = new fragment_homePage().setActivity(this);
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

    private void findViews() {
        main_BTN_AddWeight=findViewById(R.id.main_BTN_AddWeight);
        main_BTN_AddMeasure=findViewById(R.id.main_BTN_AddMeasure);
        main_BTN_Add = findViewById(R.id.main_BTN_Add);
        main_Layout_addButtons = findViewById(R.id.main_Layout_addButtons);
        main_BottomNavigationView = findViewById(R.id.main_BottomNavigationView);
    }

    @Override
    public void onBackPressed() {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("warning")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }

}
