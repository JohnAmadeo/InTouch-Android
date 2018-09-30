package com.example.android.intouch_android;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class LettersViewModel extends AndroidViewModel {

    private LiveData<List<Letter>> letters;

    public LettersViewModel(@NonNull Application application) {
        super(application);
        LettersRepository lettersRepo = new LettersRepository();
        letters = lettersRepo.getLetters();
    }

    public LiveData<List<Letter>> getLetters() {
        return letters;
    }
}
