package com.example.prm_392_nho_ho_ban.activity;

import static com.example.prm_392_nho_ho_ban.MyApplication.INTERNET_STATE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.prm_392_nho_ho_ban.R;
import com.example.prm_392_nho_ho_ban.bean.Note;
import com.example.prm_392_nho_ho_ban.bean.User;
import com.example.prm_392_nho_ho_ban.dao.NoteDAO;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;



public class EditNoteActivity extends AppCompatActivity {

    private EditText edtTitle;
    private EditText edtaNote;
    private Button btnSave;

    private TextView txtId;

    private FirebaseAuth firebaseAuth;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference noteRef = db.collection("note");

    private Toolbar noteToolbar;
    private Menu noteMenu;

    private boolean notePin;
    private Timestamp createDate;
    private Timestamp remindDate;

    public void bindingView() {
        edtTitle = findViewById(R.id.edtTitleEdit);
        edtaNote = findViewById(R.id.edtaNoteEdit);
        btnSave = findViewById(R.id.btnSaveNoteEdit);
        txtId = findViewById(R.id.noteId);

        firebaseAuth = FirebaseAuth.getInstance();

        noteToolbar = findViewById(R.id.toolbar);

    }

    public void bindingAction() {
        btnSave.setOnClickListener(this::onBtnSaveClick);

        setSupportActionBar(noteToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        intitialIntent();
    }

    private void intitialIntent() {
        Intent receiveIntent = getIntent();
        String title = receiveIntent.getStringExtra("title");
        String content = receiveIntent.getStringExtra("content");
        String id = receiveIntent.getStringExtra("id");
        boolean pin = receiveIntent.getExtras().getBoolean("pin");
        Timestamp createTime = new Timestamp(new Date(receiveIntent.getExtras().getLong("create")));
        Timestamp remindTime = new Timestamp(new Date(receiveIntent.getExtras().getLong("remind")));
//        edtTitle.setText(String.valueOf(pin));
        edtTitle.setText(title);
        edtaNote.setText(content);
        txtId.setText(id);
        notePin = pin;
        createDate = createTime;
        remindDate = remindTime;
    }

    private void onBtnSaveClick(View view) {
        String title = edtTitle.getText().toString().trim();
        String content = edtaNote.getText().toString().trim();
        String id = txtId.getText().toString().trim();

        if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(content)) {
            updateNote(title,content, id);

        } else {
            Snackbar.make(view,"Please fill empty fields!", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void updateNote(String title, String content, String id) {
        Note updateNote = new Note(id ,title,content, createDate, false, remindDate, User.USER.getUid(), notePin);
        updateNoteDataCallBack(updateNote, id);
    }

    private void updateNoteDataCallBack(Note updateNote, String id) {
        NoteDAO nDAO = new NoteDAO();
        nDAO.updateNote(new NoteDAO.FirebaseCallBack() {
            @Override
            public void onCallBack(ArrayList<Note> noteList) {
            }
            @Override
            public void onCallBack() {
                WelcomeActivity.updateFragment();
                finish();
            }

            @Override
            public void onCallBack(ArrayList<Note> noteList, ArrayList<Note> noteUnpinList) {

            }
        },updateNote, id);
        if(!INTERNET_STATE){
            Log.i("Internet Add","Yess");
            finish();
        }
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
                pinNote();
                break;
            case R.id.menuNoticeNote:
                break;
            case R.id.menuDeleteNote:
                deleteNote();
                break;
        }
        return true;
    }

    private void pinNote() {
        String id = txtId.getText().toString().trim();
        if(String.valueOf(notePin).equals("true")) {
            notePin = false;
            pinNoteCallBack(id,notePin);
        }
        else if(String.valueOf(notePin).equals("false")) {
            notePin = true;
            pinNoteCallBack(id,notePin);
        }
    }

    private void pinNoteCallBack(String id, boolean notePin) {
        NoteDAO nDAO = new NoteDAO();
        nDAO.pinNote(new NoteDAO.FirebaseCallBack() {
            @Override
            public void onCallBack(ArrayList<Note> noteList) {
            }
            @Override
            public void onCallBack() {
                finish();
            }

            @Override
            public void onCallBack(ArrayList<Note> noteList, ArrayList<Note> noteUnpinList) {

            }
        }, id, notePin);
    }

    private void deleteNote() {
        String id = txtId.getText().toString().trim();
        deleteNoteCallBack(id);
    }

    private void deleteNoteCallBack(String id) {
        NoteDAO nDAO = new NoteDAO();
        nDAO.deleteNote(new NoteDAO.FirebaseCallBack() {
            @Override
            public void onCallBack(ArrayList<Note> noteList) {
            }
            @Override
            public void onCallBack() {
                WelcomeActivity.updateFragment();
                finish();
            }

            @Override
            public void onCallBack(ArrayList<Note> noteList, ArrayList<Note> noteUnpinList) {

            }
        }, id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        bindingView();
        bindingAction();

    }
}