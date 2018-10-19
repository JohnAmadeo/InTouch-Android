package com.example.android.intouch_android.utils;

import android.arch.lifecycle.LiveData;

import com.example.android.intouch_android.model.container.Resource;

public class ModelUtils {
    public static boolean equals(Object o1, Object o2) {
        if (o1 == null) {
            return o2 == null;
        }
        if (o2 == null) {
            return false;
        }
        return o1.equals(o2);
    }

    public static <T> boolean nonNullResource(Resource<T> resource) {
        return resource != null && resource.data != null;
    }
}
