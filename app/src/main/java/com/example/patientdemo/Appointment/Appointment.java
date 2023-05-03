package com.example.patientdemo.Appointment;

public class Appointment {
    private String name;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    public Appointment() {}

    public Appointment(String name, int year, int month, int day, int hour, int minute) {
        this.name = name;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

    public String getName() {
        return name;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }
}