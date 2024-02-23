package com.kaen.whatsappclone;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.databinding.InverseMethod;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimestampConverter {

    private static final String TAG = "TimestampConverter";

    // Timestamp format from the server
    private static final String SERVER_TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";

    // Desired format for displaying in the UI
    private static final String UI_DISPLAY_FORMAT = "HH:mm:ss";

    @SuppressLint("SimpleDateFormat")
    @InverseMethod("stringToTimestamp")
    public static String timestampToString(long timestamp) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(SERVER_TIMESTAMP_FORMAT, Locale.getDefault());
            Date date = new Date(timestamp);
            return dateFormat.format(date);
        } catch (Exception e) {
            Log.e(TAG, "Error converting timestamp to string: " + e.getMessage());
            return "";
        }
    }

    public static long stringToTimestamp(String value) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(SERVER_TIMESTAMP_FORMAT, Locale.getDefault());
            Date date = dateFormat.parse(value);
            if (date != null) {
                return date.getTime();
            }
        } catch (ParseException e) {
            Log.e(TAG, "Error converting string to timestamp: " + e.getMessage());
        }
        return 0;
    }

    public static String formatForUI(long timestamp) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(UI_DISPLAY_FORMAT, Locale.getDefault());
            Date date = new Date(timestamp);
            return dateFormat.format(date);
        } catch (Exception e) {
            Log.e(TAG, "Error formatting timestamp for UI display: " + e.getMessage());
            return "";
        }
    }
}
