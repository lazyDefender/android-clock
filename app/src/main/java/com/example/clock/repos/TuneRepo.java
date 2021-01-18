package com.example.clock.repos;

import android.content.Context;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.clock.models.Tune;

import java.util.ArrayList;
import java.util.List;

public class TuneRepo {
    public static List<Tune> getAlarmTunes(Context context) {
        RingtoneManager manager = new RingtoneManager(context);
        manager.setType(RingtoneManager.TYPE_ALARM);
        Cursor cursor = manager.getCursor();
        List<Tune> tunes = new ArrayList<>();

        while (cursor.moveToNext()) {
            String id = cursor.getString(RingtoneManager.ID_COLUMN_INDEX);
            String ringtoneTitle = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
            String directoryUri = cursor.getString(RingtoneManager.URI_COLUMN_INDEX);
            Tune tune = new Tune(id, ringtoneTitle, directoryUri);
            tunes.add(tune);
        }

        return tunes;
    }

    public static List<Tune> getCustomTunes(Context context) {
        Uri collection = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
        };
        String sortOrder = MediaStore.Audio.Media.DISPLAY_NAME + " ASC";
        Cursor cursor = context.getContentResolver().query(
                collection,
                projection,
                null,
                null,
                sortOrder
        );
        List<Tune> tunes = new ArrayList<>();
        while (cursor.moveToNext()) {
            int idIndex = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int titleIndex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int dataIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            String id = cursor.getString(idIndex);
            String title = cursor.getString(titleIndex);
            String data = cursor.getString(dataIndex);
            tunes.add(new Tune(id, title, data));
        }

        return tunes;
    }
}
