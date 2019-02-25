package com.example.test;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
public class previousappointmentAdapter extends ArrayAdapter<myDoctors> {
    private Context context;private ArrayList<myDoctors> item;
    previousappointmentAdapter(Context context, ArrayList<myDoctors> item){
        super(context, 0, item);this.context = context;this.item = item;
    }
    @Override
    public void add(myDoctors object) {
        item.add(object);super.add(object);
    }
    public int getCount() { return this.item.size(); }
    private static class ViewHolder {
        private TextView child_name, appointment_date,status, doctor,ap_id;

    }
    @SuppressLint("ViewHolder") @NonNull @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflator = ((Activity)context).getLayoutInflater();
            convertView = inflator.inflate(R.layout.previous_appointment_list_parent, parent,false);

            holder = new ViewHolder();
            holder.child_name =  convertView.findViewById(R.id.child_name);
            holder.appointment_date =  convertView.findViewById(R.id.appointment_date);
            holder.status =  convertView.findViewById(R.id.status);
            holder.ap_id=convertView.findViewById(R.id.ap_id);
            holder.doctor=convertView.findViewById(R.id.doc_name);
            convertView.setTag(holder);
        }
        else holder = (ViewHolder) convertView.getTag();

        myDoctors doctors = item.get(position);
        holder.child_name.setText("Child Name: "+doctors.getName());
        holder.appointment_date.setText("Appointment Date: "+doctors.getDate());
        holder.doctor.setText("Doctor's Name: "+doctors.getDoc_name());
        holder.status.setText("Status: "+doctors.getStatus());
        holder.ap_id.setText("Appointment Id: "+doctors.getId());
        return convertView;
    }
}
