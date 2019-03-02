package com.example.test;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
public class suggestions extends Fragment {
    RadioGroup a,b,c,d,e,f,g,h,i;Button save,b1;ArrayList<String> num;String name="";LinearLayout l1,l2;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.suggestions, container, false);
        a=view.findViewById(R.id.a);b=view.findViewById(R.id.b);c=view.findViewById(R.id.c);d=view.findViewById(R.id.d);e=view.findViewById(R.id.e);
        f=view.findViewById(R.id.f);g=view.findViewById(R.id.g);h=view.findViewById(R.id.h);i=view.findViewById(R.id.i);
        save=view.findViewById(R.id.next);
        num=new ArrayList<>();l1=view.findViewById(R.id.l1);l2=view.findViewById(R.id.l2);
        b.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                b1=view.findViewById(i);

                if(b1.getText().toString().equals("Yes")) { num.add(""+4);l1.setVisibility(View.VISIBLE); }
                else { l1.setVisibility(View.GONE);num.add(""+5);}
            }
        });
        d.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                b1=view.findViewById(i);
                if(b1.getText().toString().equals("Yes")) { num.add(""+11);l2.setVisibility(View.VISIBLE); }
                else { l2.setVisibility(View.GONE);num.add(""+12); }
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b1=view.findViewById(a.getCheckedRadioButtonId());
                if(b1.getText().toString().equals("Yes, only fever")) num.add(""+1);
                else if(b1.getText().toString().equals("Yes, with headache")) num.add(""+2);
                else num.add(""+3);

                b1=view.findViewById(c.getCheckedRadioButtonId());
                if(b1.getText().toString().equals("Normal redness")) num.add(""+6);
                else if(b1.getText().toString().equals("Red circular centre")) num.add(""+7);
                else if(b1.getText().toString().equals("Honey coloured")) num.add(""+8);
                else if(b1.getText().toString().equals("Lacy red")) num.add(""+9);
                else num.add(""+10);

                b1=view.findViewById(e.getCheckedRadioButtonId());
                if(b1.getText().toString().equals("Extreme")) num.add(""+13);
                else num.add(""+14);

                b1=view.findViewById(f.getCheckedRadioButtonId());
                if(b1.getText().toString().equals("No")) num.add(""+15);
                else if(b1.getText().toString().equals("Yes, superficial")) num.add(""+16);
                else if(b1.getText().toString().equals("Yes, blisters in hand, food, mouth.")) num.add(""+17);
                else num.add(""+18);

                b1=view.findViewById(g.getCheckedRadioButtonId());
                if(b1.getText().toString().equals("Yes")) num.add(""+19);
                else num.add(""+20);

                b1=view.findViewById(h.getCheckedRadioButtonId());
                if(b1.getText().toString().equals("Yes")) num.add(""+21);
                else num.add(""+22);

                b1=view.findViewById(i.getCheckedRadioButtonId());
                if(b1.getText().toString().equals("Yes")) num.add(""+23);
                else num.add(""+24);


                if(num.contains(""+1)&&num.contains(""+4)&&num.contains(""+23)) name= "VIRAL RASH.";
                else if(num.contains(""+11)&&num.contains(""+13)&&num.contains(""+4)&&num.contains(""+6)&&num.contains(""+19)) name="ECZEMA.";
                else if(num.contains(""+11)&&num.contains(""+4)&&num.contains(""+7))name="URTICARIA.";
                else if(num.contains(""+11)&&num.contains(""+14)&&num.contains(""+4)&&num.contains(""+3)&&num.contains(""+16)) name="IMPETIGO.";
                else if(num.contains(""+1)&&num.contains(""+4)&&num.contains(""+9)&&num.contains(""+21)) name="FIFTH DISEASE.";
                else if(num.contains(""+1)&&num.contains(""+4)&&num.contains(""+10)&&num.contains(""+17)) name="HAND FOOT AND MOUTH DISEASE.";
                else if(num.contains(""+11)&&num.contains(""+14)&&num.contains(""+18)&&num.contains(""+2)) name="CHICKENPOX.";
                else if(num.contains(""+7)||num.contains(""+8)||num.contains(""+9)||num.contains(""+10)||num.contains(""+13)||num.contains(""+17)||num.contains(""+18)||num.contains(""+23))name="yes";
                else name="no";

                Log.i("name",name);
                Bundle b=new Bundle();b.putString("name",name);
                prediction p=new prediction();p.setArguments(b);
                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                ((RelativeLayout)getActivity().findViewById(R.id.Log)).removeAllViews();
                fragmentManager.beginTransaction().replace(R.id.Log,p).commit();
            }
        });
        return view;
    }

}
