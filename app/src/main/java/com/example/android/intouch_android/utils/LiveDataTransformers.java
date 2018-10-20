package com.example.android.intouch_android.utils;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

public class LiveDataTransformers {
    public LiveDataTransformers() { }

    @MainThread
    public static <X> LiveData<X> debounce(
            @NonNull LiveData<X> source,
            @NonNull int intervalInMilliseconds
    ) {
        final MediatorLiveData<X> debouncedSource = new MediatorLiveData<>();

        Debouncer<String> debouncer = new Debouncer<String>(
                () -> debouncedSource.postValue(source.getValue()),
                intervalInMilliseconds
        );

        debouncedSource.addSource(source, __ -> debouncer.call("key"));

        return debouncedSource;
    }
}
