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

    User(String username, String email, String accessToken) {
        this.username = username;
        this.email = email;
        this.accessToken = accessToken;
    }

    public String getUsername() { return this.username; }
    public String getEmail() { return this.email; }
}
