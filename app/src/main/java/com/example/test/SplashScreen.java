package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class SplashScreen extends AppCompatActivity {
    private FirebaseAuth mAuth;String userEmail;FirebaseFirestore db;
    public void getUser(String userEmail){
        db.collection("Person").whereEqualTo("email", userEmail).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document: Objects.requireNonNull(task.getResult())) {
                        if (document.exists()) {
                            String temp = document.getId().split(" ")[0];
                            if (temp.equals("PARENT")) {
                                Intent i = new Intent(getApplicationContext(), parentProfile.class);startActivity(i);finish();
                            }
                            else {
                                Intent i = new Intent(getApplicationContext(), doctorProfile.class);startActivity(i);finish();
                            }
                        }
                    }
                }
                else Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("FUTURISTIC");
        setContentView(R.layout.activity_splash_screen);
        int SPLASH_TIMEOUT = 2000;db = FirebaseFirestore.getInstance();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                if(user!=null){
                    if (user.isEmailVerified()) {
                        userEmail=user.getEmail();getUser(userEmail);
                    }}
                    else{
                Intent registerClass = new Intent(getApplicationContext(), login.class);startActivity(registerClass);finish();}
            }
        }, SPLASH_TIMEOUT);
    }
}