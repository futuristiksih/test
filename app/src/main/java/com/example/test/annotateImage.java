package com.example.test;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
public class annotateImage extends Fragment {
    //recyclerview objectprivate
    RecyclerView recyclerView;
    //adapter objectprivate
    RecyclerView.Adapter adapter;
    //database reference
    private DatabaseReference mDatabase;
    //progress dialog
    private ProgressDialog progressDialog;
    //list to hold all the uploaded images
    private List<Upload> uploads;
    String child_name, parent_email, id;
    ArrayList<String> filenames;
    FirebaseFirestore db;
    FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.annotate_image, container, false);
        getActivity().setTitle("Annotate Image");
        db = FirebaseFirestore.getInstance();
        Bundle bundle = getArguments();
        child_name = bundle.getString("child_name");
        parent_email = bundle.getString("parent_email");
        id = bundle.getString("id");
        filenames = new ArrayList<>();
        user = FirebaseAuth.getInstance().getCurrentUser();
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        progressDialog = new ProgressDialog(getActivity());
        uploads = new ArrayList<>();

        //displaying progress dialog while fetching images
        progressDialog.setMessage("Please wait...");
        progressDialog.show();


        db.collection("Email")
                .document("parent " + parent_email)
                .collection("sent_appointments")
                .document(child_name + " " + user.getEmail())
                .collection("Dates")
                .document("" + id).collection("Untag_images").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                progressDialog.dismiss();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    filenames.add(doc.getString("filename"));
                }
                adapter = new ImageAdapter(getActivity(), filenames);
                recyclerView.setAdapter(adapter);
            }
        });


        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_diagnosis, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            Log.i("print","anything");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
