package com.example.android.intouch_android.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.Date;

@Parcel
@Entity(tableName = "inmates")
public class Inmate {
    /*
     * Class variables are not given "private" access modifier since Parceler gives build complaints
     * due to private variables being much slower to work with. If no access modifier is given,
     * then the visibility of class variables is as such:
     *
     *             │ Class │ Package │ Subclass │ Subclass │ World
     *             │       │         │(same pkg)│(diff pkg)│
     * ────────────┼───────┼─────────┼──────────┼──────────┼────────
     * public      │   +   │    +    │    +     │     +    │   +
     * ────────────┼───────┼─────────┼──────────┼──────────┼────────
     * protected   │   +   │    +    │    +     │     +    │
     * ────────────┼───────┼─────────┼──────────┼──────────┼────────
     * no modifier │   +   │    +    │    +     │          │
     * ────────────┼───────┼─────────┼──────────┼──────────┼────────
     * private     │   +   │         │          │          │
     *
     */

    @PrimaryKey
    @NonNull
    @SerializedName("id")
    String id;

    @SerializedName("firstName")
    String firstName;

    @SerializedName("lastName")
    String lastName;

    @SerializedName("inmateNumber")
    String inmateNumber;

    @SerializedName("dateOfBirth")
    Date dateOfBirth;

    @SerializedName("facility")
    String facility;

    public static final String dateFormat = "MM/dd/yy";

    @ParcelConstructor
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
