package com.example.android.intouch_android.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.android.intouch_android.model.User;

import java.util.List;

import retrofit2.http.GET;

@Dao
public interface UserDao {
    @Query("DELETE FROM users")
    void deleteAllUsers();

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void saveUser(User user);

    @Query("SELECT * FROM users")
    LiveData<List<User>> getUsers();
}
