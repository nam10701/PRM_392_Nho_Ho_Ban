package com.example.prm_392_nho_ho_ban.activity;

import static com.example.prm_392_nho_ho_ban.MyApplication.dbRoom;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.example.prm_392_nho_ho_ban.R;
import com.example.prm_392_nho_ho_ban.adapter.VPAdapter;
import com.example.prm_392_nho_ho_ban.bean.Note;
import com.example.prm_392_nho_ho_ban.bean.User;
import com.example.prm_392_nho_ho_ban.dao.NoteDAO;
import com.example.prm_392_nho_ho_ban.dao.RoomNoteDAO;
import com.example.prm_392_nho_ho_ban.fragment.FragmentAllNote;
import com.example.prm_392_nho_ho_ban.fragment.FragmentTodayNote;
import com.example.prm_392_nho_ho_ban.fragment.FragmentUpcomingNote;
import com.example.prm_392_nho_ho_ban.fragment.NulldateFragment;
import com.example.prm_392_nho_ho_ban.schedulingservice.AlarmReceiver;
import com.example.prm_392_nho_ho_ban.schedulingservice.InternetStateReceiver;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.Timestamp;
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
    private static VPAdapter vpAdapter;
    private DrawerLayout mdrawer;
    private Menu noteMenu;
    private RoomNoteDAO roomNoteDAO = dbRoom.createNoteDAO();
    @RequiresApi(api = Build.VERSION_CODES.O)
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
        vpAdapter = new VPAdapter(this);
        viewPager2.setAdapter(vpAdapter);
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("NOTES");
                    break;
                case 1:
                    tab.setText("Today");
                    break;
                case 2:
                    tab.setText("ALL NOTE");
                    break;
                case 3:
                    tab.setText("COMING");
                    break;
            }
        }).attach();
    }

    public static void updateFragment() {
        NulldateFragment z = (NulldateFragment) vpAdapter.getItemByPosition(0);
        FragmentTodayNote a = (FragmentTodayNote) vpAdapter.getItemByPosition(1);
        FragmentAllNote b = (FragmentAllNote) vpAdapter.getItemByPosition(2);
        FragmentUpcomingNote c = (FragmentUpcomingNote) vpAdapter.getItemByPosition(3);
        if (z != null) {
            z.updateAdapter();
        }
        if (a != null) {
            a.updateAdapter();
        }
        if (b != null) {
            b.updateAdapter();
        }
        if (c != null) {
            c.updateAdapter();
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void reSet() {
        NoteDAO n = new NoteDAO();
       ArrayList<Note> upcomingNote = (ArrayList<Note>) roomNoteDAO.getAllUpcomingNote(new Timestamp(new Date()),true,User.USER.getUid(),true);
       ArrayList<Note> upcomingNoteUnpin = (ArrayList<Note>) roomNoteDAO.getAllUpcomingNote(new Timestamp(new Date()),false,User.USER.getUid(),true);
        allNoteList.addAll(upcomingNote);
        allNoteList.addAll(upcomingNoteUnpin);
    }


    private void bindingAction() {
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authorize();
        createBroadcast();
        setContentView(R.layout.welcome_activity);
        try {
            bindingUI();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        bindingAction();
    }

    private void authorize() {
        if (User.USER == null) {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.search_menu, menu);
//        noteMenu = menu;
//
//        return super.onCreateOptionsMenu(menu);
//    }

//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_search:
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();
        authorize();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void createBroadcast(){
        InternetStateReceiver internetStateReceiver = new InternetStateReceiver();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
            registerReceiver(internetStateReceiver, new IntentFilter(BLUETOOTH_SERVICE));
        }
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            registerReceiver(internetStateReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }


}

