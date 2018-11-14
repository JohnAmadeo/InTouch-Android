package com.example.android.intouch_android.utils;

public class NullSafe {
    // Null safe #equals for objects
    public static boolean equals(Object o1, Object o2) {
        if (o1 == null) {
            return o2 == null;
        }
        if (o2 == null) {
            return false;
        }
        return o1.equals(o2);
    }

    // Null-safe #compareTo for strings
    public static int compareTo(String s1, String s2) {
        if (s1 == null && s2 == null) {
            return 0;
        }
        else if (s1 == null) {
            return -1;
        }
        else if (s2 == null) {
            return 1;
        }

        return s1.compareTo(s2);
    }

    // Null-safe #contains for objects
    public static boolean strContains(String s1, String s2) {
        if (s1 == null || s2 == null) {
            return false;
        }
        return s1.contains(s2);
    }
}
