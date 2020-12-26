package com.example.clock;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.example.clock.databinding.ActivitySelectSignalBinding;

public class SelectSignalActivity extends AppCompatActivity {
    ActivitySelectSignalBinding activitySelectSignalBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySelectSignalBinding = DataBindingUtil.setContentView(this, R.layout.activity_select_signal);
        setSupportActionBar(activitySelectSignalBinding.toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }
}