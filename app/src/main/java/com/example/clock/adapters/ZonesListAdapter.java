package com.example.clock.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clock.databinding.ZonesListItemBinding;
import com.example.clock.models.Zone;

import java.util.List;

public class ZonesListAdapter extends RecyclerView.Adapter<ZonesListAdapter.ZoneViewHolder> {
    private Zone[] zones;

    public void setZones(Zone[] zones) {
        this.zones = zones;
        notifyDataSetChanged();
    }

    public ZonesListAdapter(Zone[] zones) {
        this.zones = zones;
    }

    @NonNull
    @Override
    public ZonesListAdapter.ZoneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ZonesListItemBinding binding = ZonesListItemBinding.inflate(
                inflater,
                parent,
                false
        );
        return new ZonesListAdapter.ZoneViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ZonesListAdapter.ZoneViewHolder holder, int position) {
        Zone zone = zones[position];
        holder.bind(zone);
    }

    @Override
    public int getItemCount() {
        return zones.length;
    }

    class ZoneViewHolder extends RecyclerView.ViewHolder {
        private ZonesListItemBinding binding;


        public ZoneViewHolder(ZonesListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Zone zone) {
            binding.setZone(zone);
            binding.executePendingBindings();
        }
    }
}
