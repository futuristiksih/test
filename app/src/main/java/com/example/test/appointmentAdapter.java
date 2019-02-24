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
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public class appointmentAdapter extends ArrayAdapter<myPatients> {
    private Context context;private ArrayList<myPatients> item;
    appointmentAdapter(Context context, ArrayList<myPatients> item){
        super(context, 0, item);this.context = context;this.item = item;
    }
    @Override
    public void add(myPatients object) {
        item.add(object);super.add(object);
    }
    public int getCount() { return this.item.size(); }
    private static class ViewHolder {
        private TextView child_name, appointment_date,status, guardian,ap_id;

    }
    @SuppressLint("ViewHolder") @NonNull @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflator = ((Activity)context).getLayoutInflater();
            convertView = inflator.inflate(R.layout.appointment_list, parent,false);

            holder = new ViewHolder();
            holder.child_name =  convertView.findViewById(R.id.child_name);
            holder.appointment_date =  convertView.findViewById(R.id.appointment_date);
            holder.status =  convertView.findViewById(R.id.status);
            holder.ap_id=convertView.findViewById(R.id.ap_id);
            holder.guardian=convertView.findViewById(R.id.guardian);
            convertView.setTag(holder);
        }
        else holder = (ViewHolder) convertView.getTag();

        myPatients patients = item.get(position);
        holder.child_name.setText("Child Name: "+patients.getName());
        holder.appointment_date.setText("Appointment Date: "+patients.getDate());
        holder.guardian.setText("Guardian's Name: "+patients.getGuardian());
        holder.status.setText("Status: "+patients.getStatus());
        holder.ap_id.setText("Appointment Id: "+patients.getId());
        return convertView;
    }
}
