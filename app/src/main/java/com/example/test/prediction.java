package com.example.test;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
public class prediction extends Fragment {
    TextView text;Button consult;String name;View view;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.prediction, container, false);
        consult=view.findViewById(R.id.consult);text=view.findViewById(R.id.text);
        Bundle b=getArguments();name=b.getString("name");
        if(!name.equals("no")&&!name.equals("yes")) {
            text.setText("Your child might be suffering from "+name);
            consult.setVisibility(View.VISIBLE);
            consult.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    doclist d=new doclist();
                    android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    ((RelativeLayout)getActivity().findViewById(R.id.Log)).removeAllViews();
                    fragmentManager.beginTransaction().replace(R.id.Log,d).commit();
                }
            });
        }
        else if(name.equals("no")){
            text.setText("THESE SYMPTOMS DO NOT REQUIRE CONSULTATION FROM ANY PHYSICIAN");
            consult.setText("BACK");
            consult.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(getActivity(),parentProfile.class);startActivity(i);getActivity().finish();
                }
            });
        }
        else{
            text.setText("Your child might be suffering from some skin problem.");
            consult.setVisibility(View.VISIBLE);
            consult.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    doclist d=new doclist();
                    android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    ((RelativeLayout)getActivity().findViewById(R.id.Log)).removeAllViews();
                    fragmentManager.beginTransaction().replace(R.id.Log,d).commit();
                }
            });
        }
    return view;
    }
}
