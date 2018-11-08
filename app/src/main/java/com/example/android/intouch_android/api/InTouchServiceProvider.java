package com.example.android.intouch_android.api;

import android.util.Log;

import com.example.android.intouch_android.model.Letter;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class InTouchServiceProvider {
    private static final Object LOCK = new Object();
    private static InTouchService sInstance;

    public static InTouchService getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new Retrofit.Builder()
                        .baseUrl("https://intouch-android-backend.herokuapp.com/")
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(
                                GsonConverterFactory.create(new GsonBuilder()
                                        .setDateFormat(Letter.dateFormat)
                                        .create()
                                )
                        )
                        .build()
                        .create(InTouchService.class);
            }
        }
        return sInstance;
    }
}
