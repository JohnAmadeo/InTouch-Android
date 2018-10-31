package com.example.android.intouch_android.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import com.example.android.intouch_android.model.container.Resource;
import com.example.android.intouch_android.repository.LettersRepository;
import com.example.android.intouch_android.model.Letter;

import java.util.ArrayList;
import java.util.List;

public class SentLettersViewModel extends AndroidViewModel {

    private LettersRepository mLettersRepository;

    private MutableLiveData<Boolean> mRefreshRequest = new MutableLiveData<>();
    private MutableLiveData<String> mSearchQuery = new MutableLiveData<>();

    private LiveData<Resource<List<Letter>>> mLetters;
    private MediatorLiveData<List<Letter>> mDisplayedLetters = new MediatorLiveData<>();

    public SentLettersViewModel(@NonNull Application application) {
        super(application);
        mLettersRepository = new LettersRepository(application.getApplicationContext());

        // "Start" the streams with an initial value. Libraries like RxJava have a .startWith()
        // function that automatically does this. Probably can extend LiveData ourselves to do this
        // but pretty overkill at this stage of the project.
        mRefreshRequest.setValue(true);
        mSearchQuery.setValue("");

        mLetters = Transformations.switchMap(mRefreshRequest,
                __ -> mLettersRepository.getLetters()
        );

        mDisplayedLetters.addSource(mSearchQuery, searchQuery ->
            mDisplayedLetters.setValue(filterLetters(mLetters.getValue(), searchQuery))
        );
        mDisplayedLetters.addSource(mLetters, letters ->
            mDisplayedLetters.setValue(filterLetters(letters, mSearchQuery.getValue()))
        );
    }

    public LiveData<Resource<List<Letter>>> getLetters() {
        return mLetters;
    }
    public LiveData<List<Letter>> getDisplayedLetters() { return mDisplayedLetters; }

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

    /* ************************************************************ */
    /*                       Test Functions                         */
    /* ************************************************************ */

    public void createLetter_TEST() { mLettersRepository.createLetter_TEST(); }
    public LettersRepository getRepo_DANGEROUS() { return mLettersRepository; }
    public void deleteAllLetters_DANGEROUS() { mLettersRepository.deleteAllLetters_DANGEROUS(); }
    public LiveData<Resource<List<Letter>>> getDrafts_TEST() {
        return mLettersRepository.getDrafts();
    }
}
