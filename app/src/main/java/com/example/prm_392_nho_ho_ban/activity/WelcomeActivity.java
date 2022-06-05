package com.example.prm_392_nho_ho_ban.activity;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm_392_nho_ho_ban.R;
import com.example.prm_392_nho_ho_ban.adapter.NoteListAdapter;
import com.example.prm_392_nho_ho_ban.bean.Note;
import com.example.prm_392_nho_ho_ban.dao.NoteDAO;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.auth.User;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WelcomeActivity extends OptionsMenuActivity {

    private static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public final static FirebaseUser USER = firebaseAuth.getCurrentUser();

    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
    private DrawerLayout mdrawer;
    private NavigationView nvDrawer;
    private BottomNavigationView nvBottom;
    private Toolbar toolbar;
    private TextView tvEmailDisplay;
    private Date today;
    private RecyclerView noteRecyclerView;
    private NoteListAdapter noteListAdapter;

    private void bindingUI() throws ParseException {
        nvDrawer = findViewById(R.id.nvView);
        nvBottom = findViewById(R.id.nvBottom);
        toolbar = findViewById(R.id.toolbar);
        mdrawer = findViewById(R.id.layoutDrawer);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_table_rows_24);
        setupDrawerContent(nvDrawer);
        setupBottomNavContent(nvBottom);
        tvEmailDisplay = nvDrawer.getHeaderView(0).findViewById(R.id.tvEmailDisplay);

        noteRecyclerView = findViewById(R.id.noteListRecyclerView);
        today = sdf.parse(sdf.format(new Date()));
        showNoteByDay(today,today);

        TextView btn = findViewById(R.id.textView4);
        btn.setOnClickListener(this::addNote);
    }

    private void addNote(View view) {
        Note note = new Note("1","test add ne","test add ne",new Timestamp(new Date()),true,new Timestamp(new Date()),USER.getUid());
        addNoteCallBack(note);
    }

    private void addNoteCallBack(Note note) {
        NoteDAO n = new NoteDAO();
        n.addNote(new NoteDAO.FirebaseCallBack() {

            @Override
            public void onCallBack(ArrayList<Note> noteList) {
            }
            @Override
            public void onCallBack() {
                Toast.makeText(getApplicationContext(), "Add thành công", Toast.LENGTH_SHORT).show();
                n.getAllNoteByDayCallBack(new NoteDAO.FirebaseCallBack() {
                    @Override
                    public void onCallBack(ArrayList<Note> noteList) {
                        noteListAdapter.update(noteList);
                    }
                    @Override
                    public void onCallBack() {
                    }
                },today,today);
            }
        },note);
    }

    public void showNoteByDay(Date startDate, Date endDate) {
        NoteDAO n = new NoteDAO();
        n.getAllNoteByDayCallBack(new NoteDAO.FirebaseCallBack()  {
            @Override
            public void onCallBack(ArrayList<Note> noteList) {
                LinearLayoutManager verticalLayoutManager
                        = new LinearLayoutManager(WelcomeActivity.this, LinearLayoutManager.VERTICAL, false);
                noteRecyclerView.setLayoutManager(verticalLayoutManager);
                noteListAdapter = new NoteListAdapter(WelcomeActivity.this,noteList);
                noteRecyclerView.setAdapter(noteListAdapter);
            }

            @Override
            public void onCallBack() {
            }
        },startDate, endDate);
    }


    private void bindingAction(){
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);
        try {
            bindingUI();
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
