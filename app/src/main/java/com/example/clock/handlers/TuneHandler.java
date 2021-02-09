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
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.example.clock.BR;
import com.example.clock.SelectCustomTuneActivity;
import com.example.clock.adapters.TunesListAdapter;
import com.example.clock.databinding.ActivitySelectTuneBinding;
import com.example.clock.models.Tune;
import com.example.clock.utils.AudioFocus;
import com.example.clock.utils.RequestCodes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class TuneHandler {
    private ActivitySelectTuneBinding activitySelectTuneBinding;
    private int selectedTuneIndex;
    MediaPlayer player;
    AudioFocusRequest audioFocusRequest;
    AudioManager audioManager;

    public TuneHandler(ActivitySelectTuneBinding binding, int selectedTuneIndex) {
        this.activitySelectTuneBinding = binding;
        this.selectedTuneIndex = selectedTuneIndex;
    }

    public abstract void afterHandle(int selectedTuneIndex);

    public void onSelect(View view, Tune tune) {
        Context context = view.getContext();
        audioManager = (AudioManager) context.getSystemService(Service.AUDIO_SERVICE);

        if(player == null) player = new MediaPlayer();

        if(player.isPlaying()) player.stop();
        player.reset();

        List<Tune> tunesList = activitySelectTuneBinding.getTunes();
        int index = tunesList.indexOf(tune);

        if(selectedTuneIndex > -1) {
            Tune currentSelectedTune = tunesList.get(selectedTuneIndex);
            currentSelectedTune.setSelected(false);
            tunesList.set(selectedTuneIndex, currentSelectedTune);
        }

        tune.setSelected(true);
        tunesList.set(index, tune);
        selectedTuneIndex = index;

        activitySelectTuneBinding.setTunes(tunesList);

        afterHandle(selectedTuneIndex);

        Tune selectedTune = tunesList.get(index);
        Uri uri = Uri.parse(selectedTune.getDirectoryUri() + '/' + selectedTune.getId());
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

    public void openSelectCustomTuneActivity(View view, Tune tune) {
        if(player != null && player.isPlaying()) player.stop();
        player.reset();
        Context context =  view.getContext();
        Activity currentActivity = (Activity) context;
        Intent intent = new Intent(context, SelectCustomTuneActivity.class);
        tune.setSelected(true);
        intent.putExtra("tune", tune);
        intent.putExtra("isCustom", tune.isCustom());
        currentActivity.startActivityForResult(intent, RequestCodes.SHOW_CUSTOM_TUNE_SELECT);
    }

    public void onBack(Activity activity, List<Tune> tunes, Tune currentTune) {
        if(player != null && player.isPlaying())
            player.stop();
        audioManager = (AudioManager) activity.getSystemService(Service.AUDIO_SERVICE);
        if(audioFocusRequest != null)
            audioManager.abandonAudioFocusRequest(audioFocusRequest);

        Tune selectedTune = tunes
                .stream()
                .filter(tune -> tune.isSelected())
                .findFirst()
                .orElse(null);
        Intent intent = new Intent();
        if(selectedTune != null) {
            boolean wasTuneChanged = selectedTune.getId() != currentTune.getId();
            selectedTune.setCustom(false);
            intent.putExtra("tune", selectedTune);
            intent.putExtra("wasTuneChanged", wasTuneChanged);

        }
        else {
            intent.putExtra("tune", currentTune);
            intent.putExtra("isCustom", true);
            intent.putExtra("wasTuneChanged", true);
        }

        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }

}
