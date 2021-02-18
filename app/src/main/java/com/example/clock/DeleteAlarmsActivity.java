package com.example.clock;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;

import com.example.clock.adapters.DeleteAlarmsListAdapter;
import com.example.clock.databinding.ActivityDeleteAlarmsBinding;
import com.example.clock.handlers.DeleteAlarmsHandler;
import com.example.clock.models.Alarm;
import com.example.clock.repos.AlarmRepo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DeleteAlarmsActivity extends AppCompatActivity {
    ActivityDeleteAlarmsBinding activityDeleteAlarmsBinding;
    DeleteAlarmsListAdapter adapter;
    DeleteAlarmsHandler handler;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.delete_alarms_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case android.R.id.home:
                Toast.makeText(this, "sure?", Toast.LENGTH_LONG).show();
                return false;
            case R.id.commit:
                AlarmManager manager = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
                try {
                    handler.onSubmit(this, adapter, manager);
                    Intent intent = new Intent();
                    intent.putIntegerArrayListExtra("deletedIds", (ArrayList<Integer>) adapter.getDeletedIds());
                    this.setResult(Activity.RESULT_OK, intent);
                    finish();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDeleteAlarmsBinding = DataBindingUtil.setContentView(this, R.layout.activity_delete_alarms);


        setSupportActionBar(activityDeleteAlarmsBinding.toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        handler = new DeleteAlarmsHandler();
        activityDeleteAlarmsBinding.setDeleteAlarmsHandler(handler);

        try {
            List<Alarm> alarms = AlarmRepo.findAll(this);
            adapter = new DeleteAlarmsListAdapter(alarms);
            activityDeleteAlarmsBinding.deleteAlarmsList.setAdapter(adapter);
            activityDeleteAlarmsBinding.deleteAlarmsList.setNestedScrollingEnabled(false);
            activityDeleteAlarmsBinding.deleteAlarmsList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}