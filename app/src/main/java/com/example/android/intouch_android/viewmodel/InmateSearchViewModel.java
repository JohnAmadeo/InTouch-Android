package com.example.android.intouch_android.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.android.intouch_android.repository.LettersRepository;
import com.example.android.intouch_android.utils.LiveDataTransformers;

public class InmateSearchViewModel extends AndroidViewModel {

    private LettersRepository mLettersRepository;

    private MutableLiveData<String> mSearchQuery = new MutableLiveData<>();
    private LiveData<String> mDebouncedSearchQuery = new MediatorLiveData<>();

    public InmateSearchViewModel(@NonNull Application application) {
        super(application);
//        mLettersRepository = new LettersRepository(application.getApplicationContext());

        mDebouncedSearchQuery = LiveDataTransformers.debounce(mSearchQuery, 1000);
    }

    // TODO: Change to mInmates
//    public LiveData<List<Letter>> getDisplayedLetters() { return mDisplayedLetters; }

    public void setQuery(String searchQuery) { mSearchQuery.setValue(searchQuery); }

    /* ************************************************************ */
    /*                     Private Functions                        */
    /* ************************************************************ */


    /* ************************************************************ */
    /*                       Test Functions                         */
    /* ************************************************************ */

    public LiveData<String> getDebouncedQuery_TEST() { return mDebouncedSearchQuery; }

}
