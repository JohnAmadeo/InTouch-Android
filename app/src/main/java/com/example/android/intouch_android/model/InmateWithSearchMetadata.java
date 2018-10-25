package com.example.android.intouch_android.model;

public class InmateWithSearchMetadata {
    private Inmate inmate;
    private boolean isPastCorrespondent;

    public InmateWithSearchMetadata(Inmate inmate, boolean isPastCorrespondent) {
        this.inmate = inmate;
        this.isPastCorrespondent = isPastCorrespondent;
    }

    public boolean isPastCorrespondent() { return this.isPastCorrespondent; }
    public Inmate getInmate() { return this.inmate; }

    @Override
    public String toString() {
        return inmate.toString();
    }
}
