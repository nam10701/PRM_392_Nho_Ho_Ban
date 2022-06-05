package com.example.prm_392_nho_ho_ban.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Date;

public class AddNoteActivity extends AppCompatActivity {

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

    public void bindingAction() {
        btnSave.setOnClickListener(this::onBtnSaveClick);

        setSupportActionBar(noteToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void onBtnSaveClick(View view) {
        String title = edtTitle.getText().toString().trim();
        String content = edtaNote.getText().toString().trim();

        if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(content)) {
            createNote(view ,title, content);
            Intent n = new Intent(this,WelcomeActivity.class);
            startActivity(n);
        } else {
            Snackbar.make(view,"Please fill empty fields!", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void createNote(View view, String title, String content) {
        Note note = new Note("",title, content, new Timestamp(new Date()), false, new Timestamp(new Date()), WelcomeActivity.USER.getUid());
        createNoteCallBack(note);
    }

    private void createNoteCallBack(Note note) {
        NoteDAO nDAO = new NoteDAO();
        nDAO.createNote(new NoteDAO.FirebaseCallBack() {
            @Override
            public void onCallBack(ArrayList<Note> noteList) {
            }
            @Override
            public void onCallBack() {
            }
        },note);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        bindingView();
        bindingAction();
    }
}