package com.example.android.intouch_android.model.container;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import com.example.android.intouch_android.AppExecutors;
import com.example.android.intouch_android.model.Letter;
import com.example.android.intouch_android.utils.ModelUtils;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * A generic class that can provide a resource backed by both the sqlite database and the network.
 * <p>
 * You can read more about it in the <a href="https://developer.android.com/arch">Architecture
 * Guide</a>.
 * @param <ResultType>
 * @param <RequestType>
 */
public abstract class NetworkBoundResource<ResultType, RequestType> {
    private final String LOG_TAG = "NetworkBoundResource";
    private final AppExecutors appExecutors;

    private final MediatorLiveData<Resource<ResultType>> result = new MediatorLiveData<>();

    @MainThread
    public NetworkBoundResource(AppExecutors appExecutors) {
        Log.d(LOG_TAG, "Init");

        this.appExecutors = appExecutors;

        result.setValue(Resource.loading(null));

        // The pattern of calling addSource() and then removeSource() in the callback essentially
        // uses LiveData as a one-shot data fetch; i.e as a Javascript promise. Perhaps using
        // Future or CompletableFuture would be a better way to model the data since neither a
        // DB fetch nor a API call is a "stream"?
        LiveData<ResultType> dbResult = loadFromDb();
        result.addSource(dbResult, dbData -> {
            result.removeSource(dbResult);

            List<Letter> letters = (List<Letter>) dbData;
            Log.d(LOG_TAG, "Pre-Cache Fetch=" + toIds(letters));

            if (shouldFetchFromNetwork(dbData)) {
                callApiAndSetValue(dbData);
            } else {
                setValue(Resource.success(dbData));
            }

        });
    }

    public LiveData<Resource<ResultType>> asLiveData() { return result; }

    /* ************************************************************ */
    /*                      Helper Functions                        */
    /* ************************************************************ */

    private void callApiAndSetValue(ResultType dbData) {
        setValue(Resource.loading(dbData));

        LiveData<ApiResponse<RequestType>> apiResult = loadFromApi();
        result.addSource(apiResult, response -> {
            result.removeSource(apiResult);

            if (response != null && response.isSuccessful()) {
                cacheAndSetValue(response);
            } else {
                onFetchFailed();
                setValue(Resource.error(response.errorMessage, dbData));
            }
        });
    }

    /*
     * Cache the response given from the API in the local database and then set the value
     * using the updated data from the local database. This approach allows us to delegate
     * our strategy for merging/reconciling network and local data using the database.
     */
    private void cacheAndSetValue(ApiResponse<RequestType> response) {
        appExecutors.diskIO().execute(() -> {
            saveApiResult(processApiResult(response));

            appExecutors.mainThread().execute(() -> {
                LiveData<ResultType> dbResult = loadFromDb();

                result.addSource(dbResult, dbData -> {
                    result.removeSource(dbResult);
                    if (dbData != null) {
                        List<Letter> letters = (List<Letter>) dbData;
                        Log.d(LOG_TAG, "Post-Cache Fetch=" + toIds(letters));
                    }
                    setValue(Resource.success(dbData));
                });
            });
        });
    }

    @MainThread
    private void setValue(Resource<ResultType> newValue) {
        if (!ModelUtils.equals(result.getValue(), newValue)) {
            result.setValue(newValue);
        }
    }

    private List<String> toIds(List<Letter> letters) {
        List<String> ids = new ArrayList<>();
        for (Letter letter:letters) {
            ids.add(letter.getId());
        }
        return ids;
    }

    /* ************************************************************ */
    /*                  Implementable Functions                     */
    /* ************************************************************ */

    protected void onFetchFailed() { }

    @NonNull
    @MainThread
    protected abstract LiveData<ResultType> loadFromDb();

    @NonNull
    @MainThread
    protected abstract LiveData<ApiResponse<RequestType>> loadFromApi();

    @WorkerThread
    protected RequestType processApiResult(ApiResponse<RequestType> response) {
        return response.body;
    }

    @WorkerThread
    protected abstract void saveApiResult(@NonNull RequestType item);

    @MainThread
    protected abstract boolean shouldFetchFromNetwork(@Nullable ResultType data);
}
