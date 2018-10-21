package com.example.android.intouch_android.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.intouch_android.model.Correspondence;
import com.example.android.intouch_android.model.Inmate;
import com.example.android.intouch_android.model.InmateWithSearchMetadata;
import com.example.android.intouch_android.utils.AppExecutors;
import com.example.android.intouch_android.api.WebserviceProvider;
import com.example.android.intouch_android.api.Webservice;
import com.example.android.intouch_android.database.LocalDatabase;
import com.example.android.intouch_android.model.container.ApiResponse;
import com.example.android.intouch_android.model.container.NetworkBoundResource;
import com.example.android.intouch_android.model.container.Resource;
import com.example.android.intouch_android.utils.AppState;
import com.example.android.intouch_android.utils.Transforms;

import java.util.ArrayList;
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

    public LiveData<Resource<List<InmateWithSearchMetadata>>> getInmates(String searchQuery) {
        return new NetworkBoundResource<
                List<InmateWithSearchMetadata>,
                List<Inmate>
            >(mExecutors) {
            @NonNull
            @Override
            protected LiveData<List<InmateWithSearchMetadata>> loadFromDb() {
                return Transformations.map(
                        mDB.inmateDao().getCorrespondences(mAppState.getUsername()),
                        inmates -> mapOnSearchMetadata(inmates, true)
                );
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<Inmate>>> loadFromApi() {
                HashMap queryParameters = new HashMap<String, String>();
                queryParameters.put("search", searchQuery);
                return mWebservice.getInmatesByName(mAppState.getUsername(), queryParameters);
            }

            @NonNull
            @Override
            protected void mergeAndSetValue(
                    List<Inmate> apiInmates,
                    List<InmateWithSearchMetadata> dbInmateCorrespondencesWithMetadata
            ) {
                List<Inmate> dbInmateCorrespondences = Transforms.map(
                        dbInmateCorrespondencesWithMetadata,
                        inmateWithMetadata -> inmateWithMetadata.getInmate()
                );

                List<InmateWithSearchMetadata> apiInmatesWithMetadata = mapOnSearchMetadata(
                        Transforms.filter(apiInmates,
                                inmate -> !dbInmateCorrespondences.contains(inmate)
                        ),
                        false
                );

                setValue(Resource.success(
                        Transforms.merge(
                                apiInmatesWithMetadata,
                                dbInmateCorrespondencesWithMetadata
                        )
                ));
            }

            @Override
            protected boolean shouldFetchFromNetwork(@Nullable List<InmateWithSearchMetadata> __) {
                // TODO: Check if the internet connection is down
                return true;
            }

        }.asLiveData();
    }

    /* ************************************************************ */
    /*                       Test Functions                         */
    /* ************************************************************ */

    public void createInmates_TEST() {
        mExecutors.diskIO().execute(() -> {
            boolean isDraft = false;
            mDB.beginTransaction();
            try {
                for(Integer i = 100 ; i < 103; i++) {
                    mDB.inmateDao().insertInmate_TEST(
                            new Inmate(
                                    i.toString(),
                                    "John",
                                    i.toString(),
                                    "AAAA" + i.toString(),
                                    new Date(),
                                    "CT Prison 1"
                            )
                    );
                }
                mDB.setTransactionSuccessful();
            } finally {
                Log.d(LOG_TAG, "Inserted inmates");
                mDB.endTransaction();
            }
        });
    }

    public void createCorrespondences_TEST() {
        mExecutors.diskIO().execute(() -> {
            mDB.beginTransaction();
            try {
                for(Integer i = 100 ; i < 103; i++) {
                    mDB.inmateDao().insertCorrespondence_TEST(new Correspondence(
                        i.toString(),
                        mAppState.getUsername()
                    ));
                }
                mDB.setTransactionSuccessful();
            } finally {
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
    private List<InmateWithSearchMetadata> mapOnSearchMetadata(
            List<Inmate> inmates,
            boolean isPastCorrespondent
    ) {
        List<InmateWithSearchMetadata> inmatesWithSearchMetadata = new ArrayList<>();
        for (Inmate inmate:inmates) {
            inmatesWithSearchMetadata.add(new InmateWithSearchMetadata(
                            inmate,
                            isPastCorrespondent
                    )
            );
        }
        return inmatesWithSearchMetadata;
    }
}
