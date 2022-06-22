package com.example.prm_392_nho_ho_ban.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm_392_nho_ho_ban.R;
import com.example.prm_392_nho_ho_ban.adapter.NoteListAdapter;
import com.example.prm_392_nho_ho_ban.bean.Note;
import com.example.prm_392_nho_ho_ban.bean.User;
import com.example.prm_392_nho_ho_ban.dao.NoteDAO;
import com.example.prm_392_nho_ho_ban.schedulingservice.AlarmReceiver;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WelcomeActivity extends OptionsMenuActivity {

    private static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public static ArrayList<Note> allNoteList = new ArrayList<>();
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
    private DrawerLayout mdrawer;
    private NavigationView nvDrawer;
    private BottomNavigationView nvBottom;
    private Toolbar toolbar;
    private TextView tvEmailDisplay;
    private Date today;
    private RecyclerView noteRecyclerView;
    private RecyclerView pinRecyclerView;
    private NoteListAdapter noteListAdapter;
    private NoteListAdapter pinListAdapter;
    private NoteDAO n = new NoteDAO();

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
        pinRecyclerView = findViewById(R.id.PinListRecyclerView);
        tvEmailDisplay.setText(User.USER.getEmail());
        today = sdf.parse(sdf.format(new Date()));
        showNoteByDay(today,today);
        showPinByDay(today,today);
    }



    public void showNoteByDay(Date startDate, Date endDate) {
        NoteDAO n = new NoteDAO();
        n.getAllNoteByDayCallBack(new NoteDAO.FirebaseCallBack()  {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onCallBack(ArrayList<Note> noteList) {
                allNoteList.addAll(noteList);
                reSetTimerNotify();
            }

            @Override
            public void onCallBack() {
            }

            @Override
            public void onCallBack(ArrayList<Note> noteList, ArrayList<Note> noteUnpinList) {

            }
        },startDate, endDate,User.USER);
    }

    public void showPinByDay(Date startDate, Date endDate) {
        NoteDAO n = new NoteDAO();
        n.getAllPinByDayCallBack(new NoteDAO.FirebaseCallBack()  {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onCallBack(ArrayList<Note> noteList) {
            }

            @Override
            public void onCallBack() {
            }

            @Override
            public void onCallBack(ArrayList<Note> noteList, ArrayList<Note> noteUnpinList) {
                LinearLayoutManager verticalLayoutManager
                        = new LinearLayoutManager(WelcomeActivity.this, LinearLayoutManager.VERTICAL, false);
                pinRecyclerView.setLayoutManager(verticalLayoutManager);
                pinListAdapter = new NoteListAdapter(WelcomeActivity.this,noteList);
                pinRecyclerView.setAdapter(pinListAdapter);

                LinearLayoutManager verticalLayoutManagerr
                        = new LinearLayoutManager(WelcomeActivity.this, LinearLayoutManager.VERTICAL, false);
                noteRecyclerView.setLayoutManager(verticalLayoutManagerr);
                noteListAdapter = new NoteListAdapter(WelcomeActivity.this,noteUnpinList);
                noteRecyclerView.setAdapter(noteListAdapter);

            }
        },startDate, endDate,true);
    }


    private void bindingAction(){
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authorize();

        setContentView(R.layout.welcome_activity);
        try {
            bindingUI();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        bindingAction();

    }

    private void authorize() {
        if(User.USER==null){
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();
        showNoteByDay(today,today);
        showPinByDay(today, today);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)

    private void reSetTimerNotify(){
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);

        if(!allNoteList.isEmpty()){
            for(Note note: allNoteList){
                if(note.getDateRemind()!=null){
                    String noteJson = new Gson().toJson(note);
                    intent.putExtra("noteJson",noteJson);
                    @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent =
                            PendingIntent.getBroadcast(getApplicationContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);// fix this
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        alarmManager
                                .setExact(AlarmManager.RTC_WAKEUP, new Date().getTime()+10000, pendingIntent);
                    } else {
                        alarmManager
                                .set(AlarmManager.RTC_WAKEUP, new Date().getTime()+10000, pendingIntent);
                    }
                }
            }
        }

    }

}

