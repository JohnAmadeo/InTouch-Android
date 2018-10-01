package com.example.android.intouch_android;

import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.intouch_android.SentLettersFragment.OnListFragmentInteractionListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Letter} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class SentLettersAdapter extends RecyclerView.Adapter<SentLetterViewHolder> {
    private final OnListFragmentInteractionListener mListener;
    private final SortedList<Letter> mSortedLetters = new SortedList<>(
            Letter.class,
            new SortedList.Callback<Letter>() {
                // TODO: Need to implement proper date comparison
                @Override
                public int compare(Letter letter1, Letter letter2) {
                    return letter2.getTimeSent().compareTo(letter1.getTimeSent());
                }

                @Override
                public void onChanged(int position, int count) {
                    notifyItemRangeChanged(position, count);
                }

                @Override
                public boolean areContentsTheSame(Letter letter1, Letter letter2) {
                    return letter1.equals(letter2);
                }

                @Override
                public boolean areItemsTheSame(Letter letter1, Letter letter2) {
                    return letter1.getId().equals(letter2.getId());
                }

                @Override
                public void onInserted(int position, int count) {
                    notifyItemRangeInserted(position, count);
                }

                @Override
                public void onRemoved(int position, int count) {
                    notifyItemRangeRemoved(position, count);
                }

                @Override
                public void onMoved(int fromPosition, int toPosition) {
                    notifyItemMoved(fromPosition, toPosition);
                }
            }
    );

    public SentLettersAdapter(OnListFragmentInteractionListener listener) {
        mListener = listener;
    }

    @Override
    public SentLetterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_letters_list_item, parent, false);
        return new SentLetterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SentLetterViewHolder holder, int position) {
        holder.mItem = mSortedLetters.get(position);
        holder.mRecipientView.setText(holder.mItem.getRecipient());
        if (!holder.mItem.getSubject().isEmpty()) {
            holder.mSubjectView.setText(holder.mItem.getSubject());
        } else {
            holder.mSubjectView.setVisibility(View.GONE);
        }
        holder.mTextView.setText(holder.mItem.getText());
        holder.mTimeSentView.setText(holder.mItem.getTimeSent());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() { return mSortedLetters.size(); }

    public void setLetters(List<Letter> letters) {
        // Initial load
        if (mSortedLetters.size() == 0) {
            mSortedLetters.addAll(letters);
        }
        // Change list of letters based on search query results
        else {
            mSortedLetters.beginBatchedUpdates();
            // Loop has to be in reverse since otherwise removing an item would mess up the indexes
            // of all the items that come after it
            for (int i = mSortedLetters.size() - 1; i >= 0; i--) {
                Letter letter = mSortedLetters.get(i);
                if (!letters.contains(letter)) {
                    mSortedLetters.remove(letter);
                }
            }

            // SortedList's addAll function does not add duplicates based on areContentsTheSame()
            mSortedLetters.addAll(letters);
            mSortedLetters.endBatchedUpdates();
        }
    }
}
