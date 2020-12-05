package com.example.clock.handlers;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.example.clock.AlarmForm;

public class AlarmHandler {
    public void onAdd(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, AlarmForm.class);
        context.startActivity(intent);
    }
}
