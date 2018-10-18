package com.example.android.intouch_android.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.android.intouch_android.model.Letter;

import java.util.List;

@Dao
public interface LetterDao {

    @Query("SELECT * FROM letters WHERE NOT(is_draft) ORDER BY time_sent DESC ")
    LiveData<List<Letter>> getLetters();

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insertLetter(Letter letter);
}
