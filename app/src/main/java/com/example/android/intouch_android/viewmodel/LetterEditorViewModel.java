package com.example.android.intouch_android.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Pair;

import com.example.android.intouch_android.model.Letter;
import com.example.android.intouch_android.model.container.Resource;
import com.example.android.intouch_android.repository.LettersRepository;
import com.example.android.intouch_android.utils.Transforms;

public class LetterEditorViewModel extends AndroidViewModel {
    private final String LOG_TAG = this.getClass().getSimpleName();
    private LettersRepository mLettersRepository;
    private MutableLiveData<String> mLoadRequest = new MutableLiveData<>();
    private MutableLiveData<String> mSubjectInput = new MutableLiveData<>();
    private MutableLiveData<String> mTextInput = new MutableLiveData<>();
    private MediatorLiveData<Letter> mDraft;
    private LiveData<Letter> mInitialDraft;

    private Letter mCurrentDraft = null;
    private Observer<Letter> mCurrentDraftObserver;

    public LetterEditorViewModel(@NonNull Application application) {
        super(application);
        mLettersRepository = new LettersRepository(application.getApplicationContext());
        mDraft = new MediatorLiveData<>();

        mInitialDraft = Transforms.startWith(
                // loadRequest -> fetch stream of draft
                Transformations.switchMap(
                        mLoadRequest,
                        letterId -> {
                            if (letterId == null) {
                                return null;
                            }
                            else {
                                return Transforms.stripResource(
                                        mLettersRepository.getDraft(letterId)
                                );
                            }
                        }
                ),
                // Start with an empty letter
                Letter.createEmptyLetter()
        );

        // Initial load
        mDraft.addSource(mInitialDraft, draft -> {
            mDraft.setValue(draft);
        });

        // On change of subject
        mDraft.addSource(mSubjectInput, subject -> {
            Letter draft = mDraft.getValue();
            if (draft != null) {
                draft.setSubject(subject);
                mDraft.setValue(draft);
            }
        });

        // On change of text
        mDraft.addSource(mTextInput, text -> {
            Letter draft = mDraft.getValue();
            if (draft != null) {
                draft.setText(text);
                mDraft.setValue(draft);
            }
        });

        mCurrentDraftObserver = draft -> { mCurrentDraft = draft; };
        mDraft.observeForever(mCurrentDraftObserver);
    }

    @Override
    protected void onCleared() {
        mDraft.removeObserver(mCurrentDraftObserver);
    }

    public void saveDraft(Letter draft) {
        mLettersRepository.saveDraft(draft);
    }

    public void setLoadRequest(String letterId) { mLoadRequest.setValue(letterId); }

    public Letter getDraft() { return mCurrentDraft; }
    public LiveData<Letter> getInitialDraft() { return mInitialDraft; }

    public void setDraftSubject(String subject) { mSubjectInput.setValue(subject); }
    public void setDraftText(String text) { mTextInput.setValue(text); }

}
