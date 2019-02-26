package com.example.test;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
public class doctorDiagnosis extends Fragment {
    EditText descr_text;
    ImageButton addMed, addTest;
//    FirebaseFirestore db;
    String parent_email,child_name;
    ArrayList<objectMedicine> medArray;ArrayList<objectTest> testArray;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.doctor_diagnosis, container, false);
        getActivity().setTitle("Prescription");
        ListView medList,testList;
        descr_text = view.findViewById(R.id.descr);addMed=view.findViewById(R.id.addMed);addTest = view.findViewById(R.id.addTest);
        medArray = new ArrayList<>();testArray = new ArrayList<>();
        Bundle bundle=getArguments();parent_email=bundle.getString("parent_email");
        child_name=bundle.getString("child_name");
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
//                        final objectMedicine medicine = new objectMedicine(medName.getText().toString(),
//                                Integer.parseInt(weeks.getText().toString()),
//                                Integer.parseInt(doses.getText().toString()),
//                                condition.getText().toString());
//                        medArray.add(medicine);
                        LinearLayout root =view.findViewById(R.id.medication_layout);
                        root.setOrientation(LinearLayout.VERTICAL);
                        View child = getLayoutInflater().inflate(R.layout.list_item_med,null);
                        root.addView(child);
                        TextView med = child.findViewById(R.id.medName),
                                doses = child.findViewById(R.id.doses),
                                conditions = child.findViewById(R.id.conditions),
                                weeks = child.findViewById(R.id.weeks);
                        med.setText("Medicine: "+medName.getText().toString());
                        doses.setText("Dose(s): "+dose.getText().toString());
                        weeks.setText("Week(s): "+week.getText().toString());
                        conditions.setText("Condition: "+condition.getText().toString());
//                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                        assert user != null;
//                        String email = user.getEmail();
//                        db = FirebaseFirestore.getInstance();
//                        db.collection("Email")
//                                .document("doctor "+user.getEmail())
//                                .collection("received_appointments")
//                                .document(child_name+" "+parent_email)
//                                .get()

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
                        final objectTest objTest = new objectTest(test.getText().toString());
                        testArray.add(objTest);
                        LinearLayout root = (LinearLayout) view.findViewById(R.id.test_layouts);
                        root.setOrientation(LinearLayout.VERTICAL);
                        View child = getLayoutInflater().inflate(R.layout.prompts_test,null);
                        root.addView(child);
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
}
