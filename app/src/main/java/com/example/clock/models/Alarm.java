package com.example.clock.models;

import androidx.annotation.NonNull;

public class Alarm {
    private String title;
    private int[] repetitionDays;
    private String signal;
    private boolean shouldVibrate;

    @NonNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("title: " + title + ", ");
        sb.append("repetitionDays: " + repetitionDays + ", ");
        sb.append("signal: " + signal + ", ");
        sb.append("shouldVibrate: " + shouldVibrate);
        return sb.toString();
    }

    public Alarm() {
        setRepetitionDays(new int[] {});
        setSignal("");
        setShouldVibrate(false);
        setTitle("Мітка");
    }

    public int[] getRepetitionDays() {
        return repetitionDays;
    }

    public void setRepetitionDays(int[] repetitionDays) {
        this.repetitionDays = repetitionDays;
    }

    public String getSignal() {
        return signal;
    }

    public void setSignal(String signal) {
        this.signal = signal;
    }

    public boolean getShouldVibrate() {
        return shouldVibrate;
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
