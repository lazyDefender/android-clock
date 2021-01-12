package com.example.clock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.widget.Toast;

import com.example.clock.json.Json;
import com.example.clock.models.Alarm;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AlarmRepo {
    private static String filename = "alarms";

    private static Alarm newAlarm = null;

    public static Alarm getNewAlarm() {
        return newAlarm;
    }

    public static void setNewAlarm(Alarm newAlarm) {
        AlarmRepo.newAlarm = newAlarm;
    }

    private static void writeToFile(String data, String filename) throws IOException {
        String str = data;
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        writer.write(str);

        writer.close();
    }

    private static String readFile(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));

        StringBuilder sb = new StringBuilder();
        String s = "";
        while ((s = br.readLine()) != null){
            sb.append(s);
        }
        return sb.toString();
}


    public static void save(Alarm alarm, Context context) throws IOException, ClassNotFoundException {
        File alarmsFile = new File(context.getFilesDir(), filename);
        String fileFullPath = context.getFilesDir().getAbsolutePath().concat("/" + filename);

        if(!alarmsFile.exists()) {
            List<Alarm> alarms = new ArrayList<>();
            alarms.add(alarm);
            String json = Json.toJsonArray(alarms);
            writeToFile(json, fileFullPath);
        }
        else {
            String currentJson = readFile(fileFullPath);
            List<Alarm> alarms = Json.parseJsonArray(currentJson, Alarm.class);
            alarms.add(alarm);
            String newJson = Json.toJsonArray(alarms);
            writeToFile(newJson, fileFullPath);
        }

    }

    public static void launchAlarm(Context context, Alarm alarm, AlarmManager manager, Class activityClass) {
        int i = 0;
        int[] days = alarm.getRepetitionDays();
        do {
            Calendar calendar = Calendar.getInstance();

            calendar.set(Calendar.HOUR, alarm.getHour());
            calendar.set(Calendar.MINUTE, alarm.getMin());
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.AM_PM, Calendar.AM);

            Intent intent = new Intent(context, AlarmActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, 0);

            if(days.length > 0) {
                calendar.set(Calendar.DAY_OF_WEEK, days[i] + 1);
                manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);
            }

            else {
                manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }

            i++;
        }
        while(i < days.length);
    }

    public static List<Alarm> findAll(Context context) throws IOException, ClassNotFoundException {
        List<Alarm> alarms = new ArrayList<>();
        File alarmsFile = new File(context.getFilesDir(), filename);
        String fileFullPath = context.getFilesDir().getAbsolutePath().concat("/" + filename);
        if(alarmsFile.exists()) {
            String json = readFile(fileFullPath);
            alarms = Json.parseJsonArray(json, Alarm.class);
            if(alarms.size() > 4) {alarms.clear(); writeToFile("[]", fileFullPath);}
        }
        else {
            String json = "[]";
            writeToFile(json, fileFullPath);
        }

        return alarms;
    }
}
