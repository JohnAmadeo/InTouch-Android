package com.example.android.intouch_android.ui.inmatesearch;

import android.arch.core.util.Function;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.intouch_android.R;
import com.example.android.intouch_android.model.Inmate;
import com.example.android.intouch_android.model.Inmate;
import com.example.android.intouch_android.model.Letter;
import com.example.android.intouch_android.utils.OnListItemClickListener;

import java.util.List;

public class InmateSearchAdapter extends RecyclerView.Adapter<InmateSearchViewHolder> {
    private final OnListItemClickListener<Inmate> mOnClickListener;
    private final SortedList<Inmate> mSortedInmates = new SortedList<>(
            Inmate.class,
            new SortedList.Callback<Inmate>() {
                // TODO: Need to implement proper date comparison
                @Override
                public int compare(Inmate inmate1, Inmate inmate2) {
                    return inmate1.getName().compareTo(inmate2.getName());
                }

                @Override
                public void onChanged(int position, int count) {
                    notifyItemRangeChanged(position, count);
                }

                @Override
                public boolean areContentsTheSame(Inmate inmate1, Inmate inmate2) {
                    return inmate1.equals(inmate2);
                }

                @Override
                public boolean areItemsTheSame(Inmate inmate1, Inmate inmate2) {
                    return inmate1.equals(inmate2);
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

    public InmateSearchAdapter(OnListItemClickListener<Inmate> onClickListener) {
        mOnClickListener = onClickListener;
    }

    @Override
    public InmateSearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_inmate_search_list_item, parent, false);
        return new InmateSearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final InmateSearchViewHolder holder, int position) {
        holder.bind(mSortedInmates.get(position), mOnClickListener);
    }

    @Override
    public int getItemCount() { return mSortedInmates.size(); }

    public void setInmates(List<Inmate> inmates) {
        // Initial load
        if (mSortedInmates.size() == 0) {
            mSortedInmates.addAll(inmates);
        }
        // Change list of letters based on search query results
        else {
            mSortedInmates.beginBatchedUpdates();
            // Loop has to be in reverse since otherwise removing an item would mess up the indexes
            // of all the items that come after it
            for (int i = mSortedInmates.size() - 1; i >= 0; i--) {
                Inmate inmate = mSortedInmates.get(i);
                if (!inmates.contains(inmate)) {
                    mSortedInmates.remove(inmate);
                }
            }

            // SortedList's addAll function does not add duplicates based on areContentsTheSame()
            mSortedInmates.addAll(inmates);
            mSortedInmates.endBatchedUpdates();
        }
    }
}
