package com.example.android.intouch_android.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(
        tableName = "correspondences",
        primaryKeys = {"inmateId", "username"},
        foreignKeys = @ForeignKey(
                entity = Inmate.class,
                parentColumns = {"id"},
                childColumns = {"inmateId"},
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE
        )
)
public class Correspondence {
    @NonNull
    @SerializedName("inmate_id")
    private String inmateId;

    @NonNull
    @SerializedName("username")
    private String username;

    @NonNull
    @SerializedName("occurrences")
    private Integer occurrences;

    public Correspondence(String inmateId, String username) {
        this.inmateId = inmateId;
        this.username = username;
        this.occurrences = 1;
    }

    @NonNull
    public String getInmateId() { return this.inmateId; }

    @NonNull
    public String getUsername() { return this.username; }

    @NonNull
    public Integer getOccurrences() { return this.occurrences; }

    public void setOccurrences(Integer occurrences) { this.occurrences = occurrences; }
}
