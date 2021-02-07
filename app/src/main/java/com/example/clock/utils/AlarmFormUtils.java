package com.example.clock.utils;

public class AlarmFormUtils {
    public static String getRepetitionDays(int[] days) {
        String[] dayNames = {
                "пн",
                "вт",
                "ср",
                "чт",
                "пт",
                "сб",
                "нд"
        };

        StringBuilder sb = new StringBuilder();
        for(int i=0;i<days.length;i++) {
            int index = days[i];
            String day = dayNames[index];
            sb.append(day);
            sb.append(" ");
        }
        String result;
        if(days.length == 0) result = "Сигнал лише раз";
        else result = sb.toString().trim();
        return result;
    }

    public static String getTuneTitle(String input) {
        int openingParenthesisIndex = input.indexOf('(');
        int closingParenthesisIndex = input.indexOf(')');
        if(closingParenthesisIndex < 0) closingParenthesisIndex = input.length();
        String result = input.substring(openingParenthesisIndex + 1, closingParenthesisIndex);
        return result;
    }
}
