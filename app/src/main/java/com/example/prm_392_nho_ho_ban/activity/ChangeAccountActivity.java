package com.example.prm_392_nho_ho_ban.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.example.prm_392_nho_ho_ban.bean.Note;
import com.example.prm_392_nho_ho_ban.bean.User;
import com.example.prm_392_nho_ho_ban.dao.NoteDAO;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ChangeAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.show();
        if(getIntent()!=null){
            String userEmail = getIntent().getStringExtra("userEmail");
            String password = getIntent().getStringExtra("password");
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signOut();
            firebaseAuth.signInWithEmailAndPassword(userEmail,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    NoteDAO noteDao = new NoteDAO();
                    noteDao.syncFirebaseToRoom(new NoteDAO.FirebaseCallBack() {
                        @Override
                        public void onCallBack(ArrayList<Note> noteList) {

                        }

                        @Override
                        public void onCallBack() {
                            progressDialog.dismiss();
                            startActivity(new Intent(getApplicationContext(), WelcomeActivity.class));
                        }

                        @Override
                        public void onCallBack(ArrayList<Note> noteList, ArrayList<Note> noteUnpinList) {

                        }
                    },User.USER.getUid());
                }
            });

        }
    }
}