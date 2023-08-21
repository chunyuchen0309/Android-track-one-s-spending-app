package com.example.money;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
            FragmentManager fragmentManager;
            FragmentTransaction fragmentTransaction,fragmentTransactionStart;
    private HomeFragment homeFragment,homeFragmentStart;
    private UserFragment userFragment;
    private ScarchFragement scarchFragement;
    private DepositFragment depositFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        fragmentTransactionStart=fragmentManager.beginTransaction();
        homeFragmentStart=new HomeFragment();
        fragmentTransactionStart.replace(R.id.main_frameLayout,homeFragmentStart,"homeFragment").commit();
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                fragmentTransaction=fragmentManager.beginTransaction();
                switch (item.getItemId()){
                    case R.id.home_fg:
                        homeFragment=new HomeFragment();
                        fragmentTransaction.replace(R.id.main_frameLayout,homeFragment,"homeFragment").commit();
                        return true;
                    case R.id.serach:
                        scarchFragement=new ScarchFragement();
                        fragmentTransaction.replace(R.id.main_frameLayout,scarchFragement,"scarchFragement").commit();
                        return true;
                    case R.id.deposit_fg:
                        depositFragment=new DepositFragment();
                        fragmentTransaction.replace(R.id.main_frameLayout,depositFragment,"depositFragement").commit();
                        return true;
                    case R.id.user_fg:
                        userFragment=new UserFragment();
                        fragmentTransaction.replace(R.id.main_frameLayout,userFragment,"userFragment").commit();
                        return true;
                }
                return false;
            }
        });
    }
}