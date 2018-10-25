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

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("lastName")
    private String lastName;

    @SerializedName("inmateNumber")
    private String inmateNumber;

    @SerializedName("dateOfBirth")
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

    @Override
    public String toString() {
        return "Inmate(" +
                "id=" + this.getId() +
                " name=" + this.getName() +
                " inmateNumber=" + this.getInmateNumber() +
                " dob=" + this.getDateOfBirth() +
                " facility=" + this.getFacility() +
                ")";
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
