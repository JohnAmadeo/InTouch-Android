package com.example.android.intouch_android.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.example.android.intouch_android.model.User;
import com.example.android.intouch_android.model.container.Status;
import com.example.android.intouch_android.repository.UserRepository;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ProfileViewModel extends AndroidViewModel {
    private UserRepository mUserRepository;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        mUserRepository = new UserRepository(application.getApplicationContext());
    }

    public @NonNull User getUser() {
        return mUserRepository.getUserFromAppState();
    }

    public Single<Status> saveUser(User user) {
        return mUserRepository.saveUserWithObservable(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
