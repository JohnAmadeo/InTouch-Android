package com.example.android.intouch_android.common;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.android.intouch_android.R;
import com.example.android.intouch_android.model.Letter;

/**
 * Specifies the logic for individual list components
 */
public class LetterViewHolder extends RecyclerView.ViewHolder {
    public final View mView;
    public final TextView mRecipientView;
    public final TextView mSubjectView;
    public final TextView mTextView;
    public final TextView mTimeView;
    public Letter mItem;

    public LetterViewHolder(View view) {
        super(view);
        mView = view;
        mRecipientView = view.findViewById(R.id.recipient);
        mSubjectView = view.findViewById(R.id.subject);
        mTextView = view.findViewById(R.id.text);
        mTimeView = view.findViewById(R.id.time);
    }

    @Override
    public String toString() {
        return super.toString() + " '" + mTextView.getText() + "'";
    }
}


