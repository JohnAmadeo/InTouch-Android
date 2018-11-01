package com.example.android.intouch_android.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.content.Context;

import com.example.android.intouch_android.api.Webservice;
import com.example.android.intouch_android.api.WebserviceProvider;
import com.example.android.intouch_android.database.LocalDatabase;
import com.example.android.intouch_android.model.User;
import com.example.android.intouch_android.utils.AppExecutors;
import com.example.android.intouch_android.utils.AppState;

import java.util.List;

public class UserRepository {
    private final String LOG_TAG = this.getClass().getSimpleName();
    private final Webservice mWebservice;
    private final LocalDatabase mDB;
    private final AppExecutors mExecutors;
    private final AppState mAppState;

    public UserRepository(Context context) {
        mWebservice = WebserviceProvider.getInstance();
        mDB = LocalDatabase.getInstance(context);
        mExecutors = AppExecutors.getInstance();
        mAppState = AppState.getInstance();
    }

    public void saveUser(User user) {
        mAppState.setUser(user);
        mExecutors.diskIO().execute(() -> {
            mDB.beginTransaction();
            try {
                mDB.userDao().deleteAllUsers();
                mDB.userDao().saveUser(user);
                mDB.setTransactionSuccessful();
            } finally {
                mDB.endTransaction();
            }
        });
    }

    public User getUserFromAppState() { return mAppState.getUser(); }

    public LiveData<User> getUserFromDB() {
        return Transformations.map(
                mDB.userDao().getUsers(),
                users -> {
                    if (users.size() != 1) {
                        return null;
                    }
                    else {
                        return users.get(0);
                    }
                }
        );
    }
}
