package com.example.prm_392_nho_ho_ban.dao;

import android.annotation.SuppressLint;

import android.util.Log;

import com.example.prm_392_nho_ho_ban.bean.Note;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NoteDAO {
    private final FirebaseFirestore db =  FirebaseFirestore.getInstance();

    private CollectionReference noteRef = db.collection("note");

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

    public void getAllNoteByDayCallBack(FirebaseCallBack firebaseCallBack, Date dateStart, Date dateEnd, FirebaseUser user) {
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
                            note.setId(document.getId());
                            note.setDateCreate(document.getTimestamp("dateCreate"));
                            note.setDateRemind(document.getTimestamp("dateRemind"));
                            if(note.getPin() == false) {
                                noteList.add(note);
                                Log.i("========after",noteList.size()+"");
                            }
//                            noteList.add(note);
//                            Log.i("========after",noteList.size()+"");
                        }
                        firebaseCallBack.onCallBack(noteList);
                    }
                });

    }


    public void getAllPinByDayCallBack(FirebaseCallBack firebaseCallBack, Date dateStart, Date dateEnd, boolean pin) {
        ArrayList<Note> noteList = new ArrayList<>();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
        final long MILLI_SECOND_IN_DAY = 86400000;
        long timeDateEnd = dateEnd.getTime() + MILLI_SECOND_IN_DAY;
        Timestamp dateStartTimestamp = new Timestamp(dateStart);
        Timestamp dateEndTimestamp;
        dateEndTimestamp = new Timestamp(new Date(timeDateEnd));

        db.collection("note").whereGreaterThanOrEqualTo("dateRemind",dateStartTimestamp).whereLessThanOrEqualTo("dateRemind",dateEndTimestamp)
                .whereEqualTo("pin",pin)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Note note = document.toObject(Note.class);
                            note.setId(document.getId());
                            note.setDateCreate(document.getTimestamp("dateCreate"));
                            note.setDateRemind(document.getTimestamp("dateRemind"));
                            noteList.add(note);
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

    public void createNote(FirebaseCallBack firebaseCallBack, Note note) {
        db.collection("note").add(note).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                firebaseCallBack.onCallBack();
            }
        });
    }

    public void updateNote(FirebaseCallBack firebaseCallBack, Note note, String id) {
        db.collection("note").document(id).set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        firebaseCallBack.onCallBack();
                    }
                });
    }

    public void deleteNote(FirebaseCallBack firebaseCallBack, String id) {
        db.collection("note").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                firebaseCallBack.onCallBack();
            }
        });
    }

    public void pinNote(FirebaseCallBack firebaseCallBack, String id,Boolean pin) {
        db.collection("note").document(id).update("pin", pin)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                firebaseCallBack.onCallBack();
            }
        });
    }

    public void getAllNoteByUser(FirebaseCallBack firebaseCallBack) {
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

    public interface FirebaseCallBack{
        void onCallBack(ArrayList<Note> noteList);
        void onCallBack();

    }

}
