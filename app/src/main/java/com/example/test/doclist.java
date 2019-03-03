package com.example.test;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
public class doclist extends Fragment {
    View view;ArrayList<doclistdesign> doclistdesigns;
    doclistadapter doclistadapter;ArrayList<String> emails;
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final FirebaseFirestore db;FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;String email = user.getEmail();
        ListView listView =  Objects.requireNonNull(getView()).findViewById(R.id.doclist);
        doclistdesigns = new ArrayList<>();doclistadapter = new doclistadapter(getActivity(),doclistdesigns);
        db = FirebaseFirestore.getInstance();
        emails=new ArrayList<>();
        db.collection("Email").whereEqualTo("verified",true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    String weekDay;
                    SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);

                    Calendar calendar = Calendar.getInstance();
                    weekDay = dayFormat.format(calendar.getTime());

                    for (QueryDocumentSnapshot document : task.getResult()) {

                        if (document.exists()&&Integer.parseInt(document.get("count").toString())<5) {
                            HashMap<String,Object> av = (HashMap<String, Object>) document.get("available");
                            for ( String key : av.keySet() ) {
                                if(key.equals(weekDay)){
                                    String timeStamp = new SimpleDateFormat("HH",Locale.getDefault()).format(new Date());
                                    int t = Integer.parseInt(timeStamp);
                                    HashMap<String,Object> w = (HashMap<String,Object>)av.get(key);
                                    String st = w.get("start").toString().split(":")[0];
                                    int s = Integer.parseInt(st);
                                    String end = w.get("end").toString().split(":")[0];
                                    int e = Integer.parseInt(end);
                                    Log.i("n",s+" "+t+" "+e);
                                    if(s<=t&&t<=e){
                                        doclistdesigns.add(new doclistdesign(document.get("name").toString(),document.get("degree").toString(),document.get("exp_yrs").toString(),document.get("rating").toString(),document.get("city").toString()));
                                        emails.add(document.get("email").toString());
                                        doclistadapter.notifyDataSetChanged();
                                    }

                                }
                            }
                        }
                    }
                }
            }
        });
        listView.setAdapter(doclistadapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                //((RelativeLayout)view.findViewById(R.id.doc)).removeAllViews();
                Intent intent=new Intent(getActivity(),showDoctor.class);
                intent.putExtra("email",emails.get(i));
                startActivity(intent);
            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.doclist, container, false);getActivity().setTitle("CHOOSE A DOCTOR");return view;
    }
}
