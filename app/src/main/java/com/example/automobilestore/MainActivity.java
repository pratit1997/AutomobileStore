package com.example.automobilestore;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.automobilestore.ui.MyAccountFragment;
import com.example.automobilestore.ui.dashboard.DashboardFragment;
import com.example.automobilestore.ui.home.HomeFragment;
import com.example.automobilestore.ui.notifications.NotificationsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    final Fragment Dashboard = new DashboardFragment();
    final Fragment home = new HomeFragment();
    final Fragment account = new MyAccountFragment();
    final Fragment profile = new NotificationsFragment();
    BottomNavigationView bottomNavigationView;
    Fragment active = home;
    private FirebaseUser curUser;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.nav_view);
        auth = FirebaseAuth.getInstance();

        final FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.frame, profile, "3").hide(profile).commit();
        fm.beginTransaction().add(R.id.frame, Dashboard, "2").hide(Dashboard).commit();
        fm.beginTransaction().add(R.id.frame, account, "4").hide(account).commit();
        fm.beginTransaction().add(R.id.frame, home, "1").commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                if (item.getItemId() == R.id.navigation_dashboard) {
                    curUser = auth.getCurrentUser();
                    if (curUser != null) {
                        fm.beginTransaction().hide(active).show(Dashboard).commit();
                        active = Dashboard;
                    } else {
                        fm.beginTransaction().hide(active).show(profile).commit();
                        active = profile;
                    }
                } else if (item.getItemId() == R.id.navigation_home) {

                    fm.beginTransaction().hide(active).show(home).commit();
                    active = home;
                } else if (item.getItemId() == R.id.navigation_notifications) {
                    curUser = auth.getCurrentUser();
                    if (curUser != null) {
                        fm.beginTransaction().hide(active).show(account).disallowAddToBackStack().commit();
                        active = account;
                    } else {
                        fm.beginTransaction().hide(active).show(profile).disallowAddToBackStack().commit();
                        active = profile;
                    }


                }
                return true;
            }
        });
        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        }


    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
