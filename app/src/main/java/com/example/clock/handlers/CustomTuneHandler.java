package com.example.clock.handlers;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.view.View;

import com.example.clock.databinding.ActivitySelectCustomTuneBinding;
import com.example.clock.models.Tune;
import com.example.clock.utils.AudioFocus;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class CustomTuneHandler {
    int selectedTuneIndex;
    private ActivitySelectCustomTuneBinding binding;
    private MediaPlayer player;
    AudioFocusRequest audioFocusRequest;
    AudioManager audioManager;

    public CustomTuneHandler(ActivitySelectCustomTuneBinding binding, int i) {
        this.binding = binding;
        selectedTuneIndex = i;
    }

    public void onSelect(View view, Tune tune) {
        Context context = view.getContext();
        audioManager = (AudioManager) context.getSystemService(Service.AUDIO_SERVICE);

        if(player == null) player = new MediaPlayer();
        if(player.isPlaying()) player.stop();
        player.reset();

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
        try {
            player.setDataSource(context, uri);
            player.prepare();
            audioFocusRequest = AudioFocus.createRequest(audioManager, player);
            int audioFocusResult = audioManager.requestAudioFocus(audioFocusRequest);
            if (audioFocusResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                player.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch(Exception e) {
            new String();
        }

    }

    public abstract void afterHandle(int prevTuneIndex, int newTuneIndex);

    public void onBack(Activity activity, List<Tune> tunes, String defaultTuneId) {
        if(player != null && player.isPlaying()) player.stop();
        audioManager = (AudioManager) activity.getSystemService(Service.AUDIO_SERVICE);
        if(audioFocusRequest != null) audioManager.abandonAudioFocusRequest(audioFocusRequest);

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
