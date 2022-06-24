package com.example.prm_392_nho_ho_ban.activity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.example.prm_392_nho_ho_ban.R;
import com.example.prm_392_nho_ho_ban.adapter.VPAdapter;
import com.example.prm_392_nho_ho_ban.bean.Note;
import com.example.prm_392_nho_ho_ban.bean.User;
import com.example.prm_392_nho_ho_ban.dao.NoteDAO;
import com.example.prm_392_nho_ho_ban.schedulingservice.AlarmReceiver;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class WelcomeActivity extends OptionsMenuActivity {

    public static ArrayList<Note> allNoteList = new ArrayList<>();
    @SuppressLint("SimpleDateFormat")
    private NavigationView nvDrawer;
    private BottomNavigationView nvBottom;
    private Toolbar toolbar;
    private TextView tvEmailDisplay;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    private void bindingUI() throws ParseException {
        nvDrawer = findViewById(R.id.nvView);
        nvBottom = findViewById(R.id.nvBottom);
        toolbar = findViewById(R.id.toolbar);
        viewPager2 = findViewById(R.id.viewPagerWelcome);
        tabLayout = findViewById(R.id.tabLayoutWelcome);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_table_rows_24);
        setupDrawerContent(nvDrawer);
        setupBottomNavContent(nvBottom);
        tvEmailDisplay = nvDrawer.getHeaderView(0).findViewById(R.id.tvEmailDisplay);
        tvEmailDisplay.setText(User.USER.getEmail());
        reSet();
        VPAdapter vpAdapter = new VPAdapter(this);
        viewPager2.setAdapter(vpAdapter);
            new TabLayoutMediator(tabLayout,viewPager2, (tab, position) ->{
switch (position){
    case 0:
        tab.setText("TODAY");
        break;
    case 1:
        tab.setText("ALL NOTE");
        break;
    case 2:
        tab.setText("UPCOMING");
        break;
    }
        }).attach();

    }




    public void reSet() {
        NoteDAO n = new NoteDAO();
        n.getAllUpcomingNoteCallBack(new NoteDAO.FirebaseCallBack()  {
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
        },User.USER);
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

