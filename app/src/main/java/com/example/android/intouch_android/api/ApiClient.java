package com.example.android.intouch_android.api;

import com.example.android.intouch_android.model.Letter;
import com.google.gson.GsonBuilder;

import android.util.Log;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final Object LOCK = new Object();
    private static Webservice sInstance;

    public static Webservice getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d("ApiClient", "Creating new API client instance");
                sInstance = new Retrofit.Builder()
                        .baseUrl("https://my-json-server.typicode.com")
                        .addConverterFactory(
                                GsonConverterFactory.create(new GsonBuilder()
                                        .setDateFormat(Letter.dateFormat)
                                        .create()
                                )
                        )
                        .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                        .build()
                        .create(Webservice.class);
            }
        }
        Log.d("ApiClient", "Getting the API client instance");
        return sInstance;
    }
}