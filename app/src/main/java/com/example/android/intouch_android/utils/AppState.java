package com.example.android.intouch_android.utils;

public class AppState {
    public static final Object LOCK = new Object();
    private static AppState sInstance;

    // TODO: Remove hardcoded username
    private String username = "JohnAmadeo";

    public static AppState getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new AppState();
            }
        }
        return sInstance;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) { this.username = username; }
}