package com.example.android.intouch_android.utils;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Transforms {
    public Transforms() { }

    @MainThread
    public static <X> LiveData<X> debounce(
            @NonNull LiveData<X> source,
            @NonNull int intervalInMilliseconds
    ) {
        final MediatorLiveData<X> debouncedSource = new MediatorLiveData<>();

        Debouncer<String> debouncer = new Debouncer<>(
                () -> debouncedSource.postValue(source.getValue()),
                intervalInMilliseconds
        );

        debouncedSource.addSource(source, __ -> debouncer.call("key"));

        return debouncedSource;
    }

    public static <X> List<X> filter(List<X> items, Function<X, Boolean> filter) {
        List<X> filteredItems = new ArrayList<>();

        for (X item:items) {
            if (filter.apply(item)) {
                filteredItems.add(item);
            }
        }
        return filteredItems;
    }

    public static <X, Y> List<Y> map(List<X> items, Function<X, Y> map) {
        List<Y> mappedItems = new ArrayList<>();

        for (X item:items) {
            mappedItems.add(map.apply(item));
        }
        return mappedItems;
    }

    public static <X> List<X> merge(List<X> list1, List<X> list2) {
        List<X> mergedList = new ArrayList<>();

        for(X item:list1) {
            mergedList.add(item);
        }
        for(X item:list2) {
            mergedList.add(item);
        }

        return mergedList;
    }
}
