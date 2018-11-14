package com.example.android.intouch_android.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.example.android.intouch_android.model.Letter;
import com.example.android.intouch_android.repository.LettersRepository;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LetterViewerViewModel extends AndroidViewModel {
    private final String LOG_TAG = this.getClass().getSimpleName();
    private LettersRepository mLettersRepository;

    public LetterViewerViewModel(@NonNull Application application) {
        super(application);
        mLettersRepository = new LettersRepository(application.getApplicationContext());
    }

    public Single<Letter> getLetter(String letterId) {
        return mLettersRepository.getLetterAsObservable(letterId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
