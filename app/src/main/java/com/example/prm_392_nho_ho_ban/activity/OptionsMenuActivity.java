package com.example.prm_392_nho_ho_ban.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.prm_392_nho_ho_ban.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class OptionsMenuActivity extends AppCompatActivity {
    private DrawerLayout mdrawer;
    private NavigationView nvDrawer;
    private Toolbar toolbar;
    private TextView tvEmailDisplay;
    @Override

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_drawer,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mdrawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setupDrawerContent(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(this::selectDrawerItem);
    }

    @SuppressLint("NonConstantResourceId")
    public boolean selectDrawerItem(MenuItem menuItem){
        switch (menuItem.getItemId()){
            case R.id.itemLogout:
                logout();
                return true;
            case R.id.itemAccount:
                return true;
        }
        return true;
    }

    private void logout() {
        startActivity(new Intent(this, LogoutActivity.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        nvDrawer = findViewById(R.id.nvView);
        toolbar = findViewById(R.id.toolbar);
        mdrawer = findViewById(R.id.layoutDrawer);
        tvEmailDisplay = nvDrawer.getHeaderView(0).findViewById(R.id.tvEmailDisplay);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_table_rows_24);
        setupDrawerContent(nvDrawer);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            tvEmailDisplay.setText(user.getEmail());}
    }
}