package com.example.android.intouch_android.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import com.example.android.intouch_android.model.Letter;
import com.example.android.intouch_android.model.container.Resource;
import com.example.android.intouch_android.repository.LettersRepository;

public class LetterEditorViewModel extends AndroidViewModel {
    private LettersRepository mLettersRepository;
    private MutableLiveData<String> mLoadRequest = new MutableLiveData<>();
    private LiveData<Resource<Letter>> mDraft;

    public LetterEditorViewModel(@NonNull Application application) {
        super(application);
        mLettersRepository = new LettersRepository(application.getApplicationContext());
        mDraft = Transformations.switchMap(mLoadRequest,
                letterId -> mLettersRepository.getDraft(letterId)
        );
    }

    public void saveDraft(Letter draft) {
        mLettersRepository.saveDraft(draft);
    }

    public void setLoadRequest(String letterId) { mLoadRequest.setValue(letterId); }

    public LiveData<Resource<Letter>> getDraft() { return mDraft; }
}
