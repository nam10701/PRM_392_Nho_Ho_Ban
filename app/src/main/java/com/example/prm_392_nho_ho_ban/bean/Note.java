package com.example.prm_392_nho_ho_ban.bean;

import com.google.firebase.Timestamp;

public class Note {
    private String id;
    private String title;
    private String content;
    private Timestamp dateCreate;
    private boolean alarm;
    private Timestamp dateRemind;
    private String uId;

    public Note() {
    }

    public Note(String id, String title, String content, Timestamp dateCreate, boolean alarm, Timestamp dateRemind, String uId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.dateCreate = dateCreate;
        this.alarm = alarm;
        this.dateRemind = dateRemind;
        this.uId = uId;
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

    public Timestamp getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Timestamp dateCreate) {
        this.dateCreate = dateCreate;
    }

    public boolean isAlarm() {
        return alarm;
    }

    public void setAlarm(boolean alarm) {
        this.alarm = alarm;
    }

    public Timestamp getDateRemind() {
        return dateRemind;
    }

    public void setDateRemind(Timestamp dateRemind) {
        this.dateRemind = dateRemind;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }
}
