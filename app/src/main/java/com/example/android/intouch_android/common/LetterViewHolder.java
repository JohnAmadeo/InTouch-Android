package com.example.android.intouch_android.common;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.android.intouch_android.R;
import com.example.android.intouch_android.model.Letter;
import com.example.android.intouch_android.utils.OnListItemClickListener;

/**
 * Specifies the logic for individual list components
 */
public class LetterViewHolder extends RecyclerView.ViewHolder {
    private final String NO_RECIPIENT = "(No recipient)";
    private final String NO_SUBJECT = "(No subject)";

    private final View mView;
    private final TextView mRecipientView;
    private final TextView mSubjectView;
    private final TextView mTextView;
    private final TextView mTimeView;

    public LetterViewHolder(View view) {
        super(view);
        mView = view;
        mRecipientView = view.findViewById(R.id.recipient);
        mSubjectView = view.findViewById(R.id.subject);
        mTextView = view.findViewById(R.id.text);
        mTimeView = view.findViewById(R.id.time);
    }

    public void bind(final Letter letter, final OnListItemClickListener<Letter> onClick) {
        if (letter.getRecipient() == null || letter.getRecipient().isEmpty()) {
            mRecipientView.setText(NO_RECIPIENT);
        }
        else {
            mRecipientView.setText(letter.getRecipient());
        }

        if (letter.getSubject().isEmpty()) {
            mSubjectView.setText(NO_SUBJECT);
        } else {
            mSubjectView.setText(letter.getSubject());
        }

        if (letter.getText().isEmpty()) {
            mTextView.setVisibility(View.GONE);
        }
        else {
            mTextView.setText(letter.getText());
        }

        if (letter.isDraft()) {
            mTimeView.setText(letter.getTimeLastEditedString());
        }
        else {
            mTimeView.setText(letter.getTimeSentString());
        }
        mView.setOnClickListener(__ -> onClick.apply(letter));
    }

    @Override
    public String toString() {
        return super.toString() + " '" + mTextView.getText() + "'";
    }
}


