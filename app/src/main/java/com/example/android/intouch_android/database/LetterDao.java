package com.example.android.intouch_android.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.android.intouch_android.model.Letter;

import java.util.List;

@Dao
public interface LetterDao {

    @Query("SELECT * FROM letters WHERE NOT(isDraft) ORDER BY timeSent DESC ")
    LiveData<List<Letter>> getLetters();

    @Query("SELECT * FROM letters WHERE isDraft ORDER BY timeSent DESC ")
    LiveData<List<Letter>> getDrafts();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertLetter(Letter letter);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertLetters(List<Letter> letters);

    @Query("DELETE FROM letters")
    void deleteAllLetters_DANGEROUS();
}
