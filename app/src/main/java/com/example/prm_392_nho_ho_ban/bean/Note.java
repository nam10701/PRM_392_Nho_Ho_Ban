package com.example.prm_392_nho_ho_ban.bean;

import com.google.firebase.Timestamp;

import java.util.Date;

public class Note {
    private String id;
    private String title;
    private String content;
    private Timestamp date;
//    private Timestamp dateCreate;
//    boolean alarm;
//    private Timestamp dateRemind;

    public Note() {
    }

    public Note(String id, String title, String content, Timestamp date) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp time) {
        this.date = time;
    }
}
