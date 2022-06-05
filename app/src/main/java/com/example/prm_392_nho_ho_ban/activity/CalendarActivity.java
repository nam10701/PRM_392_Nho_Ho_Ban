package com.example.prm_392_nho_ho_ban.activity;

import static com.prolificinteractive.materialcalendarview.MaterialCalendarView.SELECTION_MODE_MULTIPLE;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.prm_392_nho_ho_ban.R;
import com.example.prm_392_nho_ho_ban.adapter.MNoteListAdapter;
import com.example.prm_392_nho_ho_ban.adapter.NoteListAdapter;
import com.example.prm_392_nho_ho_ban.bean.Note;
import com.example.prm_392_nho_ho_ban.dao.NoteDAO;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CalendarActivity extends OptionsMenuActivity {
    private MaterialCalendarView calendar;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private TextView tvEmailDisplay;
    private RecyclerView rvNote;
    private NoteListAdapter noteListAdapter;


    private void bindingView() throws ParseException {
        calendar = findViewById(R.id.calendarView);
        toolbar = findViewById(R.id.toolbar);
        nvDrawer = findViewById(R.id.nvView);
        tvEmailDisplay = nvDrawer.getHeaderView(0).findViewById(R.id.tvEmailDisplay);
        rvNote = findViewById(R.id.rvNoteM);



    }
    private void getNoteByMonth(int month, int year){
        Date firstday; Date lastday;
        NoteDAO noteDAO = new NoteDAO();
        Calendar months = Calendar.getInstance(); months.set(year,month);
        Calendar fc = Calendar.getInstance();
        fc.set(Calendar.DAY_OF_MONTH, 1); firstday = fc.getTime();
        Calendar lc = Calendar.getInstance();
        lc.add(Calendar.MONTH,1);
        lc.set(Calendar.DAY_OF_MONTH, 1); lc.add(Calendar.DATE,-1);
        lastday = lc.getTime();
        noteDAO.getAllNoteByDayCallBack(new NoteDAO.FirebaseCallBack() {
            @Override
            public void onCallBack(ArrayList<Note> noteList) {
                noteListAdapter.update(noteList);
                Log.i("hello",noteList.size()+"");
            }

            @Override
            public void onCallBack() {

            }
        },firstday,lastday);
    }
    private void bindingAction() {
        calendar.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

                getNoteByMonth(date.getMonth(),date.getYear());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Date firstday;
        Date lastday;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);// get the reference of CalendarView
        try {
            bindingView();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        calendarAction();
        sideNav();
        Calendar fc = Calendar.getInstance();
        fc.set(Calendar.DAY_OF_MONTH, 1); firstday = fc.getTime();
        Calendar lc = Calendar.getInstance();
        lc.add(Calendar.MONTH,1);
        lc.set(Calendar.DAY_OF_MONTH, 1); lc.add(Calendar.DATE,-1);
        lastday = lc.getTime();
        showNoteByDay(firstday,lastday);
        bindingAction();
    }

    private void sideNav() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_table_rows_24);
        getSupportActionBar().setTitle("Calendar");
        setupDrawerContent(nvDrawer);

    }

    private void calendarAction() {
        calendar.setSelectionMode(SELECTION_MODE_MULTIPLE);
        calendar.state().edit()
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showNoteByDay(Date startDate, Date endDate) {

        NoteDAO n = new NoteDAO();
        n.getAllNoteByDayCallBack(new NoteDAO.FirebaseCallBack() {
            @Override
            public void onCallBack(ArrayList<Note> noteList) {
                LinearLayoutManager verticalLayoutManager
                        = new LinearLayoutManager(CalendarActivity.this, LinearLayoutManager.VERTICAL, false);
                rvNote.setLayoutManager(verticalLayoutManager);
                noteListAdapter = new NoteListAdapter(CalendarActivity.this, noteList);
                rvNote.setAdapter(noteListAdapter);
            }

            @Override
            public void onCallBack() {
            }
        }, startDate, endDate);
    }

    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            tvEmailDisplay.setText(user.getEmail());
        }
    }
}