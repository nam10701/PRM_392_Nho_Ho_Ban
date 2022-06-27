package com.example.prm_392_nho_ho_ban.bean;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
@Entity(tableName = "user")
public class User {
    private static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public final static FirebaseUser USER = firebaseAuth.getCurrentUser();

    @PrimaryKey
    @NonNull
    private String id;
    @ColumnInfo
    private String email;

    public User(String id, String email) {
        this.id = id;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
