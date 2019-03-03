package com.example.test;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
public class previous_appointment_list extends Fragment {
    View view;String doc[];FirebaseUser user;
    ArrayList<myDoctors> arrayList;previousappointmentAdapter adapter;ArrayList<String> doc_emails,ids,names;
    FirebaseFirestore db;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.doclist, container, false);getActivity().setTitle("CHOOSE A REPORT");
        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        ListView listView =  view.findViewById(R.id.doclist);arrayList = new ArrayList<>();
        adapter = new previousappointmentAdapter(getActivity(),arrayList);
        db = FirebaseFirestore.getInstance();
        doc_emails= new ArrayList<>();ids=new ArrayList<>();names=new ArrayList<>();

        db.collection("Email").document("parent "+user.getEmail()).collection("sent_appointments").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot document:queryDocumentSnapshots) {
                    arrayList.add(new myDoctors(document.get("name").toString(),document.get("id").toString(),document.get("doc_name").toString(),document.get("status").toString(),document.get("date").toString()));
                    doc = document.getId().split(" ");
                    doc_emails.add(doc[doc.length-1]);ids.add(document.get("id").toString());names.add(document.get("name").toString());
                    adapter.notifyDataSetChanged();
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
                bundle.putString("email",doc_emails.get(i));bundle.putString("name",names.get(i));bundle.putString("id",ids.get(i));
                view_details_parent details_parent=new view_details_parent();
                details_parent.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.contentpage, details_parent).commit();
            }
        });
    return view;
    }
}
