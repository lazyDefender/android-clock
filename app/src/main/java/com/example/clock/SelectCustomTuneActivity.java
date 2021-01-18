package com.example.clock;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import com.example.clock.adapters.CustomTunesListAdapter;
import com.example.clock.adapters.TunesListAdapter;
import com.example.clock.databinding.ActivitySelectCustomTuneBinding;
import com.example.clock.handlers.CustomTuneHandler;
import com.example.clock.handlers.TuneHandler;
import com.example.clock.models.Tune;
import com.example.clock.repos.TuneRepo;
import com.example.clock.utils.RequestCodes;

import java.util.ArrayList;
import java.util.List;

public class SelectCustomTuneActivity extends AppCompatActivity {
    ActivitySelectCustomTuneBinding activitySelectCustomTuneBinding;
    private CustomTunesListAdapter adapter;
    private CustomTuneHandler handler;
    private int alarmId;
    private String defaultTuneId;

    @Override
    public void onBackPressed() {
        handler.onBack(this, adapter.getTunes(), defaultTuneId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, RequestCodes.READ_EXTERNAL_STORAGE_PERMISSION);

        activitySelectCustomTuneBinding = DataBindingUtil.setContentView(this, R.layout.activity_select_custom_tune);

        setSupportActionBar(activitySelectCustomTuneBinding.toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        activitySelectCustomTuneBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        activitySelectCustomTuneBinding.recyclerView.setNestedScrollingEnabled(false);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        Tune defaultTune = (Tune) extras.get("tune");
        boolean isCustom = extras.getBoolean("isCustom");
        defaultTune.setCustom(isCustom);

        defaultTuneId = defaultTune.getId();

        List<Tune> tunesList = TuneRepo.getCustomTunes(this);
        int index = -1;
        if(defaultTune.isCustom()) {
            for(int i = 0; i < tunesList.size(); i++) {
                Tune tune = tunesList.get(i);
                String id = tune.getId();
                if(id.equals(defaultTuneId)) {
                    index = i;
                    tune.setSelected(true);
                    break;
                }
            }
        }

        activitySelectCustomTuneBinding.setTunes(tunesList);
        handler = new CustomTuneHandler(activitySelectCustomTuneBinding, index) {
            @Override
            public void afterHandle(int prevTuneIndex, int newTuneIndex ) {
                adapter.notifyItemChanged(prevTuneIndex);
                adapter.notifyItemChanged(newTuneIndex);
            }
        };
        adapter = new CustomTunesListAdapter(activitySelectCustomTuneBinding.getTunes(), handler);
        activitySelectCustomTuneBinding.recyclerView.setAdapter(adapter);

        activitySelectCustomTuneBinding.setHandler(handler);
    }
}