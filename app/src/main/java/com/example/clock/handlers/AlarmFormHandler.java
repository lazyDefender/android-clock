package com.example.clock.handlers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.example.clock.SelectSignalActivity;
import com.example.clock.databinding.ActivityAlarmFormBinding;
import com.example.clock.models.Alarm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Map<String, String> getAlarmRingtones(Context context) {
        RingtoneManager manager = new RingtoneManager(context);
        manager.setType(RingtoneManager.TYPE_ALARM);
        Cursor cursor = manager.getCursor();

        Map<String, String> list = new HashMap<>();
        while (cursor.moveToNext()) {
            String id = cursor.getString(RingtoneManager.ID_COLUMN_INDEX);
            String notificationTitle = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
            String notificationUri = cursor.getString(RingtoneManager.URI_COLUMN_INDEX);

            list.put(notificationTitle, notificationUri + '/' + id);
        }

        return list;
    }

    public void showSignalSelect(final View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, SelectSignalActivity.class);
        context.startActivity(intent);
    }
}
