package com.example.automobilestore;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.automobilestore.ui.dashboard.DashboardFragment;
import com.example.automobilestore.ui.home.HomeFragment;
import com.example.automobilestore.ui.notifications.NotificationsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.automobilestore.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        BottomNavigationView navView = findViewById(R.id.nav_view);
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(binding.navView, navController);
//    }
//final Fragment wishlist = new WishlistFragment();

    final Fragment Dashboard = new DashboardFragment();
    final Fragment home = new HomeFragment();
    final Fragment account = new NotificationsFragment();
    final Fragment profile = new Fragment();
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
            bottomNavigationView.setSelectedItemId(R.id.navigation_notifications);
        }


    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

}