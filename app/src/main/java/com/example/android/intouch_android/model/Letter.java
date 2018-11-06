package com.example.android.intouch_android.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Entity(tableName = "letters")
public class Letter {
    @PrimaryKey
    @NonNull
    @SerializedName("id")
    private String id;

    @SerializedName("recipient")
    private String recipient;

    @SerializedName("recipientId")
    private String recipientId;

    @SerializedName("subject")
    private String subject;

    @SerializedName("text")
    private String text;

    @SerializedName("timeSent")
    private Date timeSent;

    @SerializedName("isDraft")
    private boolean isDraft;

    public static final String dateFormat = "MM/dd/yy";

    public static Letter createEmptyLetter() {
        return new Letter(
                UUID.randomUUID().toString(),
                null,
                null,
                null,
                null,
                null,
                true
        );
    }

    public Letter(
            String id,
            String recipient,
            String recipientId,
            String subject,
            String text,
            Date timeSent,
            boolean isDraft
    ) {
        this.id = id;
        this.recipient = recipient;
        this.recipientId = recipientId;
        this.subject = subject;
        this.text = text;
        this.timeSent = timeSent;
        this.isDraft = isDraft;
    }

    @Ignore
    public Letter(
            String id,
            String recipient,
            String subject,
            String text
    ) {
        this.id = id;
        this.recipient = recipient;
        this.subject = subject;
        this.text = text;
        this.timeSent = null;
        this.isDraft = true;
    }

    @Override
    public String toString() {
        return "Letter(" + "\n" +
                "id=" + this.getId() + "\n" +
                "recipient=" + this.getRecipient() + "\n" +
                "recipientId=" + this.getRecipientId() + "\n" +
                "subject=" + this.getSubject() + "\n" +
                "text=" + this.getText() + "\n" +
                "timeSent=" + this.getTimeSentString() + "\n" +
                "isDraft=" + this.isDraft + "\n" +
                ")";
    }

    public String getId() { return this.id; }
    public String getRecipient() { return this.recipient; }
    public String getRecipientId() { return this.recipientId; }
    public String getSubject() { return this.subject; }
    public String getText() { return this.text; }

    public Date getTimeSent() { return this.timeSent; }
    public String getTimeSentString() { return dateToString(this.timeSent); }
    public static String dateToString(Date date) {
        if (date == null) {
            return null;
        }

        DateFormat df = new SimpleDateFormat(dateFormat);
        return df.format(date);
    }

    public void setIsDraft(boolean isDraft) { this.isDraft = isDraft; }
    public void setSubject(String subject) { this.subject = subject; }
    public void setTimeSent(Date timeSent) { this.timeSent = timeSent; }
    public void setText(String text) { this.text = text; }

    public boolean isDraft() { return this.isDraft; }

    public boolean equals(Letter letter) {
        if (this.isDraft() != letter.isDraft) {
            return false;
        }
        // if the two letters are not drafts, then we only need to compare their IDs since the
        // content of letters cannot be edited
        else if (!this.isDraft()) {
            return this.getId() == letter.getId();
        }
        else {
            return getId().equals(letter.getId()) &&
                    getRecipient().equals(letter.getRecipient()) &&
                    getRecipientId().equals(letter.getRecipientId()) &&
                    getSubject().equals(letter.getSubject()) &&
                    getText().equals(letter.getText()) &&
                    getTimeSent().equals(letter.getTimeSent()) &&
                    isDraft() == letter.isDraft();
        }
    }

    public boolean contains(String searchQuery) {
        searchQuery = searchQuery.toLowerCase();
        return getRecipient().toLowerCase().contains(searchQuery) ||
                getRecipientId().toLowerCase().contains(searchQuery) ||
                getSubject().toLowerCase().contains(searchQuery) ||
                getText().toLowerCase().contains(searchQuery) ||
                getTimeSentString().contains(searchQuery);
    }
}