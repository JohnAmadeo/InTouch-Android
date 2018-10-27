package com.example.android.intouch_android.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.example.android.intouch_android.model.Letter;
import com.example.android.intouch_android.repository.LettersRepository;

public class LetterEditorViewModel extends AndroidViewModel {
    private LettersRepository mLettersRepository;

    public LetterEditorViewModel(@NonNull Application application) {
        super(application);
        mLettersRepository = new LettersRepository(application.getApplicationContext());
    }

    public void createDraft(Letter draft) {
        mLettersRepository.createDraft(draft);
    }
}
