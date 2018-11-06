package com.example.android.intouch_android.api;

import android.arch.lifecycle.LiveData;

import com.example.android.intouch_android.model.Inmate;
import com.example.android.intouch_android.model.Letter;
import com.example.android.intouch_android.model.container.ApiResponse;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

import java.util.List;
import java.util.Map;

public interface Webservice {
    //TODO: Route should take the username as a query param to filter out letters
    @GET("/johnamadeo/intouch-fake-server/letters")
    LiveData<ApiResponse<List<Letter>>> getLetters();

    @GET("/johnamadeo/intouch-fake-server/inmates")
    LiveData<ApiResponse<List<Inmate>>> getInmatesByName(
            @QueryMap Map<String, String> options
    );

    @POST("/johnamadeo/intouch-fake-server/letters")
    Single<Response<Object>> createLetter(@Body Letter draft, @HeaderMap Map<String, String> headers);
}