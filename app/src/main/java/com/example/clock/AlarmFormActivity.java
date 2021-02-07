package com.example.clock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import com.example.clock.repos.AlarmRepo;
import com.example.clock.utils.AlarmActions;
import com.example.clock.utils.RandomID;
import com.example.clock.utils.RequestCodes;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.util.List;

public class AlarmFormActivity extends AppCompatActivity{

    ActivityAlarmFormBinding activityAlarmFormBinding;
    AlarmFormHandler alarmFormHandler;
    private boolean wasTuneChanged;
    private boolean formWasTouched;
    private int action;
    private AlarmManager manager;

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public boolean getFormWasTouched() {
        return formWasTouched;
    }

    public void setFormWasTouched(boolean formWasTouched) {
        this.formWasTouched = formWasTouched;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == RequestCodes.READ_EXTERNAL_STORAGE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            alarmFormHandler.showSignalSelect(this, activityAlarmFormBinding.getAlarm().getTune());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AlarmFormActivity activity = this;
        activityAlarmFormBinding = DataBindingUtil.setContentView(this, R.layout.activity_alarm_form);

        setSupportActionBar(activityAlarmFormBinding.toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        setAction(extras.getInt("action"));



        alarmFormHandler = new AlarmFormHandler() {
            @Override
            public void afterHandle() {
                setFormWasTouched(true);
            }

            @Override
            public void onSaveAlarm() {
                Alarm alarm = activity.activityAlarmFormBinding.getAlarm();
                activity.onSaveAlarm(activity, alarm);
            }
        };
        activityAlarmFormBinding.setAlarmFormHandler(alarmFormHandler);


        Uri alarmToneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        Ringtone ringtone = RingtoneManager.getRingtone(this, alarmToneUri);

        String title = ringtone.getTitle(this);
        Alarm defaultAlarm = new Alarm();
        defaultAlarm.setTune(new Tune(title, alarmToneUri.toString()));
        if(action == AlarmActions.UPDATE) {
            int alarmId = extras.getInt("alarmId");
            try {
                defaultAlarm = AlarmRepo.findById(this, alarmId);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        activityAlarmFormBinding.setAlarm(defaultAlarm);

        if(action == AlarmActions.UPDATE) {
            activityAlarmFormBinding.timepicker.setHour(defaultAlarm.getHour());
            activityAlarmFormBinding.timepicker.setMinute(defaultAlarm.getMin());
        }

        activityAlarmFormBinding.timepicker.setOnTimeChangedListener((timePicker, hour, min) -> {
            Alarm alarm = activityAlarmFormBinding.getAlarm();
            alarm.setHour(hour);
            alarm.setMin(min);
            activityAlarmFormBinding.setAlarm(alarm);

            setFormWasTouched(true);
        });

        manager = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
    }

    private void onSaveAlarm(Context context, Alarm alarm) {
        try {
            List<Integer> alarmManagerTaskIds = AlarmRepo.launchAlarm(context, alarm, manager);
            alarm.setAlarmManagerTaskIds(alarmManagerTaskIds);
            AlarmRepo.save(context, alarm);
            AlarmRepo.setNewAlarm(alarm);
        } catch (JsonProcessingException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(requestCode == RequestCodes.SHOW_TUNE_SELECT && resultCode == Activity.RESULT_OK) {
            Bundle extras = intent.getExtras();
            Tune tune = (Tune) extras.get("tune");
            boolean isTuneCustom = extras.getBoolean("isCustom");
            wasTuneChanged = extras.getBoolean("wasTuneChanged");
            tune.setCustom(isTuneCustom);
            Alarm alarm = activityAlarmFormBinding.getAlarm();
            alarm.setTune(tune);
            activityAlarmFormBinding.setAlarm(alarm);
        }
    }

    @Override
    public void onBackPressed() {
        boolean wasChanged = formWasTouched || wasTuneChanged;
        alarmFormHandler.onBack(this, wasChanged);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case android.R.id.home:
                boolean wasChanged = formWasTouched || wasTuneChanged;
                alarmFormHandler.onBack(this, wasChanged);
                return false;
            case R.id.save_alarm:
                Alarm alarm = activityAlarmFormBinding.getAlarm();
                if(action == AlarmActions.UPDATE) {

                    AlarmRepo.cancelAlarm(this, alarm, manager);
                    try {
                        int oldAlarmId = alarm.getId();
                        AlarmRepo.delete(this, oldAlarmId);
                        AlarmRepo.setDeletedDuringUpdateId(oldAlarmId);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                alarm.setId(RandomID.generate());
                onSaveAlarm(this, alarm);
                finish();
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