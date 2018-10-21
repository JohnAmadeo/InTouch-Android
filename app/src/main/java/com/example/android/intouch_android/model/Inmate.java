package com.example.android.intouch_android.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity(tableName = "inmates")
public class Inmate {
    @PrimaryKey
    @NonNull
    @SerializedName("id")
    private String id;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("inmate_number")
    private String inmateNumber;

    @SerializedName("date_of_birth")
    private Date dateOfBirth;

    @SerializedName("facility")
    private String facility;

    public static final String dateFormat = "MM/dd/yy";

    public Inmate(
            String id,
            String firstName,
            String lastName,
            String inmateNumber,
            Date dateOfBirth,
            String facility
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.inmateNumber = inmateNumber;
        this.dateOfBirth = dateOfBirth;
        this.facility = facility;
    }

    @NonNull
    public String getId() { return id; }
    public String getFirstName() { return this.firstName; }
    public String getLastName() { return this.lastName; }
    public String getName() { return this.firstName + " " + this.lastName; }
    public String getInmateNumber() { return this.inmateNumber; }
    public Date getDateOfBirth() { return this.dateOfBirth; }
    public String getFacility() { return this.facility; }

    public boolean equals(Inmate inmate) { return this.getId().equals(inmate.getId()); }
}
