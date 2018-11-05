package com.example.android.intouch_android.api;

import com.example.android.intouch_android.model.Letter;
import com.google.gson.GsonBuilder;

import android.util.Log;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WebserviceProvider {
    private static final Object LOCK = new Object();
    private static Webservice sInstance;

    public static Webservice getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
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
        return sInstance;
    }
}