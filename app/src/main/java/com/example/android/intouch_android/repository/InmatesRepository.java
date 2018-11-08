package com.example.android.intouch_android.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Transformations;
import android.content.Context;
import android.util.Log;

import com.example.android.intouch_android.api.Webservice;
import com.example.android.intouch_android.model.Correspondence;
import com.example.android.intouch_android.model.Inmate;
import com.example.android.intouch_android.utils.AppExecutors;
import com.example.android.intouch_android.api.WebserviceProvider;
import com.example.android.intouch_android.database.LocalDatabase;
import com.example.android.intouch_android.model.container.ApiResponse;
import com.example.android.intouch_android.model.container.Resource;
import com.example.android.intouch_android.utils.AppState;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class InmatesRepository {
    private final String LOG_TAG = this.getClass().getSimpleName();
    private final Webservice mWebservice;
    private final LocalDatabase mDB;
    private final AppExecutors mExecutors;
    private final AppState mAppState;

    /* ************************************************************ */
    /*                      Public Functions                        */
    /* ************************************************************ */

    public InmatesRepository(Context context) {
        mWebservice = WebserviceProvider.getInstance();
        mDB = LocalDatabase.getInstance(context);
        mExecutors = AppExecutors.getInstance();
        mAppState = AppState.getInstance();
    }

    /*
     * Fetch inmates with names matching the search query from the API, filter out inmates who
     * are already top past correspondences (since they already appear in the "recent" list in
     * the UI) and return the inmates.
     */
    public LiveData<Resource<List<Inmate>>> getInmatesByName(String searchQuery) {
        // If no search query, show a list of all inmates that the user has sent a letter to
        if (searchQuery.equals("")) {
            return getPastInmateCorrespondents();
        }

        MediatorLiveData<Resource<List<Inmate>>> result = new MediatorLiveData<>();
        result.setValue(Resource.loading(null));

        HashMap<String, String> queryParameters = new HashMap<String, String>();
        queryParameters.put("q", searchQuery);

        LiveData<ApiResponse<List<Inmate>>> apiInmates =
                mWebservice.getInmatesByName(queryParameters);
        result.addSource(apiInmates, response -> {
            result.removeSource(apiInmates);

            if (response != null && response.isSuccessful()) {
                List<Inmate> apiInmatesWithMetadata = response.body;

                result.setValue(Resource.success(apiInmatesWithMetadata));
            } else {
                result.setValue(Resource.error(response.errorMessage, null));
            }
        });

        return result;
    }

    /* ************************************************************ */
    /*                       Test Functions                         */
    /* ************************************************************ */

    public void createInmates_TEST() {
        mExecutors.diskIO().execute(() -> {
            boolean isDraft = false;
            mDB.beginTransaction();
            try {
//                for(Integer i = 100 ; i < 104; i++) {
//                    mDB.inmateDao().insertInmate_TEST(
//                            new Inmate(
//                                    i.toString(),
//                                    "John",
//                                    i.toString(),
//                                    "AAAA" + i.toString(),
//                                    new Date(),
//                                    "CT Prison 1"
//                            )
//                    );
//                }
//                mDB.inmateDao().insertInmate_TEST(
//                        new Inmate(
//                                "99",
//                                "Johannes Hutama Cahya Trisna Triawan Putra",
//                                "Kusuma",
//                                "AAAAA99",
//                                new Date(),
//                                "CT Prison Long Longer Longest"
//                        )
//                );
                mDB.setTransactionSuccessful();
            }
            catch(Exception e) {
                Log.d(LOG_TAG, e.toString());
            }
            finally {
                Log.d(LOG_TAG, "Inserted inmates");
                mDB.endTransaction();
            }
        });
    }

    public void createCorrespondences_TEST() {
        mExecutors.diskIO().execute(() -> {
            mDB.beginTransaction();
            try {
//                for(Integer i = 100 ; i < 104; i++) {
//                    Correspondence cor = new Correspondence(
//                            i.toString(),
//                            mAppState.getUsername()
//                    );
//                    cor.setOccurrences(i - 99);
//                    mDB.inmateDao().insertCorrespondence_TEST(cor);
//                }
                Correspondence cor = new Correspondence(
                        "99",
                        mAppState.getUsername()
                );
                cor.setOccurrences(5);
                mDB.setTransactionSuccessful();
            }
            catch(Exception e) {
                Log.d(LOG_TAG, e.toString());
            }
            finally {
                Log.d(LOG_TAG, "Inserted correspondences");
                mDB.endTransaction();
            }
        });
    }

    public void deleteInmates_TEST() {
        mExecutors.diskIO().execute(() -> {
            mDB.inmateDao().deleteInmates_TEST();
            Log.d(LOG_TAG, "Deleted all inmates");
        });
    }

    public void deleteCorrespondences_TEST() {
        mExecutors.diskIO().execute(() -> {
            mDB.inmateDao().deleteCorrespondences_TEST();
            Log.d(LOG_TAG, "Deleted all correspondences");
        });
    }

    public LocalDatabase getDatabase_DANGEROUS() { return mDB; }

    /* ************************************************************ */
    /*                      Private Functions                       */
    /* ************************************************************ */

    private LiveData<Resource<List<Inmate>>> getPastInmateCorrespondents() {
        return Transformations.map(
                mDB.inmateDao().getPastInmateCorrespondents(mAppState.getUsername()),
                inmates -> Resource.success(inmates)
        );
    }
}
