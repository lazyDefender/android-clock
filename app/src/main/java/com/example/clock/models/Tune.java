package com.example.clock.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Tune implements Parcelable {
    private String id;
    private String title;
    private String directoryUri;
    private boolean isSelected;
    private boolean isCustom;

    public boolean isCustom() {
        return isCustom;
    }

    public void setCustom(boolean custom) {
        isCustom = custom;
    }



    public static final Creator<Tune> CREATOR = new Creator<Tune>() {
        @Override
        public Tune createFromParcel(Parcel in) {
            return new Tune(in);
        }

        @Override
        public Tune[] newArray(int size) {
            return new Tune[size];
        }
    };

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Tune() {

    }

    public Tune(String id, String title, String directoryUri) {
        this.id = id;
        this.title = title;
        this.directoryUri = directoryUri;
    }

    public Tune(String title, String directoryUri) {
        this.id = "";
        this.title = title;
        this.directoryUri = directoryUri;
    }

    public Tune(Parcel parcel) {
        id = parcel.readString();
        title = parcel.readString();
        directoryUri = parcel.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(directoryUri);
    }
}
