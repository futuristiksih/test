package com.example.test;


import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class uploadChildCase extends Fragment {
    LinearLayout symptom,disease;Button upload,back;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.upload, container, false);
        getActivity().setTitle("DISEASE DETAILS");
        symptom=view.findViewById(R.id.symptom);
        disease=view.findViewById(R.id.disease);
        upload=view.findViewById(R.id.upload);
        back=view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                ((ConstraintLayout)getActivity().findViewById(R.id.up)).removeAllViews();
                fragmentManager.beginTransaction().replace(R.id.up, new Appointment()).commit();
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int a=symptom.getChildCount();
                ArrayList<String> symptoms=new ArrayList<>();
                for(int i=0;i<a;i++){
                    CheckBox checkBox=(CheckBox)symptom.getChildAt(i);
                    if(checkBox.isChecked()) symptoms.add(checkBox.getText().toString());
                }
                final int b=disease.getChildCount();
                ArrayList<String> diseases=new ArrayList<>();
                for(int i=0;i<b;i++){
                    CheckBox checkBox=(CheckBox)disease.getChildAt(i);
                    if(checkBox.isChecked()) diseases.add(checkBox.getText().toString());
                }
            }
        });

        return view;
    }

}
