package com.example.clock.utils;

public class AlarmListItemUtils {
    private static String format(int input) {
        String str = input > 9 ?
                String.valueOf(input) :
                "0" + String.valueOf(input);

        return str;
    }
    public static String formatTime(int hour, int minute) {
        String hourStr = format(hour);
        String minStr = format(minute);

        return hourStr + ":" + minStr;
    }
}
