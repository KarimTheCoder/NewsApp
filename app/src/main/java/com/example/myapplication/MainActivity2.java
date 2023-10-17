package com.example.myapplication;

import android.os.Bundle;
import android.view.View;

import com.example.myapplication.data.local.database.NewsDatabase;
import com.example.myapplication.newsAPI.INewsDataSource;
import com.example.myapplication.newsAPI.NewsDataSource;
import com.example.myapplication.ui.viewmodel.SharedViewModel;
import com.example.myapplication.ui.callbacks.BottomBarCallbacks;
import com.example.myapplication.ui.viewmodel.SharedViewModelFactory;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.databinding.ActivityMain2Binding;

public class MainActivity2 extends AppCompatActivity implements BottomBarCallbacks {

    private ActivityMain2Binding binding;
    private ConstraintLayout constraintLayout;
    private View fragment;
    private ConstraintSet constraintSet;
    private BottomNavigationView navView;
    private SharedViewModel sharedViewModel;
    AppBarConfiguration appBarConfiguration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        navView = findViewById(R.id.nav_view);

        constraintLayout = findViewById(R.id.container);
        fragment = findViewById(R.id.nav_host);

        constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);



        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_saved, R.id.navigation_read)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);



        INewsDataSource newsDataSource = new NewsDataSource(getApplication());
        NewsDatabase database = NewsDatabase.getDatabase(this);

        ViewModelProvider.Factory factory = new SharedViewModelFactory(getApplication(),newsDataSource,database);

        sharedViewModel = new ViewModelProvider(this,factory).get(SharedViewModel.class);
        sharedViewModel.setBottomBarCallbacks(this);

        sharedViewModel.setTestText("Nigger");


    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }





    @Override
    public void hideBottomBar() {
        constraintSet.connect(fragment.getId(), ConstraintSet.BOTTOM, constraintLayout.getId(), ConstraintSet.BOTTOM, 0);
        constraintSet.applyTo(constraintLayout); // Apply the constraints to the layout
        navView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showBottomBar() {
        constraintSet.connect(fragment.getId(), ConstraintSet.BOTTOM, navView.getId(), ConstraintSet.TOP, 0);
        constraintSet.applyTo(constraintLayout); // Apply the constraints to the layout
        navView.setVisibility(View.VISIBLE);
    }
}