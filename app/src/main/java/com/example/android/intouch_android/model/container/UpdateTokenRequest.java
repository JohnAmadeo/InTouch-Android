package com.example.android.intouch_android.model.container;

import com.google.gson.annotations.SerializedName;

public class UpdateTokenRequest {
    @SerializedName("grant_type")
    private String grantType;

    @SerializedName("client_id")
    private String clientId;

    @SerializedName("refresh_token")
    private String refreshToken;

    public UpdateTokenRequest(String grantType, String clientId, String refreshToken) {
        this.grantType = grantType;
        this.clientId = clientId;
        this.refreshToken = refreshToken;
    }
}
