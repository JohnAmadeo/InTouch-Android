package com.example.android.intouch_android.ui.inmatesearch;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.android.intouch_android.R;
import com.example.android.intouch_android.model.Inmate;

/**
 * Specifies the logic for individual list components
 */
public class InmateSearchViewHolder extends RecyclerView.ViewHolder {
    public final View mView;
    public final TextView mNameView;
    public final TextView mFacilityView;
    public Inmate mItem;

    public InmateSearchViewHolder(View view) {
        super(view);
        mView = view;
        mNameView = view.findViewById(R.id.inmate_name);
        mFacilityView = view.findViewById(R.id.inmate_facility);
    }

    @Override
    public String toString() {
        return super.toString() + " '" + mNameView.getText() + "'";
    }
}


