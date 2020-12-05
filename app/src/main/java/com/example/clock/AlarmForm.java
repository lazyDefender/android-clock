package com.example.clock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.clock.databinding.ActivityAlarmFormBinding;

public class AlarmForm extends AppCompatActivity {

    ActivityAlarmFormBinding activityAlarmFormBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityAlarmFormBinding = DataBindingUtil.setContentView(this, R.layout.activity_alarm_form);

        setSupportActionBar(activityAlarmFormBinding.toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case android.R.id.home:
                Toast.makeText(this, "sure?", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
        return true;
    }
}