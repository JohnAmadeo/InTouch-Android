package com.example.android.intouch_android.utils;

import com.example.android.intouch_android.model.User;

public class AppState {
    public static final Object LOCK = new Object();
    private static AppState sInstance;

    private static User user = null;

    public static AppState getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new AppState();
            }
        }
        return sInstance;
    }

    public User getUser() { return user; }
    public String getUsername() { return user.getUsername(); }

    public void setUser(User loggedInUser) { user = loggedInUser; }
    public void setUserAccessToken(String accessToken) { user.setAccessToken(accessToken); }
    public void setUserIdToken(String idToken) { user.setIdToken(idToken); }
    public void setUserRefreshToken(String refreshToken) { user.setRefreshToken(refreshToken); }
}