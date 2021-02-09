package com.example.clock;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
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
import com.example.clock.utils.AlarmUtils;
import com.example.clock.utils.AudioFocus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.databinding.DataBindingUtil;

import android.os.Handler;
import android.os.PowerManager;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class AlarmActivity extends AppCompatActivity {

    ActivityAlarmBinding activityAlarmBinding;
    MediaPlayer player;
    AudioFocusRequest audioFocusRequest;
    AudioManager audioManager;
    final Handler vibeHandler = new Handler();
    Timer timer = new Timer(false);

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


            setVolumeControlStream(AudioManager.STREAM_ALARM);
            player = new MediaPlayer();
            Activity activity = this;
            audioManager = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
            AlarmHandler handler = new AlarmHandler() {
                @Override
                public void onStop() {
                    onAlarmStop(alarm);
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

            audioFocusRequest = AudioFocus.createRequest(audioManager, player);
            int audioFocusResult = audioManager.requestAudioFocus(audioFocusRequest);
            if (audioFocusResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                player.start();
            }

            if(alarm.getShouldVibrate()) {
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                TimerTask vibratorTimerTask = new TimerTask() {
                    @Override
                    public void run() {
                        vibeHandler.post(() -> {
                            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                        });
                    }
                };
                timer.scheduleAtFixedRate(vibratorTimerTask, 0, 1000);
            }

            Handler alarmStopHandler = new Handler();

            alarmStopHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    NotificationManager notificationManager;

                    NotificationCompat.Builder builder =
                            new NotificationCompat.Builder(activity.getApplicationContext(), "notify_001");
                    Intent intent = new Intent(activity.getApplicationContext(), MainActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, intent, 0);

                    NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
                    bigText.bigText(AlarmUtils.formatTime(alarm.getHour(), alarm.getMin()));
                    bigText.setBigContentTitle(alarm.getTitle());
                    bigText.setSummaryText("Сигнал");

                    builder.setContentIntent(pendingIntent);
                    builder.setSmallIcon(R.drawable.ic_alarm);
                    builder.setStyle(bigText);

                    notificationManager =
                            (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    {
                        String channelId = "Your_channel_id";
                        NotificationChannel channel = new NotificationChannel(
                                channelId,
                                "Channel human readable title",
                                NotificationManager.IMPORTANCE_HIGH);
                        notificationManager.createNotificationChannel(channel);
                        builder.setChannelId(channelId);
                    }

                    notificationManager.notify(0, builder.build());

                    onAlarmStop(alarm);
                }
            }, 40 * 1000);



       }
        catch(Exception e) {
            new String();
        }
    }

    private void onAlarmStop(Alarm alarm) {
        player.stop();
        timer.cancel();
        audioManager.abandonAudioFocusRequest(audioFocusRequest);
        finish();
        try {
            if(alarm.getRepetitionDays().length == 0) {
                AlarmRepo.delete(this, alarm.getId());
                alarm.setActive(false);
                AlarmRepo.save(this, alarm);
                AlarmRepo.setDismissedAlarm(alarm);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}