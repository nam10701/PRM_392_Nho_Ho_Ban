package com.example.prm_392_nho_ho_ban.dao;

import static android.icu.text.MessagePattern.ArgType.SELECT;

import static com.google.common.net.HttpHeaders.FROM;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.prm_392_nho_ho_ban.room_entities.Note;
import com.example.prm_392_nho_ho_ban.room_entities.User;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface RoomNoteDAO {
        @Insert
        public void insert(ArrayList<Note> list);
        @Update
        public void update(ArrayList<Note> list);
        @Delete
        public void delete(ArrayList<Note> list);

        @Query("SELECT * FROM note WHERE uId = :uId AND dateRemind>= :firstDay AND dateRemind<= :lastDay AND pin= :pin AND alarm= :alarm")
        List<Note> getAllNoteByDay(Timestamp firstDay, Timestamp lastDay, String uId, boolean pin, boolean alarm);

    }

