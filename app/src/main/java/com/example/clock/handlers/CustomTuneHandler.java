package com.example.clock.handlers;

import android.app.Activity;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.view.View;

import com.example.clock.databinding.ActivitySelectCustomTuneBinding;
import com.example.clock.models.Tune;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class CustomTuneHandler {
    int selectedTuneIndex;
    private ActivitySelectCustomTuneBinding binding;
    private Ringtone ringtone;

    public CustomTuneHandler(ActivitySelectCustomTuneBinding binding, int i) {
        this.binding = binding;
        selectedTuneIndex = i;
    }

    public void onSelect(View view, Tune tune) {
        if(ringtone != null && ringtone.isPlaying())
        ringtone.stop();

        List<Tune> tunesList = binding.getTunes();
        int index = tunesList.indexOf(tune);

        int prevTuneIndex = selectedTuneIndex;
        if(selectedTuneIndex > -1) {
            Tune currentSelectedTune = tunesList.get(selectedTuneIndex);
            currentSelectedTune.setSelected(false);
            tunesList.set(selectedTuneIndex, currentSelectedTune);
        }
        tune.setSelected(true);
        tunesList.set(index, tune);
        selectedTuneIndex = index;

        afterHandle(prevTuneIndex, index);

        Tune selectedTune = tunesList.get(index);
        Uri uri = Uri.parse(selectedTune.getDirectoryUri());
        ringtone = RingtoneManager.getRingtone(view.getContext(), uri);
        ringtone.play();

    }

    public abstract void afterHandle(int prevTuneIndex, int newTuneIndex);

    public void onBack(Activity activity, List<Tune> tunes, String defaultTuneId) {
        if(ringtone != null && ringtone.isPlaying())
        ringtone.stop();
        Tune selectedTune = tunes
                .stream()
                .filter(tune -> tune.isSelected())
                .findFirst()
                .orElse(null);
        Intent intent = new Intent();

        if(selectedTune != null) {
            boolean wasTuneChanged = !selectedTune.getId().equals(defaultTuneId);
            selectedTune.setCustom(true);
            intent.putExtra("tune", selectedTune);
            intent.putExtra("isCustom", true);
            intent.putExtra("wasTuneChanged", wasTuneChanged);
        }
        else {
            intent.putExtra("wasTuneChanged", false);
        }


        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }
}
