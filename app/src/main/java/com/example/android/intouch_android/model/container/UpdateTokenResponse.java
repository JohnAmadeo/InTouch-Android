package com.example.android.intouch_android.model.container;

import com.google.gson.annotations.SerializedName;

public class UpdateTokenResponse {
    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("id_token")
    private String idToken;

    @SerializedName("expires_in")
    private Integer expiresIn;

    @SerializedName("scope")
    private String scope;

    @SerializedName("token_type")
    private String tokenType;

    public UpdateTokenResponse(
            String accessToken,
            String idToken,
            Integer expiresIn,
            String scope,
            String tokenType
    ) {
        this.accessToken = accessToken;
        this.idToken = idToken;
        this.expiresIn = expiresIn;

        this.scope = scope;
        this.tokenType = tokenType;
    }

    public String getAccessToken() { return accessToken; }
    public Integer getExpiresIn() { return expiresIn; }
    public String getIdToken() { return idToken; }
    public String getScope() { return scope; }
    public String getTokenType() { return tokenType; }

    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
    public void setExpiresIn(Integer expiresIn) { this.expiresIn = expiresIn; }
    public void setIdToken(String idToken) { this.idToken = idToken; }
    public void setScope(String scope) { this.scope = scope; }
    public void setTokenType(String tokenType) { this.tokenType = tokenType; }

    @Override
    public String toString() {
        return "UpdateTokenResponse(\n" +
                "accessToken=" + getAccessToken() + "\n" +
                "idToken=" + getIdToken() + "\n" +
                "expiresIn=" + getExpiresIn() + "\n" +
                "scope=" + getScope() + "\n" +
                "tokenType=" + getTokenType();
    }
}
