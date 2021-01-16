package com.example.clock.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clock.databinding.CustomTunesListItemBinding;
import com.example.clock.databinding.TunesListItemBinding;
import com.example.clock.handlers.CustomTuneHandler;
import com.example.clock.handlers.TuneHandler;
import com.example.clock.models.Tune;

import java.util.List;

public class CustomTunesListAdapter extends RecyclerView.Adapter<CustomTunesListAdapter.TuneViewHolder> {
    private List<Tune> tunesList;
    private CustomTuneHandler tuneHandler;

    public CustomTunesListAdapter(List<Tune> tunesList, CustomTuneHandler tuneHandler) {

        this.tunesList = tunesList;
        this.tuneHandler = tuneHandler;
    }

    @NonNull
    @Override
    public TuneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        CustomTunesListItemBinding binding = CustomTunesListItemBinding.inflate(
                inflater,
                parent,
                false
        );
        return new TuneViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TuneViewHolder holder, int position) {
        Tune tune = tunesList.get(position);
        holder.bind(tune, tuneHandler);
    }

    @Override
    public int getItemCount() {
        return tunesList.size();
    }

    class TuneViewHolder extends RecyclerView.ViewHolder {
        private CustomTunesListItemBinding binding;


        public TuneViewHolder(CustomTunesListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Tune tune, CustomTuneHandler tuneHandler) {
            binding.setTune(tune);
            binding.setTuneHandler(tuneHandler);
            binding.executePendingBindings();
        }

    }

}
