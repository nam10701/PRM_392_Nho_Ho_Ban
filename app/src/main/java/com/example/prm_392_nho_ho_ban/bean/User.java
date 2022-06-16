package com.example.prm_392_nho_ho_ban.bean;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class User {
    private static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public final static FirebaseUser USER = firebaseAuth.getCurrentUser();

    private int id;
    private String email;

    public User(int id, String email) {
        this.id = id;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
