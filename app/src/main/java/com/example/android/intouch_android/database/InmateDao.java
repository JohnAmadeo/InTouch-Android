package com.example.android.intouch_android.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.android.intouch_android.model.Correspondence;
import com.example.android.intouch_android.model.Inmate;

import java.util.List;

@Dao
public interface InmateDao {
    @Query("SELECT inmates.* FROM " +
            "inmates JOIN " +
            "(SELECT * FROM correspondences WHERE username = :username) AS correspondences " +
            "ORDER BY correspondences.occurrences DESC "
    )
    LiveData<List<Inmate>> getPastInmateCorrespondents(String username);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertInmate_TEST(Inmate inmate);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertCorrespondence_TEST(Correspondence correspondence);

    @Query("DELETE FROM inmates")
    void deleteInmates_TEST();

    @Query("DELETE FROM correspondences")
    void deleteCorrespondences_TEST();
}
