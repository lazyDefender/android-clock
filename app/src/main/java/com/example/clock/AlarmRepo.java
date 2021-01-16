package com.example.clock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.clock.json.Json;
import com.example.clock.models.Alarm;
import com.example.clock.utils.RandomID;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

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


    public static void save(Context context, Alarm alarm) throws IOException, ClassNotFoundException {
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

    public static PendingIntent createAlarmPendingIntent(Context context, Alarm alarm, int id) {
        Intent intent = new Intent(context, AlarmActivity.class);
        intent.putExtra("alarmId", alarm.getId());
        PendingIntent pendingIntent = PendingIntent.getActivity(context, id, intent, 0);
        return pendingIntent;
    }

    public static List<Integer> launchAlarm(Context context, Alarm alarm, AlarmManager manager) {
        int i = 0;
        int[] days = alarm.getRepetitionDays();
        List<Integer> alarmManagerTaskIds = new ArrayList<>();

        do {
            Calendar calendar = Calendar.getInstance();

            calendar.set(Calendar.HOUR, alarm.getHour());
            calendar.set(Calendar.MINUTE, alarm.getMin());
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.AM_PM, Calendar.AM);

            int alarmManagerTaskId = RandomID.generate();
            PendingIntent pendingIntent = createAlarmPendingIntent(context, alarm, alarmManagerTaskId);

            if(days.length > 0) {
                calendar.set(Calendar.DAY_OF_WEEK, days[i] + 1);
                manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);
            }

            else {
                manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }

            alarmManagerTaskIds.add(alarmManagerTaskId);

            i++;
        }
        while(i < days.length);
        return alarmManagerTaskIds;
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

    public static Alarm findById(Context context, int id) throws IOException, ClassNotFoundException {
        List<Alarm> alarms = findAll(context);
        Alarm foundAlarm = alarms
                .stream()
                .filter(alarm -> alarm.getId() == id)
                .findFirst()
                .orElse(new Alarm());
        return foundAlarm;
    }

    public static void delete(Context context, int id) throws IOException, ClassNotFoundException {
        List<Alarm> alarms = findAll(context);
        Alarm alarmToDelete = alarms
                .stream()
                .filter(alarm -> alarm.getId() == id)
                .findFirst()
                .get();
        alarms.remove(alarmToDelete);

        String newJson = Json.toJsonArray(alarms);
        String fileFullPath = context.getFilesDir().getAbsolutePath().concat("/" + filename);
        writeToFile(newJson, fileFullPath);

    }
}
