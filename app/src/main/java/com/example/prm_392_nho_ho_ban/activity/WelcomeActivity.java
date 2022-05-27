package com.example.prm_392_nho_ho_ban.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.prm_392_nho_ho_ban.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private TextView tvEmail;
    private ImageView imgAvatar;
    private MenuItem iLogout;
    private DrawerLayout mdrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;

    private void bindingUI(){
        tvEmail = findViewById(R.id.tvEmail);
        toolbar = findViewById(R.id.toolbar);
        iLogout = findViewById(R.id.itemLogout);
        imgAvatar = findViewById(R.id.imgAvatar);
        mdrawer = findViewById(R.id.layoutDrawer);
        nvDrawer = findViewById(R.id.nvView);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_table_rows_24);

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
    private void bindingAction(){
    iLogout.setOnMenuItemClickListener(this::logout);
    }

    private boolean logout(MenuItem menuItem) {
        firebaseAuth.signOut();
        startActivity(new Intent(this,LoginActivity.class));
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);
        bindingUI();
        bindingAction();
    }
}