package com.example.clock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.clock.databinding.ActivityAlarmFormBinding;
import com.example.clock.handlers.AlarmFormHandler;
import com.example.clock.models.Alarm;
import com.example.clock.models.Tune;
import com.example.clock.utils.RequestCodes;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.TimeZone;

public class AlarmFormActivity extends AppCompatActivity{

    ActivityAlarmFormBinding activityAlarmFormBinding;
    AlarmFormHandler alarmFormHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityAlarmFormBinding = DataBindingUtil.setContentView(this, R.layout.activity_alarm_form);

        setSupportActionBar(activityAlarmFormBinding.toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        alarmFormHandler = new AlarmFormHandler();
        activityAlarmFormBinding.setAlarmFormHandler(alarmFormHandler);


        Uri alarmToneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        Ringtone ringtone = RingtoneManager.getRingtone(this, alarmToneUri);

        String title = ringtone.getTitle(this);
        Alarm defaultAlarm = new Alarm();
        defaultAlarm.setTune(new Tune(title, alarmToneUri.toString()));
        activityAlarmFormBinding.setAlarm(defaultAlarm);

        activityAlarmFormBinding.timepicker.setOnTimeChangedListener((timePicker, hour, min) -> {
            Alarm alarm = activityAlarmFormBinding.getAlarm();
            alarm.setHour(hour);
            alarm.setMin(min);
            activityAlarmFormBinding.setAlarm(alarm);
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case android.R.id.home:
                Toast.makeText(this, "sure?", Toast.LENGTH_LONG).show();
                return false;
            case R.id.save_alarm:
                try {
                    Alarm alarm = activityAlarmFormBinding.getAlarm();
                    AlarmRepo.save(alarm, this);
                    AlarmRepo.setNewAlarm(alarm);

                    int i = 0;
                    int[] days = alarm.getRepetitionDays();
                    do {
                        Calendar calendar = Calendar.getInstance();

                        calendar.set(Calendar.HOUR, alarm.getHour());
                        calendar.set(Calendar.MINUTE, alarm.getMin());
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.AM_PM, Calendar.AM);

                        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        Intent intent = new Intent(this, AlarmActivity.class);
                        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

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








                    finish();
                } catch (JsonProcessingException | ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.alarm_form_menu, menu);
        return true;
    }
}