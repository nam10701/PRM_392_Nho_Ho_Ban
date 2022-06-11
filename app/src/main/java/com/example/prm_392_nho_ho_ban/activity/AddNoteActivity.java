package com.example.prm_392_nho_ho_ban.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.widget.Toolbar;

import com.example.prm_392_nho_ho_ban.R;
import com.example.prm_392_nho_ho_ban.bean.Note;
import com.example.prm_392_nho_ho_ban.dao.NoteDAO;
import com.example.prm_392_nho_ho_ban.schedulingservice.AlarmReceiver;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AddNoteActivity extends AppCompatActivity {
    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
    private EditText edtTitle;
    private EditText edtaNote;
    private Button btnSave;

    private FirebaseAuth firebaseAuth;

    private Toolbar noteToolbar;
    private Menu noteMenu;

    public void bindingView() {
        edtTitle = findViewById(R.id.edtTitle);
        edtaNote = findViewById(R.id.edtaNote);
        btnSave = findViewById(R.id.btnSaveNote);
        firebaseAuth = FirebaseAuth.getInstance();
        noteToolbar = findViewById(R.id.toolbar);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void bindingAction()   {
        btnSave.setOnClickListener(this::onBtnSaveClick);
        setSupportActionBar(noteToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void onBtnSaveClick(View view) {
        String title = edtTitle.getText().toString().trim();
        String content = edtaNote.getText().toString().trim();

        if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(content)) {
            createNote(view ,title, content);
//            Intent n = new Intent(this,WelcomeActivity.class);
//            startActivity(n);
        } else {
            Snackbar.make(view,"Please fill empty fields!", Snackbar.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setTimerNotify(Note note) {
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        String noteJson = new Gson().toJson(note);
        intent.putExtra("noteJson",noteJson);
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent =
                PendingIntent.getBroadcast(getApplicationContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long destinationTime = note.getDateRemind().getSeconds()*1000 + 10000;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager
                    .setExact(AlarmManager.RTC_WAKEUP, destinationTime, pendingIntent);
        } else {
            alarmManager
                    .set(AlarmManager.RTC_WAKEUP, destinationTime, pendingIntent);
        }
    }


    private void createNote(View view, String title, String content) {
        Note note = new Note("",title, content, new Timestamp(new Date()), false, new Timestamp(new Date()), WelcomeActivity.USER.getUid(), false);
        createNoteCallBack(note);
    }

    private void createNoteCallBack(Note note) {
        NoteDAO nDAO = new NoteDAO();
        nDAO.createNote(new NoteDAO.FirebaseCallBack() {
            @Override
            public void onCallBack(ArrayList<Note> noteList) {
            }
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onCallBack() {
                setTimerNotify(note);
            }
        },note);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.note_menu, menu);
        noteMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menuPinNote:
                break;
            case R.id.menuNoticeNote:
                break;
            case R.id.menuDeleteNote:
                finish();
                break;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        bindingView();
        bindingAction();
    }
}