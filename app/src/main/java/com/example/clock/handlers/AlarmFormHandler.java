package com.example.clock.handlers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.example.clock.databinding.ActivityAlarmFormBinding;
import com.example.clock.models.Alarm;

import java.util.ArrayList;
import java.util.Arrays;
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
        final boolean[] checkedItems = {false, false, false, false, false, false, false };
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder
                .setTitle("Повторювати")
                .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int[] repetitionDays = getRepetitionDaysFromCheckedItems(checkedItems);
                        String signal = binding.getAlarm().getSignal();
                        String title = binding.getAlarm().getTitle();
                        Boolean shouldVibrate = binding.getAlarm().getShouldVibrate();
                        Alarm updatedAlarm = new Alarm();
                        updatedAlarm.setRepetitionDays(repetitionDays);
                        updatedAlarm.setSignal(signal);
                        updatedAlarm.setTitle(title);
                        updatedAlarm.setShouldVibrate(shouldVibrate);
                        binding.setAlarm(updatedAlarm);
                    }
                })
                .setMultiChoiceItems(items, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which, boolean checked) {
                        checkedItems[which] = checked;
                    }
                })
        ;
        builder.show();
    }
    
}
