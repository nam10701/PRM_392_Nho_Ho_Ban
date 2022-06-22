package com.example.prm_392_nho_ho_ban.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;

import com.example.prm_392_nho_ho_ban.R;
import com.example.prm_392_nho_ho_ban.adapter.NoteListAdapter;
import com.example.prm_392_nho_ho_ban.bean.Note;
import com.example.prm_392_nho_ho_ban.bean.User;
import com.example.prm_392_nho_ho_ban.dao.NoteDAO;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarActivity extends OptionsMenuActivity {
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    private MaterialCalendarView calendar;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private TextView tvEmailDisplay;
    private RecyclerView rvNote;
    private NoteListAdapter noteListAdapter;
    private NoteDAO noteDAO = new NoteDAO();
    private ArrayList<Note> monthNoteList = new ArrayList<>();
    CalendarDay prevDay = null;
    private void bindingView() throws ParseException {
        calendar = findViewById(R.id.calendarView);
        toolbar = findViewById(R.id.toolbar);
        nvDrawer = findViewById(R.id.nvView);
        tvEmailDisplay = nvDrawer.getHeaderView(0).findViewById(R.id.tvEmailDisplay);
        rvNote = findViewById(R.id.rvNoteM);


    }

    private void getNoteByMonth(int month, int year) throws ParseException {
        Date firstDay;
        Date lastDay;
        String date = "1-" + month + "-" + year;
        Calendar fc = Calendar.getInstance();
        fc.setTime(sdf.parse(date));
        fc.set(Calendar.DAY_OF_MONTH, 1);
        firstDay = fc.getTime();
        Calendar lc = Calendar.getInstance();
        lc.setTime(sdf.parse(date));
        lc.add(Calendar.MONTH, 1);
        lc.set(Calendar.DAY_OF_MONTH, 1);
        lc.add(Calendar.DATE, -1);
        lastDay = lc.getTime();
        noteDAO.getAllNoteByDayCallBack(new NoteDAO.FirebaseCallBack() {
            @Override
            public void onCallBack(ArrayList<Note> noteList) {
                noteListAdapter.update(noteList);
                getAllEvent(noteList);
                monthNoteList = noteList;
            }

            @Override
            public void onCallBack() {

            }

            @Override
            public void onCallBack(ArrayList<Note> noteList, ArrayList<Note> noteUnpinList) {

            }
        }, firstDay, lastDay, User.USER);
    }

    private void bindingAction() {

        calendar.setOnMonthChangedListener((widget, date) -> {
            try {
                getNoteByMonth(date.getMonth(), date.getYear());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });

        calendar.setOnDateChangedListener(this::getNoteByDay);

    }

    private void getNoteByDay(MaterialCalendarView materialCalendarView, CalendarDay date, boolean selected) {

                String day = date.getDay() + "-" + date.getMonth() + "-" + date.getYear();
                Date d = null;
                try {
                    d = sdf.parse(day);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (selected) {
                    noteDAO.getAllNoteByDayCallBack(new NoteDAO.FirebaseCallBack() {
                        @Override
                        public void onCallBack(ArrayList<Note> noteList) {
                            noteListAdapter.update(noteList);
                        }

                        @Override
                        public void onCallBack() {

                        }

                        @Override
                        public void onCallBack(ArrayList<Note> noteList, ArrayList<Note> noteUnpinList) {

                        }
                    },d,d,User.USER);
                    if (prevDay != null && prevDay != date) {
                        calendar.setDateSelected(prevDay, false);
                    }
                    prevDay = date;
                } else {
                    noteListAdapter.update(monthNoteList);
                }
    }

    private void sideNav() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_table_rows_24);
        getSupportActionBar().setTitle("Calendar");
        setupDrawerContent(nvDrawer);

    }

    private void calendarAction() {
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
                getAllEvent(noteList);
                monthNoteList = noteList;
            }

            @Override
            public void onCallBack() {
            }

            @Override
            public void onCallBack(ArrayList<Note> noteList, ArrayList<Note> noteUnpinList) {

            }
        }, startDate, endDate,User.USER);
    }

    private void getAllEvent(ArrayList<Note> noteList) {
        for (Note note : noteList) {
            ArrayList<CalendarDay> cd = new ArrayList<>();
            Date date = new Date(note.getDateRemind().getSeconds() * 1000);
            String day = (String) DateFormat.format("dd", date);
            String monthNumber = (String) DateFormat.format("MM", date);
            String year = (String) DateFormat.format("yyyy", date);
            int yearParse = Integer.parseInt(year);
            int monthParse = Integer.parseInt(monthNumber);
            int dayParse = Integer.parseInt(day);
            cd.add(CalendarDay.from(yearParse, monthParse, dayParse));
            EventDecorator eventDecorator = new EventDecorator(cd, getApplicationContext());
            calendar.addDecorator(eventDecorator);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();
        dayOfMonth();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void dayOfMonth() {
        Date firstday;
        Date lastday;
        Calendar fc = Calendar.getInstance();
        fc.set(Calendar.DAY_OF_MONTH, 1);
        firstday = fc.getTime();
        Calendar lc = Calendar.getInstance();
        lc.add(Calendar.MONTH, 1);
        lc.set(Calendar.DAY_OF_MONTH, 1);
        lc.add(Calendar.DATE, -1);
        lastday = lc.getTime();
        showNoteByDay(firstday, lastday);
    }

    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            tvEmailDisplay.setText(user.getEmail());
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);// get the reference of CalendarView
        try {
            bindingView();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendarAction();
        sideNav();
        dayOfMonth();
        bindingAction();
    }
}