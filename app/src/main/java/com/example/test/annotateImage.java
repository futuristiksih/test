package com.example.test;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

public class annotateImage extends Fragment {
    Button goback;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.annotate_image, container, false);
        getActivity().setTitle("Annotate Image");
        goback=view.findViewById(R.id.goback);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                ((RelativeLayout)getActivity().findViewById(R.id.log)).removeAllViews();
                fragmentManager.beginTransaction().replace(R.id.log,new doctorDiagnosis()).commit();
            }
        });
        return view;
    }
}
