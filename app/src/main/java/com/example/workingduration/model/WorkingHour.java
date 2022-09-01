package com.example.workingduration.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class WorkingHour extends RealmObject {
    @PrimaryKey
    private String date;
    private double duration;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }
}
