package com.example.calendar.entity;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

public class Schedule extends LitePalSupport {

    private int id;

    @Column(unique = true)
    private String calendar;//年月日 2019年3月11日

    private String schedule;//日程

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCalendar() {
        return calendar;
    }

    public void setCalendar(String calendar) {
        this.calendar = calendar;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
}