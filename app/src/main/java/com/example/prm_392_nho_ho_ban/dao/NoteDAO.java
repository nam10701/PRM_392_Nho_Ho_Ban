package com.example.prm_392_nho_ho_ban.dao;

import android.util.Log;

import com.example.prm_392_nho_ho_ban.bean.Note;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
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
                            Timestamp ts = document.getTimestamp("date");
//                            Date date = new Date(document.toObject(Note.class).getTime().getSeconds());
//                            Log.i("=====", document.getId() + " => " + date);
                            Note n = document.toObject(Note.class);
                            n.setTime(ts);
                            noteList.add(n);
                            Log.i("========after",noteList.size()+"");
                        }
                        firebaseCallBack.onCallBack(noteList);
                    }
                });
    }

    public interface FirebaseCallBack{
        void onCallBack(ArrayList<Note> noteList);
    }

}
