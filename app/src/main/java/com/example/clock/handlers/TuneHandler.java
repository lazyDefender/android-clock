package com.example.clock.handlers;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.example.clock.BR;
import com.example.clock.adapters.TunesListAdapter;
import com.example.clock.databinding.ActivitySelectSignalBinding;
import com.example.clock.models.Tune;

import java.util.ArrayList;
import java.util.List;

public abstract class TuneHandler {
    private ActivitySelectSignalBinding activitySelectSignalBinding;
    private int selectedTuneIndex;

    public TuneHandler(ActivitySelectSignalBinding binding, int selectedTuneIndex) {
        this.activitySelectSignalBinding = binding;
        this.selectedTuneIndex = selectedTuneIndex;
    }

    public abstract void afterHandle();

    public void onSelect(View view, Tune tune) {
        List<Tune> tunesList = activitySelectSignalBinding.getTunes();
        int index = tunesList.indexOf(tune);

        Tune currentSelectedTune = tunesList.get(selectedTuneIndex);
        currentSelectedTune.setSelected(false);
        tunesList.set(selectedTuneIndex, currentSelectedTune);

        tune.setSelected(true);
        tunesList.set(index, tune);
        selectedTuneIndex = index;

        activitySelectSignalBinding.setTunes(tunesList);

        afterHandle();

        Tune selectedTune = tunesList.get(index);
        Uri uri = Uri.parse(selectedTune.getDirectoryUri() + '/' + selectedTune.getId());
        Ringtone ringtone = RingtoneManager.getRingtone(view.getContext(), uri);
        ringtone.play();

    }

}
