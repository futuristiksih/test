package com.example.test;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
        db.collection("Email").whereEqualTo("email", userEmail).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document: Objects.requireNonNull(task.getResult())) {
                        if (document.exists()) {
                            String temp = document.getId().split(" ")[0];
                            if (temp.equals("parent")) {
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
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        changeStatusBarColor();
        setContentView(R.layout.activity_splash_screen);
        int SPLASH_TIMEOUT = 4000;db = FirebaseFirestore.getInstance();
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
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
}