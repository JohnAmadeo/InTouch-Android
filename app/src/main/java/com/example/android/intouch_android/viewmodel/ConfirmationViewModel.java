package com.example.android.intouch_android.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.example.android.intouch_android.model.User;
import com.example.android.intouch_android.repository.UserRepository;

public class ConfirmationViewModel extends AndroidViewModel {
    private UserRepository mUserRepository;

    public ConfirmationViewModel(@NonNull Application application) {
        super(application);
        mUserRepository = new UserRepository(application.getApplicationContext());
    }

    public boolean isPlaceholderUser() {
        return mUserRepository.getUserFromAppState().isPlaceholderUser();
    }

    public void saveUser(User user) { mUserRepository.saveUser(user); }
}
