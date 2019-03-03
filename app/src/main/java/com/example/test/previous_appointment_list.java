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
    ArrayList<myDoctors> arrayList;previousappointmentAdapter adapter;ArrayList<String> doc_emails,ids,names;String doc_name;
    FirebaseFirestore db;
   /* public void download(final int i){
        db.collection("Email").document("doctor "+de.get(i)).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.i("ch",cn.get(i));
                doc_name=documentSnapshot.get("name").toString();
                db.collection("Email").document("doctor "+de.get(i)).collection("received_appointments").document(cn.get(i)+" "+user.getEmail()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        arrayList.add(new myDoctors(cn.get(i),documentSnapshot.get("id").toString(),doc_name,documentSnapshot.get("status").toString(),documentSnapshot.get("date").toString()));
                        doc_emails.add(de.get(i));ids.add(documentSnapshot.get("id").toString());names.add(cn.get(i));
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }*/
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.doclist, container, false);getActivity().setTitle("CHOOSE A REPORT");
        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        final ArrayList<String> cn,de;
        ListView listView =  view.findViewById(R.id.doclist);arrayList = new ArrayList<>();
        adapter = new previousappointmentAdapter(getActivity(),arrayList);
        db = FirebaseFirestore.getInstance();
        doc_emails= new ArrayList<>();ids=new ArrayList<>();names=new ArrayList<>();
        cn = new ArrayList<>();de=new ArrayList<>();

        db.collection("Email").document("parent "+user.getEmail()).collection("sent_appointments").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot document:queryDocumentSnapshots) {
                    doc = document.getId().split(" ");
                    de.add(doc[doc.length-1]);
                    cn.add(document.get("name").toString());
                }
            }
        });
        Log.i("sozd",""+cn.size());
        for(int i=0;i<de.size();i++){
            Log.i("childname",""+cn.get(i));
            //download(i);
        }
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
