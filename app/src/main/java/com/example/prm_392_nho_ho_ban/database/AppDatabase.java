package com.example.prm_392_nho_ho_ban.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.prm_392_nho_ho_ban.activity.Converters;
import com.example.prm_392_nho_ho_ban.dao.RoomNoteDAO;
import com.example.prm_392_nho_ho_ban.dao.RoomUserDAO;
import com.example.prm_392_nho_ho_ban.room_entities.Note;
import com.example.prm_392_nho_ho_ban.room_entities.User;

import java.sql.Timestamp;

@TypeConverters(Converters.class)
@Database(entities = {Note.class, User.class} ,version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract RoomNoteDAO createNoteDAO();

    public abstract RoomUserDAO createUserDAO();
}
