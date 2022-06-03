package com.example.prm_392_nho_ho_ban.dao;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.prm_392_nho_ho_ban.activity.WelcomeActivity;
import com.example.prm_392_nho_ho_ban.bean.Note;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.lang.reflect.Array;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NoteDAO {
    private final FirebaseFirestore db =  FirebaseFirestore.getInstance();
    private ArrayList<Note> notes = new ArrayList<>();

    public void getAllNoteCallBack(FirebaseCallBack firebaseCallBack){
        ArrayList<Note> noteList = new ArrayList<>();
        db.collection("note")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Note note = document.toObject(Note.class);
                            note.setDateCreate(document.getTimestamp("dateCreate"));
                            note.setDateRemind(document.getTimestamp("dateRemind"));
                            noteList.add(note);
                        }
                        firebaseCallBack.onCallBack(noteList);
                    }
                });
    }

    public void getAllNoteByDayCallBack(FirebaseCallBack firebaseCallBack, Date dateStart, Date dateEnd) {
        ArrayList<Note> noteList = new ArrayList<>();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
        final long MILLI_SECOND_IN_DAY = 86400000;
        long timeDateEnd = dateEnd.getTime() + MILLI_SECOND_IN_DAY;
        Timestamp dateStartTimestamp = new Timestamp(dateStart);
        Timestamp dateEndTimestamp;
        dateEndTimestamp = new Timestamp(new Date(timeDateEnd));

        db.collection("note").whereGreaterThanOrEqualTo("dateRemind",dateStartTimestamp).whereLessThanOrEqualTo("dateRemind",dateEndTimestamp)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Note note = document.toObject(Note.class);
                            note.setDateCreate(document.getTimestamp("dateCreate"));
                            note.setDateRemind(document.getTimestamp("dateRemind"));
                            noteList.add(note);
                            Log.i("========after",noteList.size()+"");
                        }
                        firebaseCallBack.onCallBack(noteList);
                    }
                });

    }

    public void addNote(FirebaseCallBack firebaseCallBack, Note note) {
        db.collection("note").add(note).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                firebaseCallBack.onCallBack();
            }
        });
    }


    public interface FirebaseCallBack{
        void onCallBack(ArrayList<Note> noteList);
        void onCallBack();
    }

}
