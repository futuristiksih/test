package com.example.test;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class previous_appointment_list extends Fragment {
    View view;
    ArrayList<myDoctors> arrayList;previousappointmentAdapter adapter;ArrayList<String> emails,ids,names;
    FirebaseFirestore db;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.doclist, container, false);getActivity().setTitle("CHOOSE A REPORT");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;String email = user.getEmail();
        ListView listView =  view.findViewById(R.id.doclist);
        arrayList = new ArrayList<>();
        adapter = new previousappointmentAdapter(getActivity(),arrayList);
        db = FirebaseFirestore.getInstance();
        emails= new ArrayList<>();ids=new ArrayList<>();names=new ArrayList<>();

        db.collection("Email").document("parent "+user.getEmail()).collection("sent_appointments").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                    arrayList.add(new myDoctors(documentSnapshot.get("name").toString(),documentSnapshot.get("id").toString(),documentSnapshot.get("guardian").toString(),documentSnapshot.get("status").toString(),documentSnapshot.get("date").toString()));
                    String[] parent_email=documentSnapshot.getId().split(" ");
                    String email=parent_email[parent_email.length-1];
                    emails.add(email);adapter.notifyDataSetChanged();
                    ids.add(documentSnapshot.get("id").toString());names.add(documentSnapshot.get("name").toString());
                }
            }
        });
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int i, long l) {
                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                ((RelativeLayout)view.findViewById(R.id.doc)).removeAllViews();
                Bundle bundle=new Bundle();
                bundle.putString("email",emails.get(i));bundle.putString("name",names.get(i));bundle.putString("id",ids.get(i));
                view_details_doctor viewDetailsDoctor=new view_details_doctor();
                viewDetailsDoctor.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.contentpage, viewDetailsDoctor).commit();
            }
        });
    return view;
    }
}
