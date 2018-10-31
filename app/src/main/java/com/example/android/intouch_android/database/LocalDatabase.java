package com.example.android.intouch_android.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;

import com.example.android.intouch_android.model.Correspondence;
import com.example.android.intouch_android.model.Inmate;
import com.example.android.intouch_android.model.Letter;

@Database(
        entities = {Letter.class, Inmate.class, Correspondence.class},
        version = 7,
        exportSchema = false
)
@TypeConverters(DateConverter.class)
public abstract class LocalDatabase extends RoomDatabase {
    private static final String LOG_TAG = LocalDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "intouch";

    private static LocalDatabase sInstance;

    public static LocalDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(
                        context.getApplicationContext(),
                        LocalDatabase.class,
                        LocalDatabase.DATABASE_NAME
                )
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract LetterDao letterDao();
    public abstract InmateDao inmateDao();
}
