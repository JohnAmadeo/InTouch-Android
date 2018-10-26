package com.example.android.intouch_android.ui.inmatesearch;

import android.arch.core.util.Function;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.android.intouch_android.R;
import com.example.android.intouch_android.model.Inmate;
import com.example.android.intouch_android.utils.OnListItemClickListener;

/**
 * Specifies the logic for individual list components
 */
public class InmateSearchViewHolder extends RecyclerView.ViewHolder {
    private final View mView;
    private final TextView mNameView;
    private final TextView mFacilityView;
    public Inmate mInmate;

    public InmateSearchViewHolder(View view) {
        super(view);
        mView = view;
        mNameView = view.findViewById(R.id.inmate_name);
        mFacilityView = view.findViewById(R.id.inmate_facility);
    }

    public void bind(final Inmate inmate, final OnListItemClickListener<Inmate> onClickListener) {
        mInmate = inmate;
        mNameView.setText(mInmate.getName());
        mFacilityView.setText(mInmate.getFacility());

        mView.setOnClickListener(__ -> onClickListener.apply(inmate));
    }

    public Inmate getmInmate() { return mInmate; }

    @Override
    public String toString() {
        return super.toString() + " '" + mNameView.getText() + "'";
    }
}


