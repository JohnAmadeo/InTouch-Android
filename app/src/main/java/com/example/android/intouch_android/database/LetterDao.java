package com.example.android.intouch_android.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.android.intouch_android.model.Letter;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;

@Dao
public interface LetterDao {

    @Query("SELECT * FROM letters WHERE NOT(isDraft) ORDER BY timeSent DESC ")
    LiveData<List<Letter>> getLetters();

    @Query("SELECT * FROM letters WHERE id = :letterId")
    Single<Letter> getLetter(String letterId);

    @Query("SELECT * FROM letters WHERE isDraft ORDER BY timeSent DESC ")
    LiveData<List<Letter>> getDrafts();

    @Query("SELECT * FROM letters WHERE isDraft AND id = :letterId")
    LiveData<Letter> getDraft(String letterId);

    @Query("" +
            "UPDATE letters " +
            "SET recipientId = :recipientId, recipient = :recipient " +
            "WHERE id = :letterId"
    )
    void updateDraftRecipient(String letterId, String recipientId, String recipient);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLetter(Letter letter);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertLetters(List<Letter> letters);

    @Query("DELETE FROM letters")
    void deleteAllLetters_DANGEROUS();

    @Query("DELETE FROM letters WHERE id = :id")
    void delete_DANGEROUS(String id);
}
