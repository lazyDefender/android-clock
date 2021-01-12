package com.example.clock;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.PowerManager;
import android.view.View;
import android.view.WindowManager;

public class AlarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_alarm);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

//            PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
//            PowerManager.WakeLock wakeLock = powerManager.newWakeLock(
//                    PowerManager.ACQUIRE_CAUSES_WAKEUP, "App:wake");
//            wakeLock.acquire(10 * 60 * 1000L);
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
        }
        catch(Exception e) {
            new String();
        }
    }
}