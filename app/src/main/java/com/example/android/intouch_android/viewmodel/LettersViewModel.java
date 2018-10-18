package com.example.android.intouch_android.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.android.intouch_android.repository.LettersRepository;
import com.example.android.intouch_android.model.Letter;

import java.util.List;

public class LettersViewModel extends AndroidViewModel {

//    private LiveData<List<Letter>> mLetters;
    private LettersRepository mLettersRepo;

    public LettersViewModel(@NonNull Application application) {
        super(application);
        mLettersRepo = new LettersRepository(application.getApplicationContext());
    }

    public LiveData<List<Letter>> getLettersStream() {
        return mLettersRepo.getLettersStream();
    }

    public void refreshLetters() {
         mLettersRepo.refreshLetters();
    }
}
