package com.example.prm_392_nho_ho_ban.room_entities;

import androidx.annotation.NonNull;
import com.google.firebase.Timestamp;

public class Note {
    private String id;
    private String title;
    private String content;
    private Timestamp dateCreate;
    private boolean alarm;
    private Timestamp dateRemind;
    private String uId;
    private boolean pin;

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
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

    public String getUId() {
        return uId;
    }

    public void setUId(String uId) {
        this.uId = uId;
    }

    public boolean isPin() {
        return pin;
    }

    public void setPin(boolean pin) {
        this.pin = pin;
    }

}