package com.example.android.intouch_android.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
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

    // Only set for temporary users
    @SerializedName("placeholderPassword")
    private String placeholderPassword = null;

    @Ignore
    public static final String PLACEHOLDER_EMAIL_DOMAIN = "@intouch.placeholder.com";

    public static User createPlaceholderUser() {
        String id = UUID.randomUUID().toString().substring(0, 15);
        return new User(
                id,
                id + PLACEHOLDER_EMAIL_DOMAIN,
                null,
                null,
                null,
                // temporary password
                UUID.randomUUID().toString()
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

    @Ignore
    public User(
            String username,
            String email,
            String accessToken,
            String idToken,
            String refreshToken,
            String placeholderPassword
    ) {
        this.username = username;
        this.email = email;
        this.accessToken = accessToken;
        this.idToken = idToken;
        this.refreshToken = refreshToken;
        this.placeholderPassword = placeholderPassword;
    }

    public String getUsername() { return this.username; }
    public String getEmail() { return this.email; }
    public String getAccessToken() { return this.accessToken; }
    public String getIdToken() { return this.idToken; }
    public String getRefreshToken() { return this.refreshToken; }
    public String getPlaceholderPassword() { return this.placeholderPassword; }

    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
    public void setIdToken(String idToken) { this.idToken = idToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
    public void setPlaceholderPassword(String placeholderPassword) {
        this.placeholderPassword = placeholderPassword;
    }

    @Override
    public String toString() {
        return "\nUser=(\n" +
                "username=" + getUsername() + "\n" +
                "email=" + getEmail() + "\n" +
                "accessToken=" + getAccessToken() + "\n" +
                "idToken=" + getIdToken() + "\n" +
                "refreshToken=" + getRefreshToken() + "\n" +
                "placeholderPassword=" + getPlaceholderPassword() + "\n" +
                ")";
    }

    // A temporary user is a user that does not exist on the server
    public boolean isTemporaryUser() {
        return this.getAccessToken() == null &&
                this.getIdToken() == null &&
                this.getRefreshToken() == null;
    }

    // A placeholder user is a user whose username and email has been auto-generated (this occurs
    // when users opt to get started w/o explicitly signing up or logging in)
    public boolean isPlaceholderUser() {
        return this.getEmail().contains(PLACEHOLDER_EMAIL_DOMAIN);
    }
}
