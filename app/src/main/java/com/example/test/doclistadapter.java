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
public class doclistadapter extends ArrayAdapter<doclistdesign> {
    private Context context;private ArrayList<doclistdesign> item;
    doclistadapter(Context context, ArrayList<doclistdesign> item){
        super(context, 0, item);this.context = context;this.item = item;
    }
    @Override
    public void add(doclistdesign object) {
        item.add(object);super.add(object);
    }
    public int getCount() { return this.item.size(); }
    private static class ViewHolder {
        private TextView name, exp_yrs,city, degree;RatingBar ratingBar;
    }
    @SuppressLint("ViewHolder") @NonNull @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflator = ((Activity)context).getLayoutInflater();
            convertView = inflator.inflate(R.layout.linearlayout_doclist, parent,false);

            holder = new ViewHolder();
            holder.name =  convertView.findViewById(R.id.name);
            holder.exp_yrs =  convertView.findViewById(R.id.expe_yrs);
            holder.degree =  convertView.findViewById(R.id.profession);
            holder.ratingBar = convertView.findViewById(R.id.ratingBar);
            holder.city=convertView.findViewById(R.id.city);
            convertView.setTag(holder);
        }
        else holder = (ViewHolder) convertView.getTag();

        doclistdesign doclistdesignitem = item.get(position);
        holder.name.setText(doclistdesignitem.getName());
        holder.ratingBar.setRating(Float.parseFloat(doclistdesignitem.getRating()));
        holder.exp_yrs.setText(doclistdesignitem.getExp_yrs());
        holder.degree.setText(doclistdesignitem.getDegree());
        holder.city.setText(doclistdesignitem.getCity());
        return convertView;
    }
}
