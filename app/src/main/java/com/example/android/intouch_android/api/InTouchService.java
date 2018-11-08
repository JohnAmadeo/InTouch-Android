package com.example.android.intouch_android.api;

import com.example.android.intouch_android.model.User;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface InTouchService {
    @Headers("Content-Type: application/json")
    @POST("/createuser")
    Single<Response<Object>> createUser(@Body User user);
}
