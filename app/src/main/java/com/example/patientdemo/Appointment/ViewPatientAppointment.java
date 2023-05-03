package com.example.patientdemo.Appointment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.patientdemo.PatientDashboard;
import com.example.patientdemo.R;
import com.example.patientdemo.UpdateProfilePatient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ViewPatientAppointment extends AppCompatActivity {
    DrawerLayout drawerLayout;
    TextView nav_name;
    RecyclerView recyclerView;
    ArrayList<patientappointment> userArrayList;
   ViewAppointmentAdapter ViewAppointmentAdapter;
   FirebaseFirestore db;

   ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patient_appointment);

        //ProgressDialog progressDialog = new ProgressDialog(this);
       // progressDialog.setCancelable(false);
       // progressDialog.setMessage("Fetching your appointment");
        //progressDialog.show();
        drawerLayout = findViewById(R.id.drawer_layout);
        nav_name = findViewById(R.id.nav_name);

        recyclerView= findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        db = FirebaseFirestore.getInstance();
        userArrayList= new ArrayList<patientappointment>();


     ViewAppointmentAdapter = new ViewAppointmentAdapter(ViewPatientAppointment.this,userArrayList);

       recyclerView.setAdapter(ViewAppointmentAdapter);


     EventChangeListener();
    }

    private void EventChangeListener() {

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
       db.collection("bookings")
               .addSnapshotListener(new EventListener<QuerySnapshot>() {
                   @Override
                   public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                      if (error!=null){
                          Log.e("Firestore error",error.getMessage());
                          return;
                                 }
                      for (DocumentChange dc: value.getDocumentChanges()){
                          if (dc.getType() ==DocumentChange.Type.ADDED){
                              userArrayList.add(dc.getDocument().toObject(patientappointment.class));
                          }
                          ViewAppointmentAdapter.notifyDataSetChanged();
                      }
                   }

               });
    }
    // ---------------------------- NAVIGATION BAR ---------------------------- //
    public void ClickMenu(View view) {
        PatientDashboard.openDrawer(drawerLayout);
    }

    public void ClickLogo(View view) {
        PatientDashboard.closeDrawer(drawerLayout);
    }

    public void ClickHome(View view) {
        PatientDashboard.redirectActivity(this, PatientDashboard.class);
    }

    public void ClickUpdateProfile(View view) {
        PatientDashboard.redirectActivity(this, UpdateProfilePatient.class);
    }

    public void ClickLogout(View view) {
        PatientDashboard.logout(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        PatientDashboard.closeDrawer(drawerLayout);
    }
    // ----------------------------------------------------------------------- //
}




