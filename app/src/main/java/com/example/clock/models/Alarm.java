package com.example.clock.models;

public class Alarm {
    private String title;
    private int[] repetitionDays;
    private String signal;
    private boolean shouldVibrate;

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

    public boolean isShouldVibrate() {
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
