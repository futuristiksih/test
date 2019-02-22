package com.example.test;


import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class uploadChildCase extends Fragment {
    Button upload,back;String name,birth_weight,dob,gender,doc_name,doc_email;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.upload, container, false);
        getActivity().setTitle("DISEASE DETAILS");
        Bundle bundle=getArguments();
        name=bundle.getString("name");birth_weight=bundle.getString("birth_weight");gender=bundle.getString("gender");
        dob=bundle.getString("dob");doc_name=bundle.getString("doc_name");doc_email=bundle.getString("doc_email");
        Log.i("details",""+name+dob+gender+birth_weight+doc_email+doc_name);

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

            }
        });

        return view;
    }

}
