package com.example.patientdemo.Appointment;

public class patientappointment {

String date, paragraph, time;


public patientappointment(){}

    public patientappointment(String date, String paragraph, String time) {
        this.date = date;
        this.paragraph = paragraph;
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getParagraph() {
        return paragraph;
    }

    public void setParagraph(String paragraph) {
        this.paragraph = paragraph;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
