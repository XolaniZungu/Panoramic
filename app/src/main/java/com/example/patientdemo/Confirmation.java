package com.example.patientdemo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.patientdemo.Appointment.ViewPatientAppointment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Timer;
import java.util.TimerTask;

public class Confirmation extends AppCompatActivity {

    Button Sbtn;
    String sDate;
    EditText details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        sDate = getIntent().getExtras().getString("date");
        String time = getIntent().getExtras().getString("time");
        String description = getIntent().getExtras().getString("description");

        Sbtn = findViewById(R.id.confirm);
        details = findViewById(R.id.details);

        details.setText("Date of Appointment: " +sDate + "\n" +
                "Time of Appointment: " +time + "\n"  +
                "Time of Appointment: " +description );


        Sbtn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ViewPatientAppointment.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}