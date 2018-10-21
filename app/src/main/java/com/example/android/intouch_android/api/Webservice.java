package com.example.android.intouch_android.api;

import android.arch.lifecycle.LiveData;

import com.example.android.intouch_android.model.Inmate;
import com.example.android.intouch_android.model.Letter;
import com.example.android.intouch_android.model.container.ApiResponse;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

import java.util.List;
import java.util.Map;

public interface Webservice {
    // TODO: Remove mock API once backend API is implemented
    @GET("/{username}/intouch-fake-server/letters")
    LiveData<ApiResponse<List<Letter>>> getLetters(@Path("username") String username);

    @GET("/{username}/intouch-fake-server/inmates")
    LiveData<ApiResponse<List<Inmate>>> getInmatesByName(
            @Path("username") String username,
            @QueryMap Map<String, String> options
    );
}