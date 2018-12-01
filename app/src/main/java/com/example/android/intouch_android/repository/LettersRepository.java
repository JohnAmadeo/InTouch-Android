package com.example.android.intouch_android.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.intouch_android.api.InTouchService;
import com.example.android.intouch_android.api.InTouchServiceProvider;
import com.example.android.intouch_android.api.WebserviceProvider;
import com.example.android.intouch_android.model.container.ApiException;
import com.example.android.intouch_android.model.container.ApiExceptionType;
import com.example.android.intouch_android.model.container.HTTPCode;
import com.example.android.intouch_android.model.container.Status;
import com.example.android.intouch_android.utils.AppExecutors;
import com.example.android.intouch_android.api.Webservice;
import com.example.android.intouch_android.database.LocalDatabase;
import com.example.android.intouch_android.model.Letter;
import com.example.android.intouch_android.model.container.ApiResponse;
import com.example.android.intouch_android.model.container.NetworkBoundResource;
import com.example.android.intouch_android.model.container.Resource;
import com.example.android.intouch_android.utils.AppState;
import com.example.android.intouch_android.utils.AuthUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.exceptions.Exceptions;

public class LettersRepository {
    private final String LOG_TAG = this.getClass().getSimpleName();
    private final Webservice mWebservice;
    private final InTouchService mInTouchService;
    private final LocalDatabase mDB;
    private final AppExecutors mExecutors;
    private final AppState mAppState;
    private final Context mContext;

    /* ************************************************************ */
    /*                      Public Functions                        */
    /* ************************************************************ */

    public LettersRepository(Context context) {
        mContext = context;
        mWebservice = WebserviceProvider.getInstance();
        mInTouchService = InTouchServiceProvider.getInstance();
        mDB = LocalDatabase.getInstance(mContext);
        mExecutors = AppExecutors.getInstance();
        mAppState = AppState.getInstance();
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
                return mInTouchService.getLetters(
                        mAppState.getUsername(),
                        AuthUtils.formatAuthHeader(mAppState.getUser().getAccessToken())
                );
            }

            @Override
            protected void saveApiResult(@NonNull List<Letter> letters) {
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

    public LiveData<Resource<Letter>> getDraft(String letterId) {
        MediatorLiveData<Resource<Letter>> result = new MediatorLiveData<>();

        LiveData<Letter> dbResult = mDB.letterDao().getDraft(letterId);
        result.addSource(dbResult, draft -> {
            result.removeSource(dbResult);
            result.setValue(Resource.success(draft));
        });

        return result;
    }

    public LiveData<Resource<List<Letter>>> getDrafts() {
        MediatorLiveData<Resource<List<Letter>>> result = new MediatorLiveData<>();
        result.setValue(Resource.loading(null));

        LiveData<List<Letter>> dbResult = mDB.letterDao().getDrafts();
        result.addSource(dbResult, drafts -> {
            result.removeSource(dbResult);
            result.setValue(Resource.success(drafts));
        });

        return result;
    }

    public Single<Letter> getLetterAsObservable(String letterId) {
        return mDB.letterDao().getLetter(letterId);
    }

    public void saveDraft(Letter draft) {
        draft.setTimeLastEdited(new Date());
        mExecutors.diskIO().execute(() -> mDB.letterDao().insertLetter(draft));
    }

    public void updateDraftRecipient(String letterId, String recipientId, String recipient) {
        mExecutors.diskIO().execute(() -> {
            mDB.letterDao().updateDraftRecipient(letterId, recipientId, recipient);
        });
    }

    public Single<String> createLetter(Letter draft, String accessToken) {
        draft.setIsDraft(false);
        draft.setTimeSent(new Date());

        return mInTouchService.createLetter(draft, AuthUtils.formatAuthHeader(accessToken))
                // TODO: Add timeout operator here?
                .map(response -> {
                    if (response.code() == HTTPCode.CREATED) {
                        Letter letter = response.body();
                        Log.d(LOG_TAG, letter.toString());
                        mExecutors.diskIO().execute(() -> mDB.letterDao().insertLetter(letter));
                        return letter.getId();
                    }
                    else {
                        // Automatically save the letter as a draft if it cannot be saved on the
                        // backend for some reason (e.g loss of network connectivity)
                        draft.setIsDraft(true);
                        draft.setTimeSent(null);
                        mExecutors.diskIO().execute(() -> mDB.letterDao().insertLetter(draft));

                        throw new ApiException(
                                ApiExceptionType.CREATE_LETTER,
                                "Failed to send letter on backend"
                        );
                    }
                })
                .doOnError(error -> {
                    throw new ApiException(
                            ApiExceptionType.CREATE_LETTER,
                            "Network=" + error.getMessage() + " Desc=Failed to send letter on backend"
                    );
                });
    }

    /* ************************************************************ */
    /*                       Test Functions                         */
    /* ************************************************************ */

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

    public void deleteAllLetters_DANGEROUS() {
        mExecutors.diskIO().execute(() -> {
            mDB.letterDao().deleteAllLetters_DANGEROUS();
        });
    }

    public LocalDatabase getDatabase_DANGEROUS() { return mDB; }
}
