package com.example.clock.ui.alarm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clock.R;
import com.example.clock.databinding.AlarmsListItemBinding;
import com.example.clock.models.Alarm;

import java.util.List;

public class AlarmsListAdapter extends RecyclerView.Adapter<AlarmsListAdapter.ViewHolder> {
    private List<Alarm> alarms;

    public AlarmsListAdapter(List<Alarm> _alarms) {
        alarms = _alarms;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        AlarmsListItemBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.alarms_list_item,
                parent,
                false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        holder.binding.textView.setText(alarms.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return alarms.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private AlarmsListItemBinding binding;

        public ViewHolder(@NonNull AlarmsListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
