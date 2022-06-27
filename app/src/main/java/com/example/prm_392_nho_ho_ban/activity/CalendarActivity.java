package com.example.prm_392_nho_ho_ban.activity;

import static com.example.prm_392_nho_ho_ban.MyApplication.dbRoom;

import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm_392_nho_ho_ban.R;
import com.example.prm_392_nho_ho_ban.adapter.NoteListAdapter;
import com.example.prm_392_nho_ho_ban.bean.Note;
import com.example.prm_392_nho_ho_ban.bean.User;
import com.example.prm_392_nho_ho_ban.dao.NoteDAO;
import com.example.prm_392_nho_ho_ban.dao.RoomNoteDAO;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CalendarActivity extends OptionsMenuActivity {
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    private MaterialCalendarView calendar;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private TextView tvEmailDisplay;
    private RecyclerView rvNote;
    private NoteListAdapter noteListAdapter;
    private NoteDAO noteDAO = new NoteDAO();
    private ArrayList<Note> monthNoteList;
    CalendarDay prevDay = null;
    RoomNoteDAO roomNoteDAO = dbRoom.createNoteDAO();
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
        ArrayList<Note> noteLit = (ArrayList<Note>) roomNoteDAO.getAllNoteByDay(new Timestamp(firstDay), new Timestamp(lastDay), User.USER.getUid());
        noteListAdapter.update(noteLit);
        getAllEvent(noteLit);
        monthNoteList = noteLit;

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
                Date firstDay = null;
                Date lastDay = null;
                try {
                    firstDay = sdf.parse(day);
                    lastDay = new Date(firstDay.getTime() + 86400000);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (selected) {
                    ArrayList<Note> noteLit = (ArrayList<Note>) roomNoteDAO.getAllNoteByDay(new Timestamp(firstDay), new Timestamp(lastDay), User.USER.getUid());
                    noteListAdapter.update(noteLit);
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
        ArrayList<Note> noteLit = (ArrayList<Note>) roomNoteDAO.getAllNoteByDay(new Timestamp(startDate), new Timestamp(endDate), User.USER.getUid());
        LinearLayoutManager verticalLayoutManager
                = new LinearLayoutManager(CalendarActivity.this, LinearLayoutManager.VERTICAL, false);
        rvNote.setLayoutManager(verticalLayoutManager);
        noteListAdapter = new NoteListAdapter(CalendarActivity.this, noteLit);

        rvNote.setAdapter(noteListAdapter);
        getAllEvent(noteLit);
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
        Date firstDay;
        Date lastDay;
        Calendar fc = Calendar.getInstance();
        fc.set(Calendar.DAY_OF_MONTH, 1);
        firstDay = fc.getTime();
        Calendar lc = Calendar.getInstance();
        lc.add(Calendar.MONTH, 1);
        lc.set(Calendar.DAY_OF_MONTH, 1);
        lc.add(Calendar.DATE, -1);
        lastDay = lc.getTime();
        showNoteByDay(firstDay, lastDay);
        monthNoteList = (ArrayList<Note>) roomNoteDAO.getAllNoteByDay(new Timestamp(firstDay), new Timestamp(lastDay), User.USER.getUid());
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