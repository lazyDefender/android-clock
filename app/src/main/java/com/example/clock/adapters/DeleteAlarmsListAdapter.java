package com.example.clock.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clock.databinding.DeleteAlarmsListItemBinding;
import com.example.clock.handlers.DeleteAlarmsHandler;
import com.example.clock.models.Alarm;

import java.util.ArrayList;
import java.util.List;

public class DeleteAlarmsListAdapter extends RecyclerView.Adapter<DeleteAlarmsListAdapter.AlarmViewHolder> {


    private List<Alarm> alarmsList;
    private List<Integer> deletedIds;


    public List<Alarm> getAlarmsList() {
        return alarmsList;
    }

    public void setAlarmsList(List<Alarm> alarmsList) {
        this.alarmsList = alarmsList;
    }

    public List<Integer> getDeletedIds() {
        return deletedIds;
    }

    public void setDeletedIds(List<Integer> deletedIds) {
        this.deletedIds = deletedIds;
    }

    public DeleteAlarmsListAdapter(List<Alarm> alarmsList) {
        this.alarmsList = alarmsList;
        this.deletedIds = new ArrayList<>();
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        DeleteAlarmsListItemBinding binding = DeleteAlarmsListItemBinding.inflate(
                inflater,
                parent,
                false
        );
        return new AlarmViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {
        Alarm alarm = alarmsList.get(position);
        holder.bind(alarm);
    }

    @Override
    public int getItemCount() {
        return alarmsList.size();
    }

    class AlarmViewHolder extends RecyclerView.ViewHolder {
        private DeleteAlarmsListItemBinding binding;
        DeleteAlarmsHandler handler;

        public AlarmViewHolder(DeleteAlarmsListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Alarm alarm) {
            binding.setAlarm(alarm);
            binding.executePendingBindings();
            handler = new DeleteAlarmsHandler();
            binding.setDeleteAlarmsHandler(handler);

        }

    }

}
