package com.example.clock;

import android.app.AlarmManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.clock.models.Alarm;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            List<Alarm> alarms = AlarmRepo.findAll(context);
            AlarmManager manager = (AlarmManager) context.getSystemService(Service.ALARM_SERVICE);
            alarms.stream().forEach(alarm -> {
                AlarmRepo.launchAlarm(context, alarm, manager, AlarmActivity.class);
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
