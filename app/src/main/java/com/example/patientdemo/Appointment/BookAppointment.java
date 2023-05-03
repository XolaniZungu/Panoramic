package com.example.patientdemo.Appointment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.patientdemo.Bookings;
import com.example.patientdemo.Confirmation;
import com.example.patientdemo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class BookAppointment extends AppCompatActivity {


    //for making timeslots
    String SelTime;
    String[] Times = {"8:00","9:00","10:00","11:00","12:00","14:00","15:00","16:00","17:00"};

    DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment2);

        String date = getIntent().getExtras().getString("up_date");
        String time = getIntent().getExtras().getString("up_time");
        String description = getIntent().getExtras().getString("up_description");

        TextView editTextDate = findViewById(R.id.editTextDate);
        Spinner spnTime = (Spinner)findViewById(R.id.timer1);
        TextView paragraphBox = findViewById(R.id.remarks);
        Button buttonBook = findViewById(R.id.dr_app_btn);


        Toast.makeText(BookAppointment.this, "Selected: "+date+time+description, Toast.LENGTH_SHORT).show();
        ArrayList<String> availableTimes = new ArrayList<String>(Arrays.asList(Times)); // Create an ArrayList to store available time slots
        ArrayAdapter<String> aAdapt = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,availableTimes); // Use the availableTimes ArrayList to create the adapter
        spnTime.setAdapter(aAdapt);

        if(date != null && time != null && description != null){
            editTextDate.setText(date);
            paragraphBox.setText(description);
        }



        spnTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SelTime = Times[position].toString();
                Toast.makeText(BookAppointment.this, "Selected: "+SelTime, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //making button for selecting date
        final TextView mDisplayDate = (TextView) findViewById(R.id.editTextDate);
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(BookAppointment.this, android.R.style.Theme_Holo_Dialog_MinWidth, mDateSetListener,year,month,day);
                dialog.getDatePicker().setMinDate(cal.getTimeInMillis());

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                String date = month+" /"+dayOfMonth+" /"+year;
                mDisplayDate.setText(date);
            }
        };

        // Retrieve the appointment data from Firebase to check if the selected time is available
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("bookings")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String bookedDate = document.getString("date");
                                String bookedTime = document.getString("time");

                                // Compare the booked time slots with the available time slots
                                if (bookedDate.equals(editTextDate.getText().toString()) && availableTimes.contains(bookedTime)) {
                                    availableTimes.remove(bookedTime);
                                    Times = availableTimes.toArray(new String[0]);
                                }
                            }
                            // Use the updated availableTimes ArrayList to create the adapter for the spinner
                            ArrayAdapter<String> aAdapt = new ArrayAdapter<String>(BookAppointment.this, android.R.layout.simple_spinner_item, availableTimes);
                            spnTime.setAdapter(aAdapt);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        buttonBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dateText = editTextDate.getText().toString();
                // String timeText = editTextTime.getText().toString();
                String paragraphText = paragraphBox.getText().toString();


                if(TextUtils.isEmpty(dateText)){
                    editTextDate.setError("Date is required");
                    return;
                }

                if (TextUtils.isEmpty(paragraphText)){
                    paragraphBox.setError("Please give a description of symptoms");
                    return;
                }
                // Initialize Firestore and create a new collection
                // then we get the current user's ID so we can assign it to the collection we created.
                //Then we create an appointment and save it.
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference bookingsRef = db.collection("bookings");
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DocumentReference newBookingRef = bookingsRef.document(userId);
                Bookings newBooking = new Bookings(dateText, SelTime, paragraphText);
                newBookingRef.set(newBooking)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "Appointment was successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), Confirmation.class);

                                intent.putExtra("date", dateText);
                                intent.putExtra("time", SelTime);
                                Toast.makeText(getApplicationContext(), "Appointment was successful" + SelTime, Toast.LENGTH_SHORT).show();

                                intent.putExtra("description", paragraphText);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Booking save failed
                                Toast.makeText(getApplicationContext(), "Booking save failed", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
};