package com.example.clock.models;

import androidx.annotation.NonNull;

public class Alarm {
    private String title;
    private int[] repetitionDays;
    private Tune tune;
    private boolean shouldVibrate;

    @NonNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("title: " + title + ", ");
        sb.append("repetitionDays: " + repetitionDays + ", ");
        sb.append("shouldVibrate: " + shouldVibrate);
        return sb.toString();
    }

    public Alarm() {
        setRepetitionDays(new int[] {});
        setShouldVibrate(false);
        setTitle("Мітка");
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

    public Alarm(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
