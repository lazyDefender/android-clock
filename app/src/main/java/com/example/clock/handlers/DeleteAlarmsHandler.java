package com.example.clock.handlers;

import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clock.adapters.DeleteAlarmsListAdapter;
import com.example.clock.databinding.ActivityDeleteAlarmsBinding;
import com.example.clock.models.Alarm;

import java.util.List;

public class DeleteAlarmsHandler {
    public void onDelete(View view, Alarm alarm) {
        View root = DataBindingUtil.findBinding(view).getRoot();
        RecyclerView bn = (RecyclerView) DataBindingUtil.findBinding(root).getRoot().getParent();
        DeleteAlarmsListAdapter adapter = (DeleteAlarmsListAdapter) bn.getAdapter();

        List<Alarm> alarms = adapter.getAlarmsList();
        List<Long> deletedIds = adapter.getDeletedIds();

        long id = alarm.getId();
        deletedIds.add(id);
        int index = alarms.indexOf(alarm);
        alarms.remove(index);

        adapter.notifyItemRemoved(index);
    }
}
