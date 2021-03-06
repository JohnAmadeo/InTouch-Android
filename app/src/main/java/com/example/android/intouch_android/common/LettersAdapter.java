package com.example.android.intouch_android.common;

import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.intouch_android.R;
import com.example.android.intouch_android.model.Letter;
import com.example.android.intouch_android.utils.BaseUtils;
import com.example.android.intouch_android.utils.OnListItemClickListener;

import java.util.List;

public class LettersAdapter extends RecyclerView.Adapter<LetterViewHolder> {
    private final String LOG_TAG = this.getClass().getSimpleName();

    private final OnListItemClickListener<Letter> mOnClick;
    private final SortedList<Letter> mSortedLetters = new SortedList<>(
            Letter.class,
            new SortedList.Callback<Letter>() {
                // TODO: Need to implement proper date comparison
                @Override
                public int compare(Letter letter1, Letter letter2) {
                    if (letter1.isDraft() && letter2.isDraft()) {
                        int timeComparison =
                                letter2.getTimeLastEdited().compareTo(letter1.getTimeLastEdited());

                        return timeComparison != 0 ?
                                timeComparison :
                                BaseUtils.compareTo(letter1.getRecipient(), letter2.getRecipient());
                    }
                    else if (!letter1.isDraft() && !letter2.isDraft()) {
                        int timeComparison = letter2.getTimeSent().compareTo(letter1.getTimeSent());

                        return timeComparison != 0 ?
                                timeComparison :
                                BaseUtils.compareTo(letter1.getRecipient(), letter2.getRecipient());
                    }
                    else {
                        Log.d(LOG_TAG, "Warning: Comparing letter to draft!");
                        return -1;
                    }
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

    public LettersAdapter(OnListItemClickListener<Letter> onClick) { mOnClick = onClick; }

    @Override
    public LetterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_letters_list_item, parent, false);
        return new LetterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final LetterViewHolder holder, int position) {
        holder.bind(mSortedLetters.get(position), mOnClick);
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

            for (Letter letter:letters) {
                // SortedList#add checks for duplicates
                mSortedLetters.add(letter);
            }
            mSortedLetters.endBatchedUpdates();
        }
    }
}
