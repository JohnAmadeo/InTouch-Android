package com.example.android.intouch_android.api;

import android.arch.lifecycle.LiveData;

import com.example.android.intouch_android.model.Letter;
import com.example.android.intouch_android.model.container.ApiResponse;

import retrofit2.http.GET;
import java.util.List;

public interface Webservice {
    // TODO: Remove mock API once backend API is implemented
    @GET("/JohnAmadeo/intouch-fake-server/letters")
    LiveData<ApiResponse<List<Letter>>> getLetters();
}