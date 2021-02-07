package com.example.clock.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clock.databinding.TunesListItemBinding;
import com.example.clock.handlers.TuneHandler;
import com.example.clock.models.Tune;

import java.util.List;

public class TunesListAdapter extends RecyclerView.Adapter<TunesListAdapter.TuneViewHolder> {
    private List<Tune> tunesList;
    private TuneHandler tuneHandler;

    public TunesListAdapter(List<Tune> tunesList, TuneHandler tuneHandler) {

        this.tunesList = tunesList;
        this.tuneHandler = tuneHandler;
    }

    @NonNull
    @Override
    public TuneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        TunesListItemBinding binding = TunesListItemBinding.inflate(
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
        private TunesListItemBinding binding;


        public TuneViewHolder(TunesListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Tune tune, TuneHandler tuneHandler) {
            binding.setTune(tune);
            binding.setTuneHandler(tuneHandler);
            binding.executePendingBindings();
        }

    }

}
