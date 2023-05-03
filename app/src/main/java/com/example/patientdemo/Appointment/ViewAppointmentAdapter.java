package com.example.patientdemo.Appointment;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.patientdemo.R;

import java.util.ArrayList;

public class ViewAppointmentAdapter extends RecyclerView.Adapter<ViewAppointmentAdapter.MyViewHolder> {

    Context context;
    ArrayList<patientappointment> userArrayList;

    public ViewAppointmentAdapter(Context context, ArrayList<patientappointment> userArrayList) {
        this.context = context;
        this.userArrayList = userArrayList;
    }

    @NonNull
    @Override
    public ViewAppointmentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

       View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewAppointmentAdapter.MyViewHolder holder, int position) {

        patientappointment patientappointment= userArrayList.get(position);
        holder.date.setText(patientappointment.date);
        holder.time.setText(patientappointment.time);
        holder.paragraph.setText(patientappointment.paragraph);

        holder.reschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, com.example.patientdemo.Appointment.BookAppointment.class);

                intent.putExtra("up_time", patientappointment.getTime());
                intent.putExtra("up_date", patientappointment.getDate());
                intent.putExtra("up_description", patientappointment.getParagraph());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }


    public static class MyViewHolder extends  RecyclerView.ViewHolder{
        TextView date, paragraph, time;
        Button del, reschedule;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            date=itemView.findViewById(R.id.app_dr_date);
            time=itemView.findViewById(R.id.app_dr_time);
            paragraph=itemView.findViewById(R.id.app_dr_symptoms);

            del = itemView.findViewById(R.id.delete);
            reschedule = itemView.findViewById(R.id.reschedule);
        }
    }
}
