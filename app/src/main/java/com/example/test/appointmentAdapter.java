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

public class appointmentAdapter extends ArrayAdapter<doclistdesign> {
    private Context context;private ArrayList<doclistdesign> item;
    appointmentAdapter(Context context, ArrayList<doclistdesign> item){
        super(context, 0, item);this.context = context;this.item = item;
    }
    @Override
    public void add(doclistdesign object) {
        item.add(object);super.add(object);
    }
    public int getCount() { return this.item.size(); }
    private static class ViewHolder {
        private TextView child_name, appointment_date,status, guardian;

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

            holder.guardian=convertView.findViewById(R.id.guardian);
            convertView.setTag(holder);
        }
        else holder = (ViewHolder) convertView.getTag();

        doclistdesign doclistdesignitem = item.get(position);
        holder.child_name.setText(doclistdesignitem.getName());

        holder.appointment_date.setText(doclistdesignitem.getExp_yrs()+" years of experience");
        holder.guardian.setText(doclistdesignitem.getDegree());
        holder.status.setText(doclistdesignitem.getCity());
        return convertView;
    }
}
