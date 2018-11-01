package com.example.android.intouch_android.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.android.intouch_android.model.User;
import com.example.android.intouch_android.repository.UserRepository;

public class LaunchViewModel extends AndroidViewModel {
    private UserRepository mUserRepository;

    public LaunchViewModel(@NonNull Application application) {
        super(application);
        mUserRepository = new UserRepository(application.getApplicationContext());
    }

    public void saveUser(User user) {
        mUserRepository.saveUser(user);
    }
    public LiveData<User> getUser() {
        MediatorLiveData<User> result = new MediatorLiveData<>();
        User userFromAppState = mUserRepository.getUserFromAppState();
        if (userFromAppState != null) {
            result.setValue(userFromAppState);
        }
        else {
            LiveData<User> userFromDB = mUserRepository.getUserFromDB();
            result.addSource(
                    userFromDB,
                    user -> {
                        result.removeSource(userFromDB);
                        result.setValue(user);
                    }
            );
        }
        return result;
    }
}
