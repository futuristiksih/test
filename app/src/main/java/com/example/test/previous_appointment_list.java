package com.example.test;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
public class previous_appointment_list extends Fragment {

    View view;
    ArrayList<myDoctors> arrayList;
    previousappointmentAdapter adapter;
    ArrayList<String> doc_emails,ids,names,de,cn;
    String doc_name;
    FirebaseFirestore db;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.doclist, container, false);getActivity().setTitle("CHOOSE A REPORT");
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        ListView listView =  view.findViewById(R.id.doclist);
        arrayList = new ArrayList<>();
        adapter = new previousappointmentAdapter(getActivity(),arrayList);
        db = FirebaseFirestore.getInstance();
        doc_emails= new ArrayList<>();
        ids=new ArrayList<>();
        names=new ArrayList<>();
        de = new ArrayList<>();
        cn = new ArrayList<>();
        db.collection("Email").
                document("parent "+user.getEmail())
                .collection("sent_appointments")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                            String[] doc=documentSnapshot.getId().split(" ");
                            final String doc_email=doc[doc.length-1];
                            final String child_name=documentSnapshot.get("name").toString();
                            db.collection("Email")
                                    .document("doctor "+doc_email)
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if(documentSnapshot.exists()){
                                                String docs = documentSnapshot.getId();
                                                String dc;
                                            }
                                            final String de = doc_email;
                                            final String cn = child_name;
                                            doc_name=documentSnapshot.get("name").toString();
                                            db.collection("Email")
                                                    .document("doctor "+doc_email)
                                                    .collection("received_appointments")
                                                    .document(child_name+" "+user.getEmail())
                                                    .get()
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                            if(documentSnapshot.exists()){
                                                                Log.i("name",doc_name);
                                                                arrayList.add(new myDoctors(child_name,documentSnapshot.get("id").toString(),
                                                                        doc_name,
                                                                        documentSnapshot.get("status").toString(),documentSnapshot.get("date").toString()));
                                                                doc_emails.add(doc_email);
                                                                ids.add(documentSnapshot.get("id").toString());
                                                                names.add(child_name);
                                                                adapter.notifyDataSetChanged();
                                                            }
                                                        }
                                                    });
                                        }
                                    });
                        }
                    }
                });

        Log.i("cn",String.valueOf(cn.size()));

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int i, long l) {
                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                ((RelativeLayout)view.findViewById(R.id.doc)).removeAllViews();
                Bundle bundle=new Bundle();
                bundle.putString("email",doc_emails.get(i));
                bundle.putString("name",names.get(i));
                bundle.putString("id",ids.get(i));
                view_details_parent details_parent=new view_details_parent();
                details_parent.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.contentpage, details_parent).commit();
            }
        });
    return view;
    }


}
