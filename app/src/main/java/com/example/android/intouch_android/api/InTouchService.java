package com.example.android.intouch_android.api;

import android.arch.lifecycle.LiveData;

import com.example.android.intouch_android.model.Inmate;
import com.example.android.intouch_android.model.Letter;
import com.example.android.intouch_android.model.User;
import com.example.android.intouch_android.model.container.ApiResponse;

import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface InTouchService {
    @Headers("Content-Type: application/json")
    @POST("/createuser")
    Single<Response<Object>> createUser(@Body User user);

    @Headers("Content-Type: application/json")
    @GET("/letters")
    LiveData<ApiResponse<List<Letter>>> getLetters(
            @Query("username") String username,
            @Header("Authorization") String authentication
    );

    @Headers("Content-Type: application/json")
    @GET("/inmates")
    LiveData<ApiResponse<List<Inmate>>> getInmatesByName(
            @Query("query") String searchQuery,
            @Header("Authorization") String authentication
    );

    @Headers("Content-Type: application/json")
    @POST("/letter")
    Single<Response<Letter>> createLetter(
            @Body Letter draft,
            @Header("Authorization") String authentication
    );
}
