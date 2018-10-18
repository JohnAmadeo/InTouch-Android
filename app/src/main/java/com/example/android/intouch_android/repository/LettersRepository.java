package com.example.android.intouch_android.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;

import com.example.android.intouch_android.api.Webservice;
import com.example.android.intouch_android.database.LocalDatabase;
import com.example.android.intouch_android.model.Letter;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LettersRepository {
    private Webservice mWebservice;
    private LocalDatabase mCache;

    private MutableLiveData<List<Letter>> mLetters;

    /* ************************************************************ */
    /*                      Public Functions                        */
    /* ************************************************************ */

    public LettersRepository(Context context) {
        mWebservice = new Retrofit.Builder()
                .baseUrl("https://my-json-server.typicode.com")
                .addConverterFactory(
                        GsonConverterFactory.create(new GsonBuilder()
                            .setDateFormat(Letter.dateFormat)
                            .create()
                        )
                )
                .build()
                .create(Webservice.class);

        mCache = LocalDatabase.getInstance(context);

        mLetters = new MutableLiveData<>();
        mLetters.setValue(new ArrayList<Letter>());

        
//        Log.w("Repo", mCache.getOpenHelper().getWritableDatabase().getPath());
    }

    public LiveData<List<Letter>> getLettersStream() { return mLetters; }

    public void refreshLetters() {
        // Cache
        mCache.letterDao().getLetters().observeForever(cacheLetters -> mergeLetters(cacheLetters));

        // Remote
        mWebservice.getLetters().enqueue(new Callback<List<Letter>>() {
            @Override
            public void onResponse(Call<List<Letter>> call, Response<List<Letter>> response) {
                Log.w("Repo success", response.toString());
                List<Letter> serverLetters = response.body();
                mergeLetters(serverLetters);
                // TODO: If there is a new entry, update cache
            }

            @Override
            public void onFailure(Call<List<Letter>> call, Throwable t) {
                Log.w("Repo failure", t.toString());
            }
        });
    }

    /* ************************************************************ */
    /*                      Private Functions                       */
    /* ************************************************************ */

    private void mergeLetters(List<Letter> incomingLetters) {
        List<Letter> currentLetters = mLetters.getValue();
        Set<String> currentLetterIDs = new HashSet<>();
        for (Letter letter:currentLetters) {
            currentLetterIDs.add(letter.getId());
        }

        for (Letter letter:incomingLetters) {
            if (!currentLetterIDs.contains(letter.getId())) {
                currentLetters.add(letter);
            }
        }

        mLetters.postValue(currentLetters);
    }
}
