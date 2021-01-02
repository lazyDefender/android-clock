package com.example.clock.handlers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.example.clock.AlarmFormActivity;
import com.example.clock.utils.RequestCodes;

public class AlarmHandler {
    public void onAdd(View view) {
        Context context = view.getContext();
        Activity currentActivity = (Activity) context;
        Intent intent = new Intent(context, AlarmFormActivity.class);
        currentActivity.startActivityForResult(intent, RequestCodes.SHOW_ALARM_FORM);
    }

}
