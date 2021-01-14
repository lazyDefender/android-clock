package com.example.clock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.clock.adapters.TunesListAdapter;
import com.example.clock.databinding.ActivitySelectSignalBinding;
import com.example.clock.handlers.TuneHandler;
import com.example.clock.models.Tune;

import java.util.ArrayList;
import java.util.List;

public class SelectTuneActivity extends AppCompatActivity {
    ActivitySelectSignalBinding activitySelectSignalBinding;
    private TunesListAdapter adapter;
    boolean tuneWasSelected;

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

        activitySelectSignalBinding = DataBindingUtil.setContentView(this, R.layout.activity_select_signal);
        setSupportActionBar(activitySelectSignalBinding.toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);



        activitySelectSignalBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        activitySelectSignalBinding.recyclerView.setAdapter(adapter);
        activitySelectSignalBinding.recyclerView.setNestedScrollingEnabled(false);


        List<Tune> tunesList = getAlarmTunes(this);
        activitySelectSignalBinding.setTunes(tunesList);
        TuneHandler tuneHandler = new TuneHandler(activitySelectSignalBinding, 0) {
            @Override
            public void afterHandle() {
                adapter.notifyDataSetChanged();
            }
        };
        adapter = new TunesListAdapter(activitySelectSignalBinding.getTunes(), tuneHandler);
        activitySelectSignalBinding.recyclerView.setAdapter(adapter);

    }

    public void onBack() {
        List<Tune> tunes = activitySelectSignalBinding.getTunes();
        Tune selectedTune = tunes
                .stream()
                .filter(tune -> tune.isSelected())
                .findFirst()
                .orElse(null);
        Intent intent = new Intent();
        intent.putExtra("tune", selectedTune);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        onBack();
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
                onBack();
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