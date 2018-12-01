package com.example.android.intouch_android.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.example.android.intouch_android.utils.NullSafe;
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

    @ForeignKey(
            entity = User.class,
            parentColumns = { "id" },
            childColumns = { "author" },
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
    )
    @SerializedName("author")
    private String author;

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

    @SerializedName("timeLastEdited")
    private Date timeLastEdited;

    @SerializedName("timeDeliveredEstimate")
    private Date timeDeliveredEstimate;

    @SerializedName("isDraft")
    private boolean isDraft;

    @SerializedName("lobLetterId")
    private String lobLetterId;

    public static final String dateFormat = "MM/dd/yy";

    public static Letter createEmptyLetter(String author) {
        return new Letter(
                UUID.randomUUID().toString(),
                null,
                null,
                author,
                null,
                null,
                null,
                new Date(),
                null,
                true,
                null
        );
    }

    public Letter(
            String id,
            String recipient,
            String recipientId,
            String author,
            String subject,
            String text,
            Date timeSent,
            Date timeLastEdited,
            Date timeDeliveredEstimate,
            boolean isDraft,
            String lobLetterId
    ) {
        this.id = id;
        this.recipient = recipient;
        this.recipientId = recipientId;
        this.author = author;
        this.subject = subject;
        this.text = text;
        this.timeSent = timeSent;
        this.timeLastEdited = timeLastEdited;
        this.timeDeliveredEstimate = timeDeliveredEstimate;
        this.isDraft = isDraft;
        this.lobLetterId = lobLetterId;
    }

    @Override
    public String toString() {
        return "Letter(" + "\n" +
                "id=" + this.getId() + "\n" +
                "recipient=" + this.getRecipient() + "\n" +
                "recipientId=" + this.getRecipientId() + "\n" +
                "author=" + this.getAuthor() + "\n" +
                "subject=" + this.getSubject() + "\n" +
                "text=" + this.getText() + "\n" +
                "timeSent=" + this.getTimeSentString() + "\n" +
                "timeLastEdited=" + this.getTimeLastEditedString() + "\n" +
                "timeDeliveredEstimate=" + this.getTimeDeliveredEstimateString() + "\n" +
                "isDraft=" + this.isDraft() + "\n" +
                "lobLetterId=" + this.getLobLetterId() + "\n" +
                ")";
    }

    public String getId() { return this.id; }
    public String getRecipient() { return this.recipient; }
    public String getRecipientId() { return this.recipientId; }
    public String getAuthor() { return this.author; }
    public String getSubject() { return this.subject; }
    public String getText() { return this.text; }
    public String getLobLetterId() { return this.lobLetterId; }

    public Date getTimeSent() { return this.timeSent; }
    public String getTimeSentString() { return dateToString(this.timeSent); }
    public Date getTimeLastEdited() { return this.timeLastEdited; }
    public String getTimeLastEditedString() { return dateToString(this.timeLastEdited); }
    public Date getTimeDeliveredEstimate() { return this.timeLastEdited; }
    public String getTimeDeliveredEstimateString() { return dateToString(this.timeDeliveredEstimate); }
    public static String dateToString(Date date) {
        if (date == null) {
            return null;
        }

        DateFormat df = new SimpleDateFormat(dateFormat);
        return df.format(date);
    }

    public void setAuthor(String author) { this.author = author; }
    public void setTimeLastEdited(Date timeLastEdited) { this.timeLastEdited = timeLastEdited; }
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
            return this.getId().equals(letter.getId());
        }
        else {
            return getId().equals(letter.getId()) &&
                    NullSafe.equals(getRecipient(), letter.getRecipient()) &&
                    NullSafe.equals(getRecipientId(), letter.getRecipientId()) &&
                    getSubject().equals(letter.getSubject()) &&
                    getText().equals(letter.getText()) &&
                    getTimeLastEdited().equals(letter.getTimeLastEdited()) &&
                    NullSafe.equals(getTimeDeliveredEstimate(), letter.getTimeDeliveredEstimate()) &&
                    isDraft() == letter.isDraft() &&
                    NullSafe.equals(getLobLetterId(), letter.getLobLetterId());
        }
    }

    public boolean contains(String searchQuery) {
        searchQuery = searchQuery.toLowerCase();
        String timeString = isDraft() ? getTimeLastEditedString() : getTimeSentString();

        return NullSafe.strContains(getRecipient(), searchQuery) ||
                NullSafe.strContains(getRecipientId(), searchQuery) ||
                getSubject().toLowerCase().contains(searchQuery) ||
                getText().toLowerCase().contains(searchQuery) ||
                timeString.contains(searchQuery);
    }
}