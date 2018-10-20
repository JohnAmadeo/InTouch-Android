package com.example.android.intouch_android.repository;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.intouch_android.utils.AppExecutors;
import com.example.android.intouch_android.api.WebserviceProvider;
import com.example.android.intouch_android.api.Webservice;
import com.example.android.intouch_android.database.LocalDatabase;
import com.example.android.intouch_android.model.Letter;
import com.example.android.intouch_android.model.container.ApiResponse;
import com.example.android.intouch_android.model.container.NetworkBoundResource;
import com.example.android.intouch_android.model.container.Resource;

import java.util.List;

public class LettersRepository {
    private final String LOG_TAG = this.getClass().getSimpleName();
    private final Webservice mWebservice;
    private final LocalDatabase mDB;
    private final AppExecutors mExecutors;

//    private MutableLiveData<List<Letter>> mLetters;

    /* ************************************************************ */
    /*                      Public Functions                        */
    /* ************************************************************ */

    public LettersRepository(Context context) {
        mWebservice = WebserviceProvider.getInstance();
        mDB = LocalDatabase.getInstance(context);
        mExecutors = AppExecutors.getInstance();
    }

    public LiveData<Resource<List<Letter>>> getLetters() {
        return new NetworkBoundResource<List<Letter>, List<Letter>>(mExecutors) {
            @NonNull
            @Override
            protected LiveData<List<Letter>> loadFromDb() {
                return mDB.letterDao().getLetters();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<Letter>>> loadFromApi() {
                return mWebservice.getLetters();
            }

            @Override
            protected void saveApiResult(@NonNull List<Letter> letters) {
                Log.d(LOG_TAG, "API=" + letters.size());
                mDB.beginTransaction();
                try {
                    mDB.letterDao().insertLetters(letters);
                    mDB.setTransactionSuccessful();
                } finally {
                    mDB.endTransaction();
                }
            }

            @Override
            protected boolean shouldFetchFromNetwork(@Nullable List<Letter> letters) {
                // CHECK IF INTERNET CONNECTION IS DOWN
                return true;
            }

        }.asLiveData();
    }

    public void createLetter_TEST() {
        mExecutors.diskIO().execute(() -> {
//            boolean isDraft = false;
//            mDB.beginTransaction();
//            try {
//                for(Integer i = 100 ; i < 110; i++) {
//                    mDB.letterDao().insertLetter(
//                            new Letter(
//                                    i.toString(),
//                                    i.toString(),
//                                    i.toString(),
//                                    i.toString(),
//                                    new Date(),
//                                    isDraft
//                            )
//                    );
//                }
//                mDB.setTransactionSuccessful();
//            } finally {
//                Log.d("LettersRepository", "Inserted letter ");
//                mDB.endTransaction();
//            }
        });
    }

    public LocalDatabase getDatabase_DANGEROUS() { return mDB; }
}
