package com.example.android.intouch_android.utils;

import com.example.android.intouch_android.model.User;

public class AppState {
    public static final Object LOCK = new Object();
    private static AppState sInstance;

    // TODO: Remove hardcoded username
    private User user;

    public static AppState getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new AppState();
            }
        }
        return sInstance;
    }

    public void setUser(User user) { this.user = user; }
    public User getUser() {
        return this.user;
    }
    public String getUsername() { return this.user.getUsername(); }
}