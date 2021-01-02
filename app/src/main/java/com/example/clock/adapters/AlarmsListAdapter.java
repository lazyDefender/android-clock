package com.example.clock.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clock.DeleteAlarmsActivity;
import com.example.clock.databinding.AlarmsListItemBinding;
import com.example.clock.models.Alarm;

import java.util.List;

public class AlarmsListAdapter extends RecyclerView.Adapter<AlarmsListAdapter.AlarmViewHolder> {
    private List<Alarm> alarmsList;

    public AlarmsListAdapter(List<Alarm> alarmsList) {
        this.alarmsList = alarmsList;
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        AlarmsListItemBinding binding = AlarmsListItemBinding.inflate(
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
        private AlarmsListItemBinding binding;

        public AlarmViewHolder(AlarmsListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Alarm alarm) {
            binding.setAlarm(alarm);
            binding.executePendingBindings();
            binding.alarmListItemContainer.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Context context = view.getContext();

                    Intent intent = new Intent(context, DeleteAlarmsActivity.class);
                    context.startActivity(intent);

                    return true;
                }
            });
        }

    }

}
