package com.example.clock.models;

public class Tune {
    private String id;
    private String title;
    private String directoryUri;
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Tune(String id, String title, String directoryUri) {
        this.id = id;
        this.title = title;
        this.directoryUri = directoryUri;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirectoryUri() {
        return directoryUri;
    }

    public void setDirectoryUri(String directoryUri) {
        this.directoryUri = directoryUri;
    }
}
