package com.example.clock;

import android.os.Bundle;

import com.example.clock.adapters.DeleteAlarmsListAdapter;
import com.example.clock.databinding.ActivityDeleteAlarmsBinding;
import com.example.clock.handlers.DeleteAlarmsHandler;
import com.example.clock.models.Alarm;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.View;
import android.view.WindowManager;

import java.io.IOException;
import java.util.List;

public class DeleteAlarmsActivity extends AppCompatActivity {
    ActivityDeleteAlarmsBinding activityDeleteAlarmsBinding;
    DeleteAlarmsListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDeleteAlarmsBinding = DataBindingUtil.setContentView(this, R.layout.activity_delete_alarms);

        setSupportActionBar(activityDeleteAlarmsBinding.toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        try {
            List<Alarm> alarms = AlarmRepo.findAll(this);
            adapter = new DeleteAlarmsListAdapter(alarms);
            activityDeleteAlarmsBinding.setAdapter(adapter);
            activityDeleteAlarmsBinding.deleteAlarmsList.setNestedScrollingEnabled(false);
            activityDeleteAlarmsBinding.deleteAlarmsList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}