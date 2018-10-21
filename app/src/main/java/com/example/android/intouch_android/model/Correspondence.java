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

    public Correspondence(String inmateId, String username) {
        this.inmateId = inmateId;
        this.username = username;
    }

    @NonNull
    public String getInmateId() { return inmateId; }

    @NonNull
    public String getUsername() { return username; }
}
