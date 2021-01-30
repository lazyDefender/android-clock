package com.example.clock;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.example.clock.databinding.ActivityAlarmBinding;
import com.example.clock.handlers.AlarmHandler;
import com.example.clock.models.Alarm;
import com.example.clock.models.Tune;
import com.example.clock.repos.AlarmRepo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.os.Handler;
import android.os.PowerManager;
import android.view.View;
import android.view.WindowManager;

import java.io.IOException;

public class AlarmActivity extends AppCompatActivity {

    ActivityAlarmBinding activityAlarmBinding;
    MediaPlayer player;

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
            int id = extras.getInt("alarmId");
            Alarm alarm = AlarmRepo.findById(this, id);
            activityAlarmBinding.setAlarm(alarm);

            player = new MediaPlayer();
            Activity activity = this;
            AlarmHandler handler = new AlarmHandler() {
                @Override
                public void onStop() {
                    player.stop();
                    activity.finish();
                    try {
                        if(alarm.getRepetitionDays().length == 0) {
                            AlarmRepo.delete(activity, id);
                            alarm.setActive(false);
                            AlarmRepo.save(activity, alarm);
                            AlarmRepo.setDismissedAlarm(alarm);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            };
            activityAlarmBinding.setHandler(handler);

            Tune tune = alarm.getTune();
            boolean isCustom = tune.isCustom();
            String tuneId = tune.getId();
            String tuneUri = "";
            if(!isCustom) {
                String tunePath = tuneId.length() > 0 ? '/' + tuneId : tuneId;
                tuneUri = tune.getDirectoryUri() + tunePath;
            }
            else {
                tuneUri = tune.getDirectoryUri();
            }

            player.setDataSource(this, Uri.parse(tuneUri));
            player.setLooping(true);
            player.prepare();
            player.start();
        }
        catch(Exception e) {
            new String();
        }
    }
}