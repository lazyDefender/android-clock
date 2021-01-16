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

import java.util.ArrayList;
import java.util.List;

public class SelectCustomTuneActivity extends AppCompatActivity {
    ActivitySelectCustomTuneBinding activitySelectCustomTuneBinding;
    private CustomTunesListAdapter adapter;
    private CustomTuneHandler handler;
    private int alarmId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 999);

        activitySelectCustomTuneBinding = DataBindingUtil.setContentView(this, R.layout.activity_select_custom_tune);

        setSupportActionBar(activitySelectCustomTuneBinding.toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        activitySelectCustomTuneBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        activitySelectCustomTuneBinding.recyclerView.setNestedScrollingEnabled(false);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        List<Tune> tunesList = new ArrayList<>();
        activitySelectCustomTuneBinding.setTunes(tunesList);
        handler = new CustomTuneHandler(activitySelectCustomTuneBinding, 0);
        adapter = new CustomTunesListAdapter(activitySelectCustomTuneBinding.getTunes(), handler);
        activitySelectCustomTuneBinding.recyclerView.setAdapter(adapter);

        activitySelectCustomTuneBinding.setHandler(handler);
    }
}