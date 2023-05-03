package com.example.patientdemo;

public class Bookings {
    private String date;
    private String time;
    private String paragraph;

    public Bookings() {
        // Empty constructor required for Firestore
    }

    public Bookings(String date, String time, String paragraph) {
        this.date = date;
        this.time = time;
        this.paragraph = paragraph;
    }

    // Getters and setters
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getParagraph() {
        return paragraph;
    }

    public void setParagraph(String paragraph) {
        this.paragraph = paragraph;
    }
}
