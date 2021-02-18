package com.example.clock;

import android.app.AlarmManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.clock.models.Alarm;
import com.example.clock.repos.AlarmRepo;

import java.io.IOException;
import java.util.List;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            List<Alarm> alarms = AlarmRepo.findAll(context);
            AlarmManager manager = (AlarmManager) context.getSystemService(Service.ALARM_SERVICE);
            alarms
                    .stream()
                    .filter(alarm -> alarm.isActive())
                    .forEach(alarm -> {
                AlarmRepo.launchAlarm(context, alarm, manager);
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
