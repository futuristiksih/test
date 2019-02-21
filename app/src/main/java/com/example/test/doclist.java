package com.example.test;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;
public class doclist extends Fragment {
    View view;ArrayList<doclistdesign> doclistdesigns;doclistadapter doclistadapter;
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final FirebaseFirestore db;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;String email = user.getEmail();
        ListView listView =  Objects.requireNonNull(getView()).findViewById(R.id.doclist);
        doclistdesigns = new ArrayList<>();
        doclistadapter = new doclistadapter(getActivity(),doclistdesigns);
        db = FirebaseFirestore.getInstance();
        db.collection("Email").document("doctor "+email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
               if(task.isSuccessful()){
                    DocumentSnapshot document=task.getResult();
                    if(document.exists()){
                        doclistdesigns.add(new doclistdesign(document.get("name").toString(),document.get("degree").toString(),document.get("exp_yrs").toString(),document.get("rating").toString(),document.get("city").toString()));

                    }
               }
            }
        });
        listView.setAdapter(doclistadapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                ((ConstraintLayout)view.findViewById(R.id.doc)).removeAllViews();
                fragmentManager.beginTransaction().replace(R.id.doc, new Appointment()).commit();
            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.doclist, container, false);getActivity().setTitle("CHOSE A DOCTOR");return view;
    }
}
