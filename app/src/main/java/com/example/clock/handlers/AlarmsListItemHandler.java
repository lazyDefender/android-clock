package com.example.clock.handlers;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;

import com.example.clock.AlarmFormActivity;
import com.example.clock.models.Alarm;
import com.example.clock.repos.AlarmRepo;
import com.example.clock.utils.AlarmActions;
import com.example.clock.utils.RequestCodes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AlarmsListItemHandler {
    public void onShowForm(View view, Alarm alarm) {
        Context context = view.getContext();
        Intent intent = new Intent(context, AlarmFormActivity.class);
        intent.putExtra("action", AlarmActions.UPDATE);
        intent.putExtra("alarmId", alarm.getId());
        context.startActivity(intent);
    }

    public void onActiveChange(View view, Alarm alarm) {
        Context context = view.getContext();
        SwitchCompat sw = (SwitchCompat) view;
        boolean checked = sw.isChecked();
        AlarmManager manager = (AlarmManager) context.getSystemService(Service.ALARM_SERVICE);
        if(!checked) {
            AlarmRepo.cancelAlarm(context, alarm, manager);
            alarm.setActive(false);
            alarm.setAlarmManagerTaskIds(new ArrayList<>());
            try {
                AlarmRepo.delete(context, alarm.getId());
                AlarmRepo.save(context, alarm);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        else {
            List<Integer> taskIds = AlarmRepo.launchAlarm(context, alarm, manager);
            alarm.setAlarmManagerTaskIds(taskIds);
            alarm.setActive(true);
            try {
                AlarmRepo.delete(context, alarm.getId());
                AlarmRepo.save(context, alarm);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
}
