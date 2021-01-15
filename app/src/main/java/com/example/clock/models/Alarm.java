package com.example.clock.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.clock.utils.RandomID;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Alarm {
    private int id;
    private String title;
    private int[] repetitionDays;
    private Tune tune;
    private boolean shouldVibrate;
    private int hour;
    private int min;
    private boolean isActive = true;

    private List<Integer> alarmManagerTaskIds;

    public Alarm() {
        setId(RandomID.generate());
        setRepetitionDays(new int[] {});
        setShouldVibrate(false);
        setTitle("Мітка");
        LocalTime time = LocalTime.now();
        setHour(time.getHour());
        setMin(time.getMinute());
        setTune(new Tune());
        setAlarmManagerTaskIds(new ArrayList<>());
    }

    public Alarm(String title) {
        setTitle(title);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int[] getRepetitionDays() {
        return repetitionDays;
    }

    public void setRepetitionDays(int[] repetitionDays) {
        this.repetitionDays = repetitionDays;
    }

    public boolean getShouldVibrate() {
        return shouldVibrate;
    }

    public Tune getTune() {
        return tune;
    }

    public void setTune(Tune tune) {
        this.tune = tune;
    }

    public void setShouldVibrate(boolean shouldVibrate) {
        this.shouldVibrate = shouldVibrate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<Integer> getAlarmManagerTaskIds() {
        return alarmManagerTaskIds;
    }

    public void setAlarmManagerTaskIds(List<Integer> alarmManagerTaskIds) {
        this.alarmManagerTaskIds = alarmManagerTaskIds;
    }


}
