package com.example.android.intouch_android.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.intouch_android.model.Letter;
import com.example.android.intouch_android.model.container.Resource;
import com.example.android.intouch_android.repository.LettersRepository;

import java.util.ArrayList;
import java.util.List;

public class DraftsViewModel extends AndroidViewModel {
    private final String LOG_TAG = this.getClass().getSimpleName();

    private LettersRepository mLettersRepository;
    private MutableLiveData<Boolean> mRefreshRequest = new MutableLiveData<>();
    private MutableLiveData<String> mSearchQuery = new MutableLiveData<>();

    private LiveData<Resource<List<Letter>>> mDrafts;
    private MediatorLiveData<List<Letter>> mDisplayedDrafts = new MediatorLiveData<>();

    public DraftsViewModel(@NonNull Application application) {
        super(application);
        mLettersRepository = new LettersRepository(application.getApplicationContext());

        // "Start" the streams with an initial value. Libraries like RxJava have a .startWith()
        // function that automatically does this. Probably can extend LiveData ourselves to do this
        // but pretty overkill at this stage of the project.
        mRefreshRequest.setValue(true);
        mSearchQuery.setValue("");

        mDrafts = Transformations.switchMap(mRefreshRequest,
                __ -> mLettersRepository.getDrafts()
        );

        mDisplayedDrafts.addSource(mSearchQuery, searchQuery -> {
            mDisplayedDrafts.setValue(filterLetters(mDrafts.getValue(), searchQuery));
        });

        mDisplayedDrafts.addSource(mDrafts, letters -> {

            mDisplayedDrafts.setValue(filterLetters(letters, mSearchQuery.getValue()));
        });
    }

    public LiveData<Resource<List<Letter>>> getDrafts() {
        return mDrafts;
    }
    public LiveData<List<Letter>> getDisplayedDrafts() { return mDisplayedDrafts; }

    public void setRefreshRequest() { mRefreshRequest.setValue(true); }
    public void setQuery(String searchQuery) { mSearchQuery.setValue(searchQuery); }

    /* ************************************************************ */
    /*                     Private Functions                        */
    /* ************************************************************ */

    private List<Letter> filterLetters(
            Resource<List<Letter>> letters,
            String searchQuery
    ) {
        if (letters == null || letters.data == null) {
            return new ArrayList<>();
        }
        if (searchQuery == null) {
            return letters.data;
        }

        List<Letter> filteredLetters = new ArrayList<>();
        for (Letter letter:letters.data) {
            if (letter.contains(searchQuery)) {
                filteredLetters.add(letter);
            }
        }
        return filteredLetters;
    }
}
