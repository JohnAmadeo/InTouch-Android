package com.example.android.intouch_android.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import com.example.android.intouch_android.model.Inmate;
import com.example.android.intouch_android.model.InmateWithSearchMetadata;
import com.example.android.intouch_android.model.container.Resource;
import com.example.android.intouch_android.repository.InmatesRepository;
import com.example.android.intouch_android.utils.Transforms;

import java.util.List;

public class InmateSearchViewModel extends AndroidViewModel {

    private InmatesRepository mInmatesRepository;

    private MutableLiveData<String> mSearchQuery = new MutableLiveData<>();
    private LiveData<String> mDebouncedSearchQuery;
    private LiveData<Resource<List<InmateWithSearchMetadata>>> mInmates;

    public InmateSearchViewModel(@NonNull Application application) {
        super(application);
        mInmatesRepository = new InmatesRepository(application.getApplicationContext());
        mDebouncedSearchQuery = Transforms.debounce(mSearchQuery, 1000);

        mInmates = Transformations.switchMap(mDebouncedSearchQuery,
                searchQuery -> mInmatesRepository.getInmates(searchQuery)
        );
    }

    public LiveData<Resource<List<InmateWithSearchMetadata>>> getInmates() { return mInmates; }

    public void setQuery(String searchQuery) { mSearchQuery.setValue(searchQuery); }

    /* ************************************************************ */
    /*                     Private Functions                        */
    /* ************************************************************ */


    /* ************************************************************ */
    /*                       Test Functions                         */
    /* ************************************************************ */

    public LiveData<String> getDebouncedQuery_TEST() { return mDebouncedSearchQuery; }

    public void createCorrespondences_TEST() {
        mInmatesRepository.createInmates_TEST();
        mInmatesRepository.createCorrespondences_TEST();
    }

    public void deleteCorrespondences_TEST() {
        mInmatesRepository.deleteInmates_TEST();
        mInmatesRepository.deleteCorrespondences_TEST();
    }
}
