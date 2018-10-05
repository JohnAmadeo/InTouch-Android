package com.example.android.intouch_android;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Letter {
    @NonNull
    @SerializedName("id")
    private String id;

    @SerializedName("recipient")
    private String recipient;

    @SerializedName("subject")
    private String subject;

    @SerializedName("text")
    private String text;

    @SerializedName("timeSent")
    private Date timeSent;

    @SerializedName("isDraft")
    private boolean isDraft;

    public static final String dateFormat = "MM/dd/yy";

    public Letter(
            String id,
            String recipient,
            String subject,
            String text,
            Date timeSent,
            boolean isDraft
    ) {
        this.id = id;
        this.recipient = recipient;
        this.subject = subject;
        this.text = text;
        this.timeSent = timeSent;
        this.isDraft = isDraft;
    }

    @Override
    public String toString() {
        return this.text;
    }

    public String getId() { return this.id; }
    public String getRecipient() { return this.recipient; }
    public String getSubject() { return this.subject; }
    public String getText() { return this.text; }

    public Date getTimeSent() { return this.timeSent; }
    public String getTimeSentString() { return dateToString(this.timeSent); }

    public boolean isDraft() { return this.isDraft; }

    public boolean equals(Letter letter) {
        return getId().equals(letter.getId()) &&
                getRecipient().equals(letter.getRecipient()) &&
                getSubject().equals(letter.getSubject()) &&
                getText().equals(letter.getText()) &&
                getTimeSent().equals(letter.getTimeSent()) &&
                isDraft() == letter.isDraft();
    }

    public boolean contains(String searchQuery) {
        searchQuery = searchQuery.toLowerCase();
        return getRecipient().toLowerCase().contains(searchQuery) ||
                getSubject().toLowerCase().contains(searchQuery) ||
                getText().toLowerCase().contains(searchQuery) ||
                getTimeSentString().contains(searchQuery);
    }

    private String dateToString(Date date) {
        DateFormat df = new SimpleDateFormat(dateFormat);
        return df.format(date);
    }
}