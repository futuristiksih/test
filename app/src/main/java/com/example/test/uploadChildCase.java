package com.example.test;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
public class uploadChildCase extends Fragment {
    EditText immunization,bowel_movement,fever,inception,infected_area,intake,environment,crying;Button next;
    CheckBox breast_feedC,vomitC,dehydrationC;String doc_name,doc_email,dob,birth_weight,child_name,gender;
    String vomit="false",breast_feed="false",dehydration="false";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.upload, container, false);
        getActivity().setTitle("SYMPTOMS");

        Bundle args=getArguments();
        if(args!=null) {
            doc_name = args.getString("doc_name");
            doc_email = args.getString("doc_email");
            birth_weight = args.getString("birth_weight");
            dob = args.getString("dob");
            child_name = args.getString("child_name");
            gender = args.getString("gender");
        }
        immunization=view.findViewById(R.id.immunization);bowel_movement=view.findViewById(R.id.bowel_movement);
        fever=view.findViewById(R.id.fever);inception=view.findViewById(R.id.inception);infected_area=view.findViewById(R.id.infected_area);
        intake=view.findViewById(R.id.intake);environment=view.findViewById(R.id.environment);crying=view.findViewById(R.id.crying);

        breast_feedC=view.findViewById(R.id.breast_feed);vomitC=view.findViewById(R.id.vomit);dehydrationC=view.findViewById(R.id.dehydration);
        next=view.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(vomitC.isChecked()) vomit="true";if(dehydrationC.isChecked())dehydration="true";if(breast_feedC.isChecked())breast_feed="true";

                Bundle bundle=new Bundle();
                bundle.putString("doc_name",doc_name);bundle.putString("doc_email",doc_email);bundle.putString("birth_weight",birth_weight);
                bundle.putString("child_name",child_name);bundle.putString("dob",dob);bundle.putString("gender",gender);

                bundle.putString("breast_feed",breast_feed);bundle.putString("crying",crying.getText().toString().trim());
                bundle.putString("vomit",vomit);bundle.putString("dehydration",dehydration);bundle.putString("environment",environment.getText().toString().trim());
                bundle.putString("immunization",immunization.getText().toString().trim());bundle.putString("intake",intake.getText().toString().trim());
                bundle.putString("bowel_movement",bowel_movement.getText().toString().trim());bundle.putString("fever",fever.getText().toString().trim());
                bundle.putString("inception",inception.getText().toString().trim());bundle.putString("infected_area",infected_area.getText().toString().trim());
                Intent i=new Intent(getActivity(),multi_upload.class);
                i.putExtra("bundle",bundle);startActivity(i);
            }
        });

        return view;
    }

}
