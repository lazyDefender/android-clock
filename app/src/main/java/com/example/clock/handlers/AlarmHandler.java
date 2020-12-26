package com.example.clock.handlers;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.example.clock.AlarmFormActivity;

public class AlarmHandler {
    public void onAdd(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, AlarmFormActivity.class);
        context.startActivity(intent);
    }
}
