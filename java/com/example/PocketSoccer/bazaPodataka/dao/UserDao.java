package com.example.kf150605d.pocketsoccer.bazaPodataka.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.kf150605d.pocketsoccer.bazaPodataka.entity.User;
import java.util.List;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertUser(User user);

    @Update
    void updateUser(User user);

    @Query("DELETE FROM user_table")
    void deleteAllUsers();

    @Query("SELECT * FROM user_table WHERE current = 1")
    LiveData<List<User>> getCurrentUsers();


    @Query("SELECT * FROM user_table")
    LiveData<List<User>> getAllUsers();

    @Delete
    void deleteUser(User user);

}

