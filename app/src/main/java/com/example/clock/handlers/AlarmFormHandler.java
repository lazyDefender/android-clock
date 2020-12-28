package com.example.clock.handlers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.example.clock.SelectTuneActivity;
import com.example.clock.databinding.ActivityAlarmFormBinding;
import com.example.clock.models.Alarm;

import java.util.ArrayList;
import java.util.List;

public class AlarmFormHandler {
    ActivityAlarmFormBinding binding;
    private int[] getRepetitionDaysFromCheckedItems(boolean[] checkedItems) {
        List<Integer> days = new ArrayList<>();
        for(int j=0;j<checkedItems.length;j++) {
            if(checkedItems[j]) days.add(j);
        }
        int[] repetitionDays = new int[days.size()];
        for(int j=0;j<days.size();j++) {
            repetitionDays[j] = days.get(j);
        }
        return repetitionDays;
    }

    public void showRepeatDialog(final View view) {
        binding = DataBindingUtil.findBinding(view);

        String[] items = {"Понеділок", "Вівторок", "Середа", "Четвер", "П\'ятниця", "Субота", "Неділя"};
        int[] selectedRepetitionDays = binding.getAlarm().getRepetitionDays();

        final boolean[] checkedItems = {false, false, false, false, false, false, false };
        for(int d : selectedRepetitionDays) {
            checkedItems[d] = true;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder
                .setTitle("Повторювати")
                .setNegativeButton("СКАСУВАТИ", ((dialogInterface, i) -> dialogInterface.dismiss()))
                .setPositiveButton("ОК", (dialogInterface, i) -> {
                    int[] repetitionDays = getRepetitionDaysFromCheckedItems(checkedItems);
                    Alarm alarm = binding.getAlarm();
                    alarm.setRepetitionDays(repetitionDays);
                    binding.setAlarm(alarm);
                })
                .setMultiChoiceItems(items, checkedItems, (dialogInterface, which, checked) -> checkedItems[which] = checked)
        ;
        builder.show();
    }

    public void showSignalSelect(final View view) {
        Context context = view.getContext();
        Activity currentActivity = (Activity) context;
        Intent intent = new Intent(context, SelectTuneActivity.class);
        currentActivity.startActivityForResult(intent, 1);
    }

    public void showTitleDialog(final View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
//        builder
//                .setTitle("Мітка")

    }

}
