package com.example.android.intouch_android.api;

import com.example.android.intouch_android.model.container.UpdateTokenRequest;
import com.example.android.intouch_android.model.container.UpdateTokenResponse;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {
    @POST("/oauth/token")
    Single<Response<UpdateTokenResponse>> getRefreshToken(@Body UpdateTokenRequest body);
}
