package com.example.prm_392_nho_ho_ban.dao;

import android.annotation.SuppressLint;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.prm_392_nho_ho_ban.bean.Note;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NoteDAO {
    private final FirebaseFirestore db =  FirebaseFirestore.getInstance();

    private CollectionReference noteRef = db.collection("note");

    private ArrayList<Note> notes = new ArrayList<>();

    public void getAllNoteCallBack(FirebaseCallBack firebaseCallBack){
        ArrayList<Note> noteList = new ArrayList<>();
        ArrayList<Note> noteUnpinList = new ArrayList<>();
        noteRef
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Note note = document.toObject(Note.class);
                            note.setDateCreate(document.getTimestamp("dateCreate"));
                            note.setDateRemind(document.getTimestamp("dateRemind"));
                            if(note.isPin()){
                                noteList.add(note);
                            }else{
                                noteUnpinList.add(note);
                            }
                        }
                        firebaseCallBack.onCallBack(noteList,noteUnpinList);
                    }
                });
    }
    public void getAllUpcomingNoteCallBack(FirebaseCallBack firebaseCallBack, FirebaseUser user){
        ArrayList<Note> noteList = new ArrayList<>();
        ArrayList<Note> noteUnpinList = new ArrayList<>();
        Timestamp dateStartTimestamp = new Timestamp(new Date());
        noteRef.whereGreaterThanOrEqualTo("dateRemind",dateStartTimestamp)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Note note = document.toObject(Note.class);
                            note.setDateCreate(document.getTimestamp("dateCreate"));
                            note.setDateRemind(document.getTimestamp("dateRemind"));
                            if(note.isPin()){
                                noteList.add(note);
                            }else{
                                noteUnpinList.add(note);
                            }
                        }
                        firebaseCallBack.onCallBack(noteList, noteUnpinList);
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

        noteRef.whereGreaterThanOrEqualTo("dateRemind",dateStartTimestamp).whereLessThanOrEqualTo("dateRemind",dateEndTimestamp)
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

    public void getAllNoteByDayCallBack2(FirebaseCallBack firebaseCallBack, Date dateStart, Date dateEnd, FirebaseUser user, Context context) {
        ArrayList<Note> noteList = new ArrayList<>();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
        final long MILLI_SECOND_IN_DAY = 86400000;
        long timeDateEnd = dateEnd.getTime() + MILLI_SECOND_IN_DAY;
        Timestamp dateStartTimestamp = new Timestamp(dateStart);
        Timestamp dateEndTimestamp;
        dateEndTimestamp = new Timestamp(new Date(timeDateEnd));
        long time = System.currentTimeMillis();
        noteRef.whereGreaterThanOrEqualTo("dateRemind",dateStartTimestamp).whereLessThanOrEqualTo("dateRemind",dateEndTimestamp)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        long timee = System.currentTimeMillis() -  time;
                        Toast.makeText(context,timee+"bb", Toast.LENGTH_LONG).show();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Note note = document.toObject(Note.class);
                                note.setId(document.getId());
                                note.setDateCreate(document.getTimestamp("dateCreate"));
                                note.setDateRemind(document.getTimestamp("dateRemind"));

                                noteList.add(note);

                            }
                        } else {
                            Log.d("QUERY", "Error getting documents: ", task.getException());
                        }
                        firebaseCallBack.onCallBack(noteList);
                    }
                });

    }


    public void getAllPinByDayCallBack(FirebaseCallBack firebaseCallBack, Date dateStart, Date dateEnd, boolean pin) {
        ArrayList<Note> noteList = new ArrayList<>();
        ArrayList<Note> noteUnpinList = new ArrayList<>();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
        final long MILLI_SECOND_IN_DAY = 86400000;
        long timeDateEnd = dateEnd.getTime() + MILLI_SECOND_IN_DAY;
        Timestamp dateStartTimestamp = new Timestamp(dateStart);
        Timestamp dateEndTimestamp;
        dateEndTimestamp = new Timestamp(new Date(timeDateEnd));
        noteRef.whereGreaterThanOrEqualTo("dateRemind",dateStartTimestamp).whereLessThanOrEqualTo("dateRemind",dateEndTimestamp)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.i("TAG","YES");
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Note note = document.toObject(Note.class);
                            note.setId(document.getId());
                            note.setDateCreate(document.getTimestamp("dateCreate"));
                            note.setDateRemind(document.getTimestamp("dateRemind"));
                            if(note.isPin()){
                                noteList.add(note);
                            }else{
                                noteUnpinList.add(note);
                            }
                        }
                        firebaseCallBack.onCallBack(noteList,noteUnpinList);
                    }
                });

    }

    public void offline(FirebaseCallBack firebaseCallBack,Date dateStart, Date dateEnd){
        ArrayList<Note> noteList = new ArrayList<>();
        ArrayList<Note> noteUnpinList = new ArrayList<>();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
        final long MILLI_SECOND_IN_DAY = 86400000;
        long timeDateEnd = dateEnd.getTime() + MILLI_SECOND_IN_DAY;
        Timestamp dateStartTimestamp = new Timestamp(dateStart);
        Timestamp dateEndTimestamp;
        dateEndTimestamp = new Timestamp(new Date(timeDateEnd));
        noteRef.whereGreaterThanOrEqualTo("dateRemind",dateStartTimestamp).whereLessThanOrEqualTo("dateRemind",dateEndTimestamp)
                .addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot querySnapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("TAG", "Listen error", e);
                            return;
                        }

                        for (DocumentChange change : querySnapshot.getDocumentChanges()) {
                            if (change.getType() == DocumentChange.Type.ADDED) {
                                Log.d("TAG", "New city:" + change.getDocument().getData());
                            }

                            String source = querySnapshot.getMetadata().isFromCache() ?
                                    "local cache" : "server";
                            Log.d("TAG", "Data fetched from " + source);
                        }

                    }
                });
    }


    public void addNote(FirebaseCallBack firebaseCallBack, Note note) {
        noteRef.add(note).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                firebaseCallBack.onCallBack();
            }
        });
    }

    public void createNote(FirebaseCallBack firebaseCallBack, Note note) {
        noteRef.add(note).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                firebaseCallBack.onCallBack();
            }
        });
    }

    public void updateNote(FirebaseCallBack firebaseCallBack, Note note, String id) {
        noteRef.document(id).set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        firebaseCallBack.onCallBack();
                    }
                });
    }

    public void deleteNote(FirebaseCallBack firebaseCallBack, String id) {
        noteRef.document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                firebaseCallBack.onCallBack();
            }
        });
    }

    public void pinNote(FirebaseCallBack firebaseCallBack, String id,Boolean pin) {
        noteRef.document(id).update("pin", pin)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                firebaseCallBack.onCallBack();
            }
        });
    }

    public void getAllNoteByUser(FirebaseCallBack firebaseCallBack) {
        ArrayList<Note> noteList = new ArrayList<>();
        noteRef
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
        void onCallBack(ArrayList<Note> noteList, ArrayList<Note> noteUnpinList);
    }

}
