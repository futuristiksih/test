package com.example.test;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class doctorDiagnosis extends Fragment {
    EditText descr_text;
    ImageButton addMed, addTest;
    FirebaseFirestore db;
    String child_name, parent_email, id, type,doc_email,doc_name;
    FirebaseUser user;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.doctor_diagnosis, container, false);
        setHasOptionsMenu(true);
        getActivity().setTitle("Prescription");
        user = FirebaseAuth.getInstance().getCurrentUser();

        final LinearLayout rootMed =view.findViewById(R.id.medication_layout);
        rootMed.setOrientation(LinearLayout.VERTICAL);

        final LinearLayout rootTest = (LinearLayout) view.findViewById(R.id.test_layouts);
        rootTest.setOrientation(LinearLayout.VERTICAL);

        ListView medList,testList;
        descr_text = view.findViewById(R.id.descr);
        addMed=view.findViewById(R.id.addMed);
        addTest = view.findViewById(R.id.addTest);

        Bundle bundle=getArguments();
        child_name = bundle.getString("child_name");
        id = bundle.getString("id");
        type = bundle.getString("user");
        if(type.equals("p")){
            doc_email = bundle.getString("doctor_email");
            parent_email = user.getEmail();
            addMed.setVisibility(View.GONE);
            addTest.setVisibility(View.GONE);

            db = FirebaseFirestore.getInstance();
            CollectionReference testpath =
                    db.collection("Email")
                    .document("doctor " + doc_email)
                    .collection("received_appointments")
                    .document(child_name + " " + parent_email)
                    .collection("tests");

            CollectionReference medpath = db.collection("Email")
                    .document("doctor " + doc_email)
                    .collection("received_appointments")
                    .document(child_name + " " + parent_email)
                    .collection("medication");

            testpath.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        View child = getLayoutInflater().inflate(R.layout.prompts_test,null);
                        rootTest.addView(child);

                        TextView testVw = child.findViewById(R.id.test);
                        testVw.setText(doc.get("test").toString());

                    }


                    }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i("Error",e.getMessage());
                }
            });

            medpath.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Log.i("med",doc.get("medicine").toString());
                        Log.i("dose",doc.get("doses").toString());
                        Log.i("week",doc.get("weeks").toString());
                        View child = getLayoutInflater().inflate(R.layout.list_item_med,null);
                        rootMed.addView(child);
                        TextView med = child.findViewById(R.id.medName),
                                doses = child.findViewById(R.id.doses),
                                conditions = child.findViewById(R.id.conditions),
                                weeks = child.findViewById(R.id.weeks);

                        med.setText("Medicine: "+doc.get("medicine").toString());
                        doses.setText("Dose(s): "+doc.get("doses").toString());
                        weeks.setText("Week(s): "+doc.get("weeks").toString());
                        conditions.setText("Condition: "+doc.get("conditions").toString());

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i("Error",e.getMessage());
                }
            });

            db.collection("Email")
                    .document("doctor " + doc_email)
                    .collection("received_appointments")
                    .document(child_name + " " + parent_email)
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    //Log.i("descr",documentSnapshot.get("description").toString());
                    descr_text.setText(documentSnapshot.get("description").toString());
                }
            });
            descr_text.setEnabled(false);
        }
        else{
            doc_email = user.getEmail();
            parent_email = bundle.getString("parent_email");
        }

        // Dialog box for testimonal addition
        addMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(getActivity());
                View promptsView = li.inflate(R.layout.activity_med, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(promptsView);
                final EditText medName =promptsView.findViewById(R.id.edit_med);
                final EditText week =promptsView.findViewById(R.id.edit_week);
                final EditText dose = promptsView.findViewById(R.id.edit_dose);
                final EditText condition = promptsView.findViewById(R.id.edit_condition);
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        View child = getLayoutInflater().inflate(R.layout.list_item_med,null);
                        rootMed.addView(child);
                        child.setOnClickListener(onClickListener);

                        TextView med = child.findViewById(R.id.medName),
                                doses = child.findViewById(R.id.doses),
                                conditions = child.findViewById(R.id.conditions),
                                weeks = child.findViewById(R.id.weeks);
                        med.setText("Medicine: "+medName.getText().toString());
                        doses.setText("Dose(s): "+dose.getText().toString());
                        weeks.setText("Week(s): "+week.getText().toString());
                        conditions.setText("Condition: "+condition.getText().toString());


                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog=builder.create();alertDialog.show();
            }
        });




        // Dialog box for testimonal addition
        addTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(getActivity());
                View promptsView = li.inflate(R.layout.activity_test, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(promptsView);
                final EditText test =promptsView.findViewById(R.id.edit_test);
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        View child = getLayoutInflater().inflate(R.layout.prompts_test,null);
                        rootTest.addView(child);

                        child.setOnClickListener(onClickListener2);

                        TextView testVw = child.findViewById(R.id.test);
                        testVw.setText(test.getText().toString());
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog=builder.create();
                alertDialog.show();
            }
        });



        return view;
    }

    //OnClickListener for test linearlayout childs
    View.OnClickListener onClickListener2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            final View child = (LinearLayout)v;

            LayoutInflater li = LayoutInflater.from(getActivity());
            View promptsView = li.inflate(R.layout.activity_test, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setView(promptsView);
            final EditText test =promptsView.findViewById(R.id.edit_test);

            TextView testvv = child.findViewById(R.id.test);

            test.setText(testvv.getText().toString());

            builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    TextView testVw = child.findViewById(R.id.test);
                    testVw.setText(test.getText().toString());

                }
            });
            builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ((LinearLayout)child.getParent()).removeView(child);
                }
            });
            AlertDialog alertDialog=builder.create();alertDialog.show();
        }
    };

    //OnClickListener for medication linearlayout childs
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            final View child = (LinearLayout)v;
            LayoutInflater li = LayoutInflater.from(getActivity());
            View promptsView = li.inflate(R.layout.activity_med, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setView(promptsView);
            final EditText medName =promptsView.findViewById(R.id.edit_med);
            final EditText week =promptsView.findViewById(R.id.edit_week);
            final EditText dose = promptsView.findViewById(R.id.edit_dose);
            final EditText condition = promptsView.findViewById(R.id.edit_condition);

            TextView med = child.findViewById(R.id.medName),
                    doses = child.findViewById(R.id.doses),
                    conditions = child.findViewById(R.id.conditions),
                    weeks = child.findViewById(R.id.weeks);
            medName.setText(med.getText().toString().substring(10));
            week.setText(weeks.getText().toString().substring(9));
            dose.setText(doses.getText().toString().substring(9));
            condition.setText(conditions.getText().toString().substring(11));

            builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    TextView med = child.findViewById(R.id.medName),
                            doses = child.findViewById(R.id.doses),
                            conditions = child.findViewById(R.id.conditions),
                            weeks = child.findViewById(R.id.weeks);
                    med.setText("Medicine: "+medName.getText().toString());
                    doses.setText("Dose(s): "+dose.getText().toString());
                    weeks.setText("Week(s): "+week.getText().toString());
                    conditions.setText("Condition: "+condition.getText().toString());
                }
            });
            builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ((LinearLayout)child.getParent()).removeView(child);
                }
            });
            AlertDialog alertDialog=builder.create();alertDialog.show();
        }
    };

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem save = menu.findItem(R.id.action_save),
                download = menu.findItem(R.id.action_download);
        if(type.equals("p")){
            save.setVisible(false);
        }else{
            download.setVisible(false);
        }

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_diagnosis, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()==R.id.action_save){

            LinearLayout rootMed = (LinearLayout) view.findViewById(R.id.medication_layout);

            for(int i=0; i < rootMed.getChildCount();i++) {
                View v = (LinearLayout) rootMed.getChildAt(i);
                TextView mname, doses, weeks, condition;
                mname = v.findViewById(R.id.medName);
                doses = v.findViewById(R.id.doses);
                weeks = v.findViewById(R.id.weeks);
                condition = v.findViewById(R.id.conditions);
                Log.i("mname", mname.getText().toString());
                Log.i("weeks", weeks.getText().toString());
                Log.i("condition", doses.getText().toString());
                Log.i("doses", condition.getText().toString());
                Map<String,Object> medicine = new HashMap<>();
                medicine.put("medicine",mname.getText().toString().substring(10));
                medicine.put("weeks",weeks.getText().toString().substring(9));
                medicine.put("doses",doses.getText().toString().substring(9));
                medicine.put("conditions",condition.getText().toString().substring(11));


                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                assert user != null;
                String email = user.getEmail();
                db = FirebaseFirestore.getInstance();
                db.collection("Email")
                        .document("doctor " + user.getEmail())
                        .collection("received_appointments")
                        .document(child_name + " " + parent_email)
                        .collection("medication").document(Integer.toString(i)).set(medicine);

                db = FirebaseFirestore.getInstance();
                db.collection("diseases").document("skin disease")
                        .collection("medication").document(Integer.toString(i)).set(medicine);
            }


            LinearLayout rootTest = (LinearLayout) view.findViewById(R.id.test_layouts);
            for(int i=0; i < rootTest.getChildCount();i++) {
                View v = (LinearLayout) rootTest.getChildAt(i);
                TextView test;
                test = v.findViewById(R.id.test);

                Log.i("test", test.getText().toString());

                Map<String,Object> tests = new HashMap<>();
                tests.put("test",test.getText().toString());



                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                assert user != null;
                String email = user.getEmail();
                db = FirebaseFirestore.getInstance();
                db.collection("Email")
                        .document("doctor " + user.getEmail())
                        .collection("received_appointments")
                        .document(child_name + " " + parent_email)
                        .collection("tests").document(Integer.toString(i)).set(tests);

                db = FirebaseFirestore.getInstance();
                db.collection("diseases").document("skin disease")
                        .collection("tests").document(Integer.toString(i)).set(tests);
            }

            EditText descr = view.findViewById(R.id.descr);
            Map<String,Object> description = new HashMap<>();
            description.put("status","check");
            description.put("description",descr.getText().toString());
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            assert user != null;
            String email = user.getEmail();
            db = FirebaseFirestore.getInstance();
            db.collection("Email")
                    .document("doctor " + user.getEmail())
                    .collection("received_appointments")
                    .document(child_name + " " + parent_email)
                    .set(description, SetOptions.merge()).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i("i","hoini");
                }
            });

            db.collection("Email").document("doctor "+doc_email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    doc_name = documentSnapshot.get("name").toString();
                }
            });

            Map<String,Object> notificationMessage = new HashMap<>();
            notificationMessage.put("message",doc_name+" diagnosed "+child_name);
            notificationMessage.put("from","doctor "+user.getEmail());

            Toast.makeText(getActivity(), "File Uploaded Successfully", Toast.LENGTH_SHORT).show();
            Intent i=new Intent(getActivity(),doctorProfile.class);startActivity(i);getActivity().finish();
            return true;
        }
        else if(item.getItemId()==R.id.action_download){
            //Showing pdf commented for now
            String url="http://labemc.org/test/report.php?format=pdf&url='https://firebasestorage.googleapis.com/v0/b/fiemsih.appspot.com/o/Images%2FIMG-20190217-WA0006.jpg'&appointment_id=APT_290192039_0015&child_name=Rohan%20Roy&child_gender=Male&child_dob=20-12-2017&child_birth_weight=3%20KG&child_inception=20-12-2017&child_vomit=Yes&child_bowel_movement=Normal&child_breast_feeding=Yes&child_crying=normal&child_current_weight=5%20KG&child_infected_areas=Hand%20and%20face&child_dehydration=no&child_environment=Clean&child_fever=Slight&child_immunization=Polio2&child_intake=Normal%20Diet&parent_name=Gohan%20Roy&parent_address=Kolkata,%20West%20Bengal&parent_email=pawanshaw700@gmail.com&parent_phn=9876543210&medication1=Calpol&medication2=%20&testimonials1=TSH%20profile&testimonials2=%20";
            Uri webpage = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
            if (intent.resolveActivity(getActivity().getPackageManager()) != null)
                startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
}
