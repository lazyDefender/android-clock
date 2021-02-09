package com.example.clock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.clock.adapters.TunesListAdapter;
import com.example.clock.databinding.ActivitySelectTuneBinding;
import com.example.clock.handlers.TuneHandler;
import com.example.clock.models.Alarm;
import com.example.clock.models.Tune;
import com.example.clock.repos.AlarmRepo;
import com.example.clock.repos.TuneRepo;
import com.example.clock.utils.RequestCodes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SelectTuneActivity extends AppCompatActivity {
    ActivitySelectTuneBinding activitySelectTuneBinding;
    private TunesListAdapter adapter;
    private TuneHandler handler;
    private int alarmId;

    List<Tune> tunesList;

    public int getSelectedTuneIndex() {
        return selectedTuneIndex;
    }

    public void setSelectedTuneIndex(int selectedTuneIndex) {
        this.selectedTuneIndex = selectedTuneIndex;
    }

    private int selectedTuneIndex = 0;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == RequestCodes.READ_EXTERNAL_STORAGE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        }
        else {
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, RequestCodes.READ_EXTERNAL_STORAGE_PERMISSION);

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
        boolean isCustom = extras.getBoolean("isCustom");

        selectedTune.setCustom(isCustom);
        activitySelectTuneBinding.setTune(selectedTune);

        String selectedTuneId = selectedTune.getId();

        tunesList = TuneRepo.getAlarmTunes(this);

        Tune lastSelected = tunesList.get(selectedTuneIndex);
        lastSelected.setSelected(false);
        tunesList.set(selectedTuneIndex, lastSelected);

        List<String> ids = tunesList
                .stream()
                .map(tune -> tune.getId())
                .collect(Collectors.toList());
        selectedTuneIndex = ids.indexOf(selectedTuneId);

        if(selectedTuneIndex < 0) selectedTuneIndex = 0;

        if(!isCustom) {
            Tune newSelected = tunesList.get(selectedTuneIndex);
            newSelected.setSelected(true);
            tunesList.set(selectedTuneIndex, newSelected);
        }
        else selectedTuneIndex = -1;

        activitySelectTuneBinding.setTunes(tunesList);



        handler = new TuneHandler(activitySelectTuneBinding, selectedTuneIndex) {
            @Override
            public void afterHandle(int selectedTuneIndex) {
                adapter.notifyDataSetChanged();
                setSelectedTuneIndex(selectedTuneIndex);
            }
        };
        adapter = new TunesListAdapter(activitySelectTuneBinding.getTunes(), handler);
        activitySelectTuneBinding.recyclerView.setAdapter(adapter);

        activitySelectTuneBinding.setHandler(handler);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(requestCode == RequestCodes.SHOW_CUSTOM_TUNE_SELECT && resultCode == Activity.RESULT_OK) {
            Bundle extras = intent.getExtras();
            Object tuneObject = extras.get("tune");
            Tune tune = tuneObject != null ?
                    (Tune) tuneObject :
                    null;
            boolean isCustom = extras.getBoolean("isCustom");

            boolean wasTuneChanged = extras.getBoolean("wasTuneChanged");
            if(wasTuneChanged) {
                tune.setCustom(isCustom);
                if(selectedTuneIndex > -1) {
                    Tune prevSelected = tunesList.get(selectedTuneIndex);
                    prevSelected.setSelected(false);
                }
                adapter.notifyItemChanged(selectedTuneIndex);
                activitySelectTuneBinding.setTune(tune);
            }

            new String();
        }
    }

    public void onBack() throws IOException, ClassNotFoundException {
        List<Tune> tunes = activitySelectTuneBinding.getTunes();

        Tune currentTune = activitySelectTuneBinding.getTune();
        handler.onBack(this, tunes, currentTune);
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