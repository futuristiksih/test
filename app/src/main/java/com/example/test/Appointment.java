package com.example.test;

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
    Spinner spinner;EditText dob,birth_weight,name;String gender,doc_name,doc_email;DatePicker picker;Button next;
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                gender= adapterView.getItemAtPosition(i).toString();
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
                Bundle bundle=new Bundle();
                bundle.putString("name",name.getText().toString());bundle.putString("birth_weight",birth_weight.getText().toString());
                bundle.putString("dob",dob.getText().toString());bundle.putString("gender",gender);
                bundle.putString("doc_name",doc_name);bundle.putString("doc_email",doc_email);

                uploadChildCase uploadChildCase=new uploadChildCase();
                uploadChildCase.setArguments(bundle);
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
        Bundle bundle=new Bundle();doc_name=bundle.getString("doc_name");doc_email=bundle.getString("doc_email");

        name=view.findViewById(R.id.name);dob=view.findViewById(R.id.dob);dob.setEnabled(false);
        birth_weight=view.findViewById(R.id.birth_weight);
        spinner = view.findViewById(R.id.gender);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),R.array.gender,android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
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
