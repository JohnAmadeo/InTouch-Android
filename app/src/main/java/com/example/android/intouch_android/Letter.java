package com.example.android.intouch_android;

import com.google.gson.annotations.SerializedName;

/**
 * A dummy item representing a piece of content.
 */
public class Letter {
    // TODO: Make the class variables private
    @SerializedName("id")
    private String id;

    @SerializedName("recipient")
    private String recipient;

    @SerializedName("subject")
    private String subject;

    @SerializedName("text")
    private String text;

    @SerializedName("timeSent")
    private String timeSent;

    public Letter(String id, String recipient, String subject, String text, String timeSent) {
        this.id = id;
        this.recipient = recipient;
        this.subject = subject;
        this.text = text;
        this.timeSent = timeSent;
    }

    @Override
    public String toString() {
        return this.text;
    }

}