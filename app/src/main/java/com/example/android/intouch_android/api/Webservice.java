package com.example.android.intouch_android.api;

import com.example.android.intouch_android.model.Letter;

import retrofit2.http.GET;
import retrofit2.Call;
import java.util.List;

public interface Webservice {
    // TODO: Remove mock API once backend API is implemented
    @GET("/JohnAmadeo/intouch-fake-server/letters")
    Call<List<Letter>> getLetters();
}