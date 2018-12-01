package com.example.android.intouch_android.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

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

    @SerializedName("active")
    Boolean active;

    public static final String dateFormat = "MM/dd/yy";

    public Inmate(
            String id,
            String firstName,
            String lastName,
            String inmateNumber,
            Date dateOfBirth,
            String facility,
            Boolean active
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.inmateNumber = inmateNumber;
        this.dateOfBirth = dateOfBirth;
        this.facility = facility;
        this.active = active;
    }

    @Override
    public String toString() {
        return "Inmate(" + "\n" +
                "id=" + this.getId() + "\n" +
                "name=" + this.getName() + "\n" +
                "inmateNumber=" + this.getInmateNumber() + "\n" +
                "dob=" + this.getDateOfBirth() + "\n" +
                "facility=" + this.getFacility() + "\n" +
                "active =" + this.isActive() + "\n" +
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
    public Boolean isActive() { return this.active; }

    public boolean equals(Inmate inmate) { return this.getId().equals(inmate.getId()); }
}
