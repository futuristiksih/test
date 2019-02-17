package com.example.test;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Calendar;

public class Appointment extends Fragment {
    Spinner gender;EditText dob,weight,age,medication;String genderType;DatePicker picker;Button next;
    public void next(View view){
        Intent i=new Intent(getActivity(),uploadChildCase.class);startActivity(i);getActivity().finish();
    }
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                genderType="";
                genderType = adapterView.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dob.setText(getCurrentDate());
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                ((ConstraintLayout)getActivity().findViewById(R.id.cl)).removeAllViews();
                fragmentManager.beginTransaction().replace(R.id.cl, new uploadChildCase()).commit();
            }
        });

    }
    public String getCurrentDate(){
        return ((picker.getMonth() + 1) + "/") +//month is 0 based
                picker.getDayOfMonth() + "/" + picker.getYear();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.appointment, container, false);
        getActivity().setTitle("CHILD DETAILS");
        age=view.findViewById(R.id.age);
        dob=view.findViewById(R.id.dob);dob.setEnabled(false);
        weight=view.findViewById(R.id.weight);
        medication=view.findViewById(R.id.medication);
        gender = view.findViewById(R.id.gender);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),R.array.gender,android.R.layout.simple_spinner_item);
        gender.setAdapter(adapter);
        picker=view.findViewById(R.id.dobspin);
        Calendar c= Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        picker.init(year, month, dayOfMonth, new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(
                            DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        dob.setText(dayOfMonth +"/"+monthOfYear + "/" + year);
                    }
                });
        next=view.findViewById(R.id.next);
        return view;
    }
}
