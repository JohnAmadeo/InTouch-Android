package com.example.android.intouch_android;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LettersRepository {
    private Webservice mWebservice;
    private MutableLiveData<List<Letter>> mLetters;

    public LettersRepository() {
        mLetters = new MutableLiveData<>();
        mLetters.setValue(new ArrayList<Letter>());
        mWebservice = new Retrofit.Builder()
                .baseUrl("https://my-json-server.typicode.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Webservice.class);
    }

    public LiveData<List<Letter>> getLetters() {
        mWebservice.getLetters().enqueue(new Callback<List<Letter>>() {
            @Override
            public void onResponse(Call<List<Letter>> call, Response<List<Letter>> response) {
                mLetters.setValue(response.body());
            }

            // TODO: Override onFailure and implement API error handling
            @Override
            public void onFailure(Call<List<Letter>> call, Throwable t) {
            }
        });

        return mLetters;
    }
}
