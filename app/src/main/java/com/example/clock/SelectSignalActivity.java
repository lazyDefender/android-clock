package com.example.clock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import com.example.clock.adapters.AlarmsListAdapter;
import com.example.clock.adapters.SignalsListAdapter;
import com.example.clock.databinding.ActivitySelectSignalBinding;
import com.example.clock.models.Alarm;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectSignalActivity extends AppCompatActivity {
    ActivitySelectSignalBinding activitySelectSignalBinding;
    private SignalsListAdapter adapter;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if(requestCode == 999 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(this, "granted!", Toast.LENGTH_LONG).show();
//            ContentResolver contentResolver = getContentResolver();
//            Uri uriExternal = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//            Cursor cursor = null;
//            try {
//                cursor = contentResolver.query(uriExternal, null, null, null, null);
//            }
//            catch(Exception e) {
//                new String();
//            }
//            if (cursor != null && cursor.moveToFirst()) {
//                do {
//                    String songTitle = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
//                    String songId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
//                    Uri songUri = Uri.withAppendedPath(uriExternal, songId);
//                    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//                    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), songUri);
//                    r.play();
//                    new String();
//
//                } while (cursor.moveToNext());
//            }
//
//        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 999);

        activitySelectSignalBinding = DataBindingUtil.setContentView(this, R.layout.activity_select_signal);
        setSupportActionBar(activitySelectSignalBinding.toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        List<Alarm> signals = new ArrayList<>();
        adapter = new SignalsListAdapter(signals);
        activitySelectSignalBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        activitySelectSignalBinding.recyclerView.setAdapter(adapter);
        activitySelectSignalBinding.recyclerView.setNestedScrollingEnabled(false);

        List<Alarm> signalsList = new ArrayList<>();
//                getAlarmRingtones(this);
        AlarmsListAdapter adapter = new AlarmsListAdapter(signalsList);
        activitySelectSignalBinding.recyclerView.setAdapter(adapter);
    }

    public Map<String, String> getAlarmRingtones(Context context) {
        RingtoneManager manager = new RingtoneManager(context);
        manager.setType(RingtoneManager.TYPE_ALARM);
        Cursor cursor = manager.getCursor();

        Map<String, String> list = new HashMap<>();
        while (cursor.moveToNext()) {
            String id = cursor.getString(RingtoneManager.ID_COLUMN_INDEX);
            String notificationTitle = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
            String notificationUri = cursor.getString(RingtoneManager.URI_COLUMN_INDEX);

            list.put(notificationTitle, notificationUri + '/' + id);
        }

        return list;
    }
}