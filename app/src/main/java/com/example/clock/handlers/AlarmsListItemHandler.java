package com.example.clock.handlers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.example.clock.AlarmFormActivity;
import com.example.clock.models.Alarm;
import com.example.clock.utils.AlarmActions;
import com.example.clock.utils.RequestCodes;

public class AlarmsListItemHandler {
    public void onShowForm(View view, Alarm alarm) {
        Context context = view.getContext();
        Intent intent = new Intent(context, AlarmFormActivity.class);
        intent.putExtra("action", AlarmActions.UPDATE);
        intent.putExtra("alarmId", alarm.getId());
        context.startActivity(intent);
    }
}
