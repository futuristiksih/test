package com.example.test;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;
public class view_details_parent extends Fragment {
    FirebaseFirestore db;String doc_email,doc_uid,child_name,id;RadioButton male,female;Button chat,diagnosis;
    EditText immunization,bowel_movement,fever,inception,infected_area,intake,environment,crying,name,dob,birth_weight;CheckBox breast_feedC,vomitC,dehydrationC;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_details_doctor, container, false);getActivity().setTitle("REPORT DETAILS");
        immunization=view.findViewById(R.id.immunization);bowel_movement=view.findViewById(R.id.bowel_movement);
        fever=view.findViewById(R.id.fever);inception=view.findViewById(R.id.inception);infected_area=view.findViewById(R.id.infected_area);
        intake=view.findViewById(R.id.intake);environment=view.findViewById(R.id.environment);crying=view.findViewById(R.id.crying);
        breast_feedC=view.findViewById(R.id.breast_feed);vomitC=view.findViewById(R.id.vomit);dehydrationC=view.findViewById(R.id.dehydration);
        dob=view.findViewById(R.id.dob);birth_weight=view.findViewById(R.id.birth_weight);name=view.findViewById(R.id.child_name);
        male=view.findViewById(R.id.male);female=view.findViewById(R.id.female);
        chat=view.findViewById(R.id.btn_chat);diagnosis=view.findViewById(R.id.btn_diagnosis);

        immunization.setEnabled(false);bowel_movement.setEnabled(false);fever.setEnabled(false);inception.setEnabled(false);infected_area.setEnabled(false);
        intake.setEnabled(false);environment.setEnabled(false);crying.setEnabled(false);breast_feedC.setEnabled(false);vomitC.setEnabled(false);dehydrationC.setEnabled(false);
        dob.setEnabled(false);birth_weight.setEnabled(false);name.setEnabled(false);male.setEnabled(false);female.setEnabled(false);

        Bundle bundle=getArguments();doc_email=bundle.getString("email");child_name=bundle.getString("name");id=bundle.getString("id");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();assert user != null;
        final String email = user.getEmail();db=FirebaseFirestore.getInstance();
        db.collection("Email").document("parent "+email).collection("sent_appointments").document(child_name+" "+doc_email).collection("Dates").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                db.collection("Email").document("parent "+email).collection("sent_appointments").document(child_name+" "+doc_email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        dob.setText(documentSnapshot.get("dob").toString());name.setText(child_name);birth_weight.setText(documentSnapshot.get("birth_weight").toString());
                        if(documentSnapshot.get("gender").toString().equals("Male")) male.setChecked(true);
                        else female.setChecked(true);
                    }
                });
                Map<String,Object> diagnosis=(Map<String, Object>)documentSnapshot.get("diagnosis");
                Map<String,Object> child_details=(Map<String, Object>) diagnosis.get("child_details");
               childDetails details=new childDetails(child_details.get("immunization").toString(),child_details.get("bowel_movement").toString(),child_details.get("fever").toString(),child_details.get("inception").toString(),child_details.get("infected_area").toString(),child_details.get("intake").toString(),child_details.get("environment").toString(),child_details.get("crying").toString(),child_details.get("vomit").toString(),child_details.get("breast_feed").toString(),child_details.get("dehydration").toString());
                immunization.setText(details.getImmunization());bowel_movement.setText(details.getBowel_movement());fever.setText(details.getFever());
                inception.setText(details.getInception());infected_area.setText(details.getInfected_area());intake.setText(details.getIntake());
                environment.setText(details.getEnvironment());crying.setText(details.getCrying());
                if(details.getBreast_feed().equals("tnarue")) breast_feedC.setChecked(true);
                if(details.getVomit().equals("true"))vomitC.setChecked(true);
                if(details.getDehydration().equals("true"))dehydrationC.setChecked(true);
            }
        });
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chatIntent = new Intent(getActivity(), ChatActivity.class);
                chatIntent.putExtra("user_id","ZWvTV9b4oZeSmgkgyx1lpKbZpLN2");//doctor uid
                chatIntent.putExtra("user_name", doc_email);
                startActivity(chatIntent);
            }
        });
        diagnosis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return view;
    }

}
