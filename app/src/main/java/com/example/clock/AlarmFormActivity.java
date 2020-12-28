package com.example.clock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.clock.databinding.ActivityAlarmFormBinding;
import com.example.clock.handlers.AlarmFormHandler;
import com.example.clock.models.Alarm;
import com.example.clock.models.Tune;

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) {
            if(resultCode == RESULT_OK) {
                Tune selectedTune = data.getParcelableExtra("tune");
                Alarm alarm = activityAlarmFormBinding.getAlarm();
                alarm.setTune(selectedTune);
                activityAlarmFormBinding.setAlarm(alarm);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case android.R.id.home:
                Toast.makeText(this, "sure?", Toast.LENGTH_LONG).show();
                return false;
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