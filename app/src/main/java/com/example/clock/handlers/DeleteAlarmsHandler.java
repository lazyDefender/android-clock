package com.example.clock.handlers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clock.AlarmRepo;
import com.example.clock.adapters.DeleteAlarmsListAdapter;
import com.example.clock.databinding.ActivityDeleteAlarmsBinding;
import com.example.clock.models.Alarm;

import java.io.IOException;
import java.util.List;

public class DeleteAlarmsHandler {
    private DeleteAlarmsListAdapter adapter;

    public void onDelete(View view, Alarm alarm) {
        View root = DataBindingUtil.findBinding(view).getRoot();
        RecyclerView recycler = (RecyclerView) DataBindingUtil.findBinding(root).getRoot().getParent();
        adapter = (DeleteAlarmsListAdapter) recycler.getAdapter();

        List<Alarm> alarms = adapter.getAlarmsList();
        List<Long> deletedIds = adapter.getDeletedIds();

        long id = alarm.getId();
        deletedIds.add(id);
        int index = alarms.indexOf(alarm);
        alarms.remove(index);

        adapter.notifyItemRemoved(index);
    }

    public void onSubmit(Context context, DeleteAlarmsListAdapter adapter, AlarmManager manager) throws IOException, ClassNotFoundException {
            List<Long> deletedIds = adapter.getDeletedIds();


            for(Long id : deletedIds) {

                Alarm alarm = AlarmRepo.findById(context, id);

                if(alarm != null) {
                    for(Long taskId : alarm.getAlarmManagerTaskIds()) {
                        PendingIntent pendingIntent = AlarmRepo.createAlarmPendingIntent(context, alarm, taskId);
                        manager.cancel(pendingIntent);
                    }
                }
                AlarmRepo.delete(context, id);
            }
            new String();
    }
}
