package com.example.prm_392_nho_ho_ban.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm_392_nho_ho_ban.R;
import com.example.prm_392_nho_ho_ban.adapter.NoteListAdapter;
import com.example.prm_392_nho_ho_ban.bean.Note;
import com.example.prm_392_nho_ho_ban.dao.NoteDAO;
import com.example.prm_392_nho_ho_ban.schedulingservice.NotificationScheduling;
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
import java.util.Calendar;
import java.util.Date;

public class WelcomeActivity extends OptionsMenuActivity {

    private static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public final static FirebaseUser USER = firebaseAuth.getCurrentUser();
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
    private NoteListAdapter noteListAdapter;
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
        tvEmailDisplay.setText(USER.getEmail());
        today = sdf.parse(sdf.format(new Date()));
        showNoteByDay(today,today);

        TextView btn = findViewById(R.id.textView4);
        btn.setOnClickListener(this::addNote);

    }

    private void addNote(View view) {
        Note note = new Note("1","  test add ne","test add ne",new Timestamp(new Date()),true,new Timestamp(new Date()),USER.getUid());
        addNoteCallBack(note);
    }

    private void addNoteCallBack(Note note) {
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
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onCallBack(ArrayList<Note> noteList) {
                allNoteList.addAll(noteList);
                LinearLayoutManager verticalLayoutManager
                        = new LinearLayoutManager(WelcomeActivity.this, LinearLayoutManager.VERTICAL, false);
                noteRecyclerView.setLayoutManager(verticalLayoutManager);
                noteListAdapter = new NoteListAdapter(WelcomeActivity.this,noteList);
                noteRecyclerView.setAdapter(noteListAdapter);
                reSetTimerNotify();
            }

            @Override
            public void onCallBack() {
            }
        },startDate, endDate);
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
        if(USER==null){
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();
        showNoteByDay(today,today);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void reSetTimerNotify(){
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent;
        if(!allNoteList.isEmpty()){
            for(Note note: allNoteList){
                if(note.getDateRemind()!=null){
                    long destinationTime = note.getDateRemind().getSeconds()*1000;
                    intent = new Intent(this, NotificationScheduling.class);
                    //xem lai id cua notify
                    String noteJson = new Gson().toJson(note);
                    intent.putExtra("noteJson",noteJson);
                    intent.putExtra("action",NotificationScheduling.ACTION_BUILD_NOTIFY);
                    @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent =
                            PendingIntent.getService(this, allNoteList.indexOf(note)+1, intent, PendingIntent.FLAG_ONE_SHOT);
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

