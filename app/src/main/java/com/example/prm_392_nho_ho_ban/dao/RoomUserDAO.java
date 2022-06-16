package com.example.prm_392_nho_ho_ban.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import com.example.prm_392_nho_ho_ban.room_entities.User;

import java.util.ArrayList;

@Dao
public interface RoomUserDAO {

    @Insert
    public void insert(ArrayList<User> list);
    @Update
    public void update(ArrayList<User> list);
    @Delete
    public void delete(ArrayList<User> list);
}

