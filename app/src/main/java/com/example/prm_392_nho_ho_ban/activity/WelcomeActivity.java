package com.example.prm_392_nho_ho_ban.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm_392_nho_ho_ban.R;
import com.example.prm_392_nho_ho_ban.adapter.NoteListAdapter;
import com.example.prm_392_nho_ho_ban.bean.Note;
import com.example.prm_392_nho_ho_ban.dao.NoteDAO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class WelcomeActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<Note> notes = new ArrayList<>();
    private TextView tvEmailDisplay;
    private ImageView imgAvatar;
    private DrawerLayout mdrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private RecyclerView noteRecyclerView;
    private NoteListAdapter noteListAdapter;

    private void bindingUI(){
        nvDrawer = findViewById(R.id.nvView);
        toolbar = findViewById(R.id.toolbar);
        imgAvatar = findViewById(R.id.imgAvatar);
        mdrawer = findViewById(R.id.layoutDrawer);
        tvEmailDisplay = nvDrawer.getHeaderView(0).findViewById(R.id.tvEmailDisplay);
        noteRecyclerView = findViewById(R.id.noteListRecyclerView);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_table_rows_24);
        setupDrawerContent(nvDrawer);
        showAllNote();


    }

    public void showAllNote(){
        NoteDAO n = new NoteDAO();
        n.getAllNoteCallBack(new NoteDAO.FirebaseCallBack() {
            @Override
            public void onCallBack(ArrayList<Note> noteList) {
                LinearLayoutManager verticalLayoutManager
                        = new LinearLayoutManager(WelcomeActivity.this, LinearLayoutManager.VERTICAL, false);
                noteRecyclerView.setLayoutManager(verticalLayoutManager);
                noteListAdapter = new NoteListAdapter(WelcomeActivity.this,noteList);
                noteRecyclerView.setAdapter(noteListAdapter);
            }
        });
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

    private void bindingAction(){
    }

    private void logout() {
        startActivity(new Intent(this, LogoutActivity.class));
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
//        ArrayList<Note> noteList = db.collection("Note")

    }
}