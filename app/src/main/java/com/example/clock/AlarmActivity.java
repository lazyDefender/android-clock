package com.example.clock;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.example.clock.databinding.ActivityAlarmBinding;
import com.example.clock.models.Alarm;
import com.example.clock.models.Tune;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.os.PowerManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class AlarmActivity extends AppCompatActivity {

    ActivityAlarmBinding activityAlarmBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);

            activityAlarmBinding = DataBindingUtil.setContentView(this, R.layout.activity_alarm);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
            PowerManager.WakeLock  wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK |
                    PowerManager.ACQUIRE_CAUSES_WAKEUP |
                    PowerManager.ON_AFTER_RELEASE, "appname::WakeLock");

            //acquire will turn on the display
            wakeLock.acquire();
            
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                setShowWhenLocked(true);
                setTurnScreenOn(true);
            } else {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
            }



            Bundle extras = getIntent().getExtras();
            long id = extras.getLong("alarmId");
            Alarm alarm = AlarmRepo.findById(this, id);
            activityAlarmBinding.setAlarm(alarm);

            Tune tune = alarm.getTune();
            String tuneId = tune.getId();
            String tunePath = tuneId.length() > 0 ? '/' + tuneId : tuneId;
            String tuneUri = tune.getDirectoryUri() + tunePath;
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), Uri.parse(tuneUri));
            r.play();
        }
        catch(Exception e) {
            new String();
        }
    }
}