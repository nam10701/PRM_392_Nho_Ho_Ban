package com.example.prm_392_nho_ho_ban.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.prm_392_nho_ho_ban.bean.Note;
import com.example.prm_392_nho_ho_ban.bean.User;
import com.example.prm_392_nho_ho_ban.dao.NoteDAO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
            firebaseAuth.signInWithEmailAndPassword(userEmail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        final FirebaseUser user = firebaseAuth.getCurrentUser();
                        User.USER = user;
                        NoteDAO noteDAO = new NoteDAO();
                        noteDAO.syncFirebaseToRoom(new NoteDAO.FirebaseCallBack() {
                            @Override
                            public void onCallBack(ArrayList<Note> noteList) {

                            }

                            @Override
                            public void onCallBack() {
                                Log.i("USer", user.getUid() + user.getEmail());

                                progressDialog.dismiss();
                                startActivity(new Intent(getApplicationContext(), WelcomeActivity.class));
                            }

                            @Override
                            public void onCallBack(ArrayList<Note> noteList, ArrayList<Note> noteUnpinList) {
                            }
                        }, user.getUid());
                    } else {
                        finish();
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            progressDialog.dismiss();
        }

        }
    }