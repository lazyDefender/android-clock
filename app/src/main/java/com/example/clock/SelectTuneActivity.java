package com.example.clock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.clock.adapters.TunesListAdapter;
import com.example.clock.databinding.ActivitySelectTuneBinding;
import com.example.clock.handlers.TuneHandler;
import com.example.clock.models.Alarm;
import com.example.clock.models.Tune;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SelectTuneActivity extends AppCompatActivity {
    ActivitySelectTuneBinding activitySelectTuneBinding;
    private TunesListAdapter adapter;
    private TuneHandler handler;
    private int alarmId;

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
//                    Tune r = RingtoneManager.getRingtone(getApplicationContext(), songUri);
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

        activitySelectTuneBinding = DataBindingUtil.setContentView(this, R.layout.activity_select_tune);
        setSupportActionBar(activitySelectTuneBinding.toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        activitySelectTuneBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        activitySelectTuneBinding.recyclerView.setAdapter(adapter);
        activitySelectTuneBinding.recyclerView.setNestedScrollingEnabled(false);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        Tune selectedTune = (Tune) extras.get("tune");
        String selectedTuneId = selectedTune.getId();

        List<Tune> tunesList = getAlarmTunes(this);

        int selectedTuneIndex = selectedTuneId.length() > 0 ?
                tunesList.indexOf(selectedTune) : 0;
        activitySelectTuneBinding.setTunes(tunesList);
        handler = new TuneHandler(activitySelectTuneBinding, selectedTuneIndex) {
            @Override
            public void afterHandle() {
                adapter.notifyDataSetChanged();
            }
        };
        adapter = new TunesListAdapter(activitySelectTuneBinding.getTunes(), handler);
        activitySelectTuneBinding.recyclerView.setAdapter(adapter);

        activitySelectTuneBinding.setHandler(handler);
    }

    public void onBack() throws IOException, ClassNotFoundException {
        List<Tune> tunes = activitySelectTuneBinding.getTunes();
        Alarm currentAlarm = new Alarm();
        try {
            currentAlarm = AlarmRepo.findById(this, alarmId);
        }
        catch(Exception e) {

        }

        Tune currentTune = currentAlarm.getTune();
        handler.onBack(this, tunes, currentTune.getId());
    }

    @Override
    public void onBackPressed() {
        try {
            onBack();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<Tune> getAlarmTunes(Context context) {
        RingtoneManager manager = new RingtoneManager(context);
        manager.setType(RingtoneManager.TYPE_ALARM);
        Cursor cursor = manager.getCursor();
        List<Tune> tunes = new ArrayList<>();

        while (cursor.moveToNext()) {
            String id = cursor.getString(RingtoneManager.ID_COLUMN_INDEX);
            String ringtoneTitle = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
            String directoryUri = cursor.getString(RingtoneManager.URI_COLUMN_INDEX);
            Tune tune = new Tune(id, ringtoneTitle, directoryUri);
            tunes.add(tune);
        }

        Tune firstTune = tunes.get(0);
        firstTune.setSelected(true);
        tunes.set(0, firstTune);
        return tunes;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case android.R.id.home:
                try {
                    onBack();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return true;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}