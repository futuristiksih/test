package com.example.test;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;
public class view_details_parent extends Fragment {
    FirebaseFirestore db;String doc_email,doc_uid,child_name,id;RadioButton male,female;Button chat,pdf,call;
    EditText immunization,bowel_movement,fever,inception,infected_area,intake,environment,crying,name,dob,birth_weight;
    CheckBox breast_feedC,vomitC,dehydrationC;String phone,breastfeed,vomit,dehydration,birthweight,gender,Dob,parentname,parentaddress;
    FirebaseUser user;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_details_parent, container, false);getActivity().setTitle("REPORT DETAILS");
        immunization=view.findViewById(R.id.immunization);bowel_movement=view.findViewById(R.id.bowel_movement);
        fever=view.findViewById(R.id.fever);inception=view.findViewById(R.id.inception);infected_area=view.findViewById(R.id.infected_area);
        intake=view.findViewById(R.id.intake);environment=view.findViewById(R.id.environment);crying=view.findViewById(R.id.crying);
        breast_feedC=view.findViewById(R.id.breast_feed);vomitC=view.findViewById(R.id.vomit);dehydrationC=view.findViewById(R.id.dehydration);
        dob=view.findViewById(R.id.dob);birth_weight=view.findViewById(R.id.birth_weight);name=view.findViewById(R.id.child_name);
        male=view.findViewById(R.id.male);female=view.findViewById(R.id.female);
        chat=view.findViewById(R.id.btn_chat);pdf=view.findViewById(R.id.pdf);call=view.findViewById(R.id.call);
        db=FirebaseFirestore.getInstance();
        immunization.setEnabled(false);bowel_movement.setEnabled(false);fever.setEnabled(false);inception.setEnabled(false);infected_area.setEnabled(false);
        intake.setEnabled(false);environment.setEnabled(false);crying.setEnabled(false);breast_feedC.setEnabled(false);vomitC.setEnabled(false);dehydrationC.setEnabled(false);
        dob.setEnabled(false);birth_weight.setEnabled(false);name.setEnabled(false);male.setEnabled(false);female.setEnabled(false);
        user=FirebaseAuth.getInstance().getCurrentUser();
        db.collection("Email").document("parent "+user.getEmail()).get().addOnSuccessListener(
                new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                phone=documentSnapshot.get("phone").toString();
            }
        });
        Bundle bundle=getArguments();doc_email=bundle.getString("email");child_name=bundle.getString("name");id=bundle.getString("id");
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();assert user != null;
        final String email = user.getEmail();db=FirebaseFirestore.getInstance();
        db.collection("Email").document("parent "+email).collection("sent_appointments").document(child_name+" "+doc_email).collection("Dates").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                db.collection("Email").document("parent "+email).collection("sent_appointments").document(child_name+" "+doc_email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        dob.setText(documentSnapshot.get("dob").toString());
                        Dob=documentSnapshot.get("dob").toString();
                        name.setText(child_name);
                        birth_weight.setText(documentSnapshot.get("birth_weight").toString());
                        birthweight=documentSnapshot.get("birth_weight").toString();
                        if(documentSnapshot.get("gender").toString().equals("Male")) male.setChecked(true);
                        else female.setChecked(true);
                        gender=documentSnapshot.get("gender").toString();
                    }
                });
                Map<String,Object> diagnosis=(Map<String, Object>)documentSnapshot.get("diagnosis");
                Map<String,Object> child_details=(Map<String, Object>) diagnosis.get("child_details");
               childDetails details=new childDetails(child_details.get("immunization").toString(),child_details.get("bowel_movement").toString(),child_details.get("fever").toString(),child_details.get("inception").toString(),child_details.get("infected_area").toString(),child_details.get("intake").toString(),child_details.get("environment").toString(),child_details.get("crying").toString(),child_details.get("vomit").toString(),child_details.get("breast_feed").toString(),child_details.get("dehydration").toString());
                immunization.setText(details.getImmunization());bowel_movement.setText(details.getBowel_movement());fever.setText(details.getFever());
                inception.setText(details.getInception());infected_area.setText(details.getInfected_area());intake.setText(details.getIntake());
                environment.setText(details.getEnvironment());crying.setText(details.getCrying());
                if(details.getBreast_feed().equals("true")) breast_feedC.setChecked(true);breastfeed=details.getBreast_feed();
                if(details.getVomit().equals("true"))vomitC.setChecked(true);vomit=details.getVomit();
                if(details.getDehydration().equals("true"))dehydrationC.setChecked(true);dehydration=details.getDehydration();
            }
        });
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chatIntent = new Intent(getActivity(), ChatActivity.class);
                db.collection("Email").document("doctor "+doc_email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        doc_uid=documentSnapshot.get("uid").toString();
                    }
                });
                chatIntent.putExtra("user_id","ZWvTV9b4oZeSmgkgyx1lpKbZpLN2");//doctor uid
                chatIntent.putExtra("user_name", doc_email);
                startActivity(chatIntent);
            }
        });
        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url="http://labemc.org/test/report.php?format=pdf&url='https://firebasestorage.googleapis.com/v0/b/fiemsih.appspot.com/o/Images%2FIMG-20190217-WA0006.jpg'&appointment_id=APT_290192039_0015&child_name=Rohan%20Roy&child_gender=Male&child_dob=20-12-2017&child_birth_weight=3%20KG&child_inception=20-12-2017&child_vomit=Yes&child_bowel_movement=Normal&child_breast_feeding=Yes&child_crying=normal&child_current_weight=5%20KG&child_infected_areas=Hand%20and%20face&child_dehydration=no&child_environment=Clean&child_fever=Slight&child_immunization=Polio2&child_intake=Normal%20Diet&parent_name=Gohan%20Roy&parent_address=Kolkata,%20West%20Bengal&parent_email=pawanshaw700@gmail.com&parent_phn=9876543210&medication1=Calpol&medication2=%20&testimonials1=TSH%20profile&testimonials2=%20";
                Uri webpage = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                if (intent.resolveActivity(getActivity().getPackageManager()) != null)
                    startActivity(intent);
                /*db.collection("Email").document("parent "+user.getEmail()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        parentname=documentSnapshot.get("name").toString();
                        parentaddress=documentSnapshot.get("address").toString();
                    }
                });
                Bundle bundle1=new Bundle();
                bundle1.putString("child_name",child_name);bundle1.putString("id",id);
                bundle1.putString("doc_email",doc_email);bundle1.putString("birth_weight",birthweight);
                bundle1.putString("dob",Dob);bundle1.putString("gender",gender);bundle1.putString("parent_name",parentname);

                bundle1.putString("breast_feed",breastfeed);bundle1.putString("crying",crying.getText().toString().trim());
                bundle1.putString("vomit",vomit);bundle1.putString("dehydration",dehydration);
                bundle1.putString("environment",environment.getText().toString().trim());
                bundle1.putString("immunization",immunization.getText().toString().trim());
                bundle1.putString("intake",intake.getText().toString().trim());
                bundle1.putString("bowel_movement",bowel_movement.getText().toString().trim());
                bundle1.putString("fever",fever.getText().toString().trim());
                bundle1.putString("inception",inception.getText().toString().trim());
                bundle1.putString("infected_area",infected_area.getText().toString().trim());
                bundle1.putString("parent_address",parentaddress);bundle1.putString("parent_phone",phone);
                bundle1.putString("parent_email",user.getEmail());


                annotateImage img = new annotateImage();
                img.setArguments(bundle1);
                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                ((RelativeLayout)getActivity().findViewById(R.id.log)).removeAllViews();
                fragmentManager.beginTransaction().replace(R.id.log,img).commit();*/

            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+phone));
                startActivity(intent);
            }
        });
        return view;
    }

}
