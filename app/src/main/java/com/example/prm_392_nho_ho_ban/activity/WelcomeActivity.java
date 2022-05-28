package com.example.prm_392_nho_ho_ban.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.prm_392_nho_ho_ban.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {

    private TextView tvEmailDisplay;
    private ImageView imgAvatar;
    private DrawerLayout mdrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;

    private void bindingUI(){
        nvDrawer = findViewById(R.id.nvView);

        toolbar = findViewById(R.id.toolbar);
        imgAvatar = findViewById(R.id.imgAvatar);
        mdrawer = findViewById(R.id.layoutDrawer);
        tvEmailDisplay = nvDrawer.getHeaderView(0).findViewById(R.id.tvEmailDisplay);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_table_rows_24);
        setupDrawerContent(nvDrawer);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
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

    private void bindingAction(){

    }

    private boolean logout() {
        startActivity(new Intent(this, LogoutActivity.class));
        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);
        bindingUI();
        bindingAction();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            tvEmailDisplay.setText(user.getEmail());}
    }
}