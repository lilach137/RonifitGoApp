package com.example.ronifitgo.ronifitgo.Activities;



import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.ronifitgo.R;
import com.example.ronifitgo.ronifitgo.Adapters.MyFragmentsAdapter;
import com.example.ronifitgo.ronifitgo.utils.DataManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;

public class Activity_trainer extends AppCompatActivity {

    private TextView user_txt_name;
    private TabLayout trainer_tabLayout;
    private ViewPager2 trainer_viewPager2;
    private MaterialButton trainer_BTN_back;
    private MyFragmentsAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer);
        findView();
        initTabLayout();


        trainer_BTN_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(Activity_trainer.this,Activity_main_coach.class));
            }
        });


    }

    private void initTabLayout() {
        trainer_tabLayout.addTab(trainer_tabLayout.newTab().setText("פרופיל"));
        trainer_tabLayout.addTab(trainer_tabLayout.newTab().setText("משקלים"));
        trainer_tabLayout.addTab(trainer_tabLayout.newTab().setText("היקפים"));

        user_txt_name.setText(DataManager.getInstance().getCurrentUser().getName());

        FragmentManager fragmentManager = getSupportFragmentManager();
        adapter = new MyFragmentsAdapter(fragmentManager , getLifecycle());
        trainer_viewPager2.setAdapter(adapter);

        trainer_tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                trainer_viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        trainer_viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                trainer_tabLayout.selectTab(trainer_tabLayout.getTabAt(position));
            }
        });
    }

    public void onBackPressed() {
        finish();
       startActivity(new Intent(Activity_trainer.this, Activity_main_coach.class));
    }


    public void findView(){
        user_txt_name = findViewById(R.id.user_txt_name);
        trainer_tabLayout = findViewById(R.id. trainer_tabLayout);
        trainer_viewPager2 = findViewById(R.id.trainer_viewPager2);
        trainer_BTN_back = findViewById(R.id.trainer_BTN_back);

    }
}