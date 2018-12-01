package com.example.android.intouch_android.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.example.android.intouch_android.model.User;
import com.example.android.intouch_android.repository.LettersRepository;
import com.example.android.intouch_android.repository.UserRepository;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ConfirmationViewModel extends AndroidViewModel {
    private UserRepository mUserRepository;
    private LettersRepository mLettersRepository;

    public ConfirmationViewModel(@NonNull Application application) {
        super(application);
        mUserRepository = new UserRepository(application.getApplicationContext());
        mLettersRepository = new LettersRepository(application.getApplicationContext());
    }

    public boolean isPlaceholderUser() {
        return mUserRepository.getUserFromAppState().isPlaceholderUser();
    }

    public void saveUser(User user) { mUserRepository.saveUser(user); }

    public Single<String> getTimeDeliveredEstimate(String letterId) {
        return mLettersRepository.getLetterAsObservable(letterId)
                .map(letter -> letter.getTimeDeliveredEstimateString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
