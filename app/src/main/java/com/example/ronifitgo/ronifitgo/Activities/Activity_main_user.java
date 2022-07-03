package com.example.ronifitgo.ronifitgo.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.ronifitgo.R;
//import com.example.ronifitgo.ronifitgo.Fragments.fragment_homePage;
import com.example.ronifitgo.ronifitgo.Fragments.fragment_homePage;
import com.example.ronifitgo.ronifitgo.Fragments.fragment_measures;
import com.example.ronifitgo.ronifitgo.Fragments.fragment_tips;
import com.example.ronifitgo.ronifitgo.Fragments.fragment_weights;
import com.example.ronifitgo.ronifitgo.utils.DataManager;
import com.example.ronifitgo.ronifitgo.utils.KEYS;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class Activity_main_user extends AppCompatActivity {

    private static final int SIZE =4;
    private LinearLayout main_Layout_addButtons;
    private ExtendedFloatingActionButton main_BTN_AddWeight;
    private ExtendedFloatingActionButton main_BTN_AddMeasure;
    private ExtendedFloatingActionButton main_BTN_picture;
    private FloatingActionButton main_BTN_Add;
    private CircleImageView main_IMG_profile;
    private boolean isFBTOpen = false;
    private BottomNavigationView main_BottomNavigationView;
    private LottieAnimationView loading_animation;
    public static final int TIPS = 0, MEASURES = 1, WEIGHTS = 2,  MAIN = 3;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Fragment[] panel_fragments;
    private final DataManager dataManager = DataManager.getInstance();
    private final FirebaseFirestore db = dataManager.getDbFireStore();
    private final FirebaseDatabase realtimeDB = dataManager.getRealTimeDB();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        closeFBT();

        //upload profile photo
        Uri myUri = Uri.parse(DataManager.getInstance().getCurrentUser().getProfileImgUrl());
        Glide.with(Activity_main_user.this)
                .load(myUri)
                .into(main_IMG_profile);


        setFragments();
        replaceFragments(panel_fragments[MAIN]);

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

        main_BTN_AddMeasure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_main_user.this, Activity_addMeasures.class));
                finish();
            }
        });


        main_BTN_AddWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_main_user.this, Activity_addWeight.class));
                finish();
            }
        });


        main_BottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Handler handler = new Handler();
                switch (item.getItemId()) {
                    case R.id.tips:
                        replaceFragments(panel_fragments[TIPS]);
                        break;
                    case R.id.measures:
                        replaceFragments(panel_fragments[MEASURES]);
                        break;
                    case R.id.data:
                        replaceFragments(panel_fragments[WEIGHTS]);
                        break;
                    case R.id.profile:
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
        panel_fragments[TIPS] = new fragment_tips().setActivity(this);
        panel_fragments[MEASURES] = new fragment_measures().setActivity(this);
        panel_fragments[WEIGHTS] = new fragment_weights().setActivity(this);
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
        main_IMG_profile = findViewById(R.id.main_IMG_profile);
        main_BTN_AddWeight=findViewById(R.id.main_BTN_AddWeight);
        main_BTN_AddMeasure=findViewById(R.id.main_BTN_AddMeasure);
        main_BTN_Add = findViewById(R.id.main_BTN_Add);
        main_Layout_addButtons = findViewById(R.id.main_Layout_addButtons);
        main_BottomNavigationView = findViewById(R.id.main_BottomNavigationView);
        loading_animation = findViewById(R.id.loading_animation);
    }


    public void onBackPressed() {
        AlertDialog alertDialog = new AlertDialog.Builder(Activity_main_user.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("זהירות!")
                .setMessage("אתה בטוח שאתה רוצה לצאת?")
                .setPositiveButton("כן", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(Activity_main_user.this, Activity_goodbye.class));
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





