package com.example.prm_392_nho_ho_ban.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.prm_392_nho_ho_ban.bean.Note;

import java.sql.Time;
import com.google.firebase.Timestamp;
import java.util.List;

@Dao
public interface RoomNoteDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Note note);
    @Delete
    void delete(Note note);
    @Update
    void update(Note note);

    @Query("SELECT * FROM note WHERE uId = :uId")
    List<Note> getAllNote(String uId);

    @Query("SELECT * FROM note WHERE dateRemind > :date AND uId = :uId AND pin= :isPin")
    List<Note> getAllUpcomingNote(Timestamp date,boolean isPin, String uId);

    @Query("SELECT * FROM note WHERE dateRemind > :firstDay AND dateRemind<:lastDay AND uId = :uId")
    List<Note> getAllNoteByDay(Timestamp firstDay, Timestamp lastDay, String uId);

    @Query("SELECT * FROM note WHERE dateRemind > :firstDay AND dateRemind < :lastDay AND pin = :isPin AND uId = :uId")
    List<Note> getAllPinByDay(Timestamp firstDay, Timestamp lastDay, boolean isPin, String uId);

    @Query("SELECT * FROM note WHERE pin = :isPin AND uId = :uId")
    List<Note> getAllPin(boolean isPin, String uId);

    @Query("SELECT * FROM note WHERE uId = :uId ORDER BY dateCreate DESC LIMIT 1")
    Note getLatestNote(String uId);


}
