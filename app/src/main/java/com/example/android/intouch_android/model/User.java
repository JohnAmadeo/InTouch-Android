package com.example.android.intouch_android.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

@Entity(tableName = "users")
public class User {
    @PrimaryKey
    @NonNull
    @SerializedName("username")
    private String username;

    @SerializedName("email")
    private String email;

    @SerializedName("accessToken")
    private String accessToken;

    @SerializedName("idToken")
    private String idToken;

    @SerializedName("refreshToken")
    private String refreshToken;

    public static User createTemporaryUser() {
        String id = UUID.randomUUID().toString();
        return new User(
                id + "-user",
                id + "@intouch.com",
                null,
                null,
                null
        );
    }

    public User(
            String username,
            String email,
            String accessToken,
            String idToken,
            String refreshToken
    ) {
        this.username = username;
        this.email = email;
        this.accessToken = accessToken;
        this.idToken = idToken;
        this.refreshToken = refreshToken;
    }

    public String getUsername() { return this.username; }
    public String getEmail() { return this.email; }
    public String getAccessToken() { return this.accessToken; }
    public String getIdToken() { return this.idToken; }
    public String getRefreshToken() { return this.refreshToken; }

    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
    public void setIdToken(String idToken) { this.idToken = idToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
}
