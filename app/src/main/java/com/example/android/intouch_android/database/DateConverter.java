package com.example.android.intouch_android.database;

import android.arch.persistence.room.TypeConverter;

import com.example.android.intouch_android.model.Letter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateConverter {
    @TypeConverter
    public static Date toDate(String dateString) {
        DateFormat df = new SimpleDateFormat(Letter.dateFormat, Locale.ENGLISH);
        try {
            return df.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date(Long.MIN_VALUE);
        }
    }

    @TypeConverter
    public static String toString(Date date) {
        return Letter.dateToString(date);
    }
}
