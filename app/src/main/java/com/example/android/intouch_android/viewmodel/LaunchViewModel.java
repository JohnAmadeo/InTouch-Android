package com.example.android.intouch_android.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.android.intouch_android.model.User;
import com.example.android.intouch_android.repository.LettersRepository;
import com.example.android.intouch_android.repository.UserRepository;
import com.example.android.intouch_android.utils.AppState;

public class LaunchViewModel extends AndroidViewModel {
    private UserRepository mUserRepository;
    private MutableLiveData<Boolean> mIsLoggingIn = new MutableLiveData<>();

    public LaunchViewModel(@NonNull Application application) {
        super(application);
        mUserRepository = new UserRepository(application.getApplicationContext());
    }

    public void saveUser(User user) {
        mUserRepository.saveUser(user);
    }

    public void setIsLoggingIn(Boolean isLoggingIn) { mIsLoggingIn.postValue(isLoggingIn); }
    public LiveData<Boolean> getIsLoggingIn() { return this.mIsLoggingIn; }
}
