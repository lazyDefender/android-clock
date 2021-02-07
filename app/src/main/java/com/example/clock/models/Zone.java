package com.example.clock.models;

import java.util.Date;

public class Zone {
    private String id;
    private String name;
    private String dateStr;

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String s) {
        this.dateStr = s;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Zone(String id) {
        this.id = id;
    }

    public Zone(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
