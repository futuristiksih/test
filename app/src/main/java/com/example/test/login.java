package com.example.test;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;
public class login extends AppCompatActivity {
    private FirebaseAuth mAuth;private EditText email, password;FirebaseFirestore db;String userEmail="";TextView forgot;
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
    public void signIn(View view) {
        if (TextUtils.isEmpty(email.getText())) email.setError("EMPTY EMAIL ADDRESS FIELD");
        else if(TextUtils.isEmpty(password.getText())) password.setError("EMPTY PASSWORD FIELD");
        else {
            final ProgressDialog progressDialog= ProgressDialog.show(login.this,"PLEASE WAIT","PROCESSING",true);
            mAuth.signInWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) { progressDialog.dismiss();
                   if(!task.isSuccessful()){
                       try { throw Objects.requireNonNull(task.getException());
                       }
                       catch (FirebaseAuthInvalidUserException invalidEmail) {
                           Toast.makeText(getApplicationContext(), "EMAIL ID DOES NOT EXIST OR IS DISABLED", Toast.LENGTH_LONG).show();
                       }
                       catch (FirebaseAuthInvalidCredentialsException wrongPassword) {
                           Toast.makeText(getApplicationContext(), "PASSWORD IS WRONG", Toast.LENGTH_LONG).show();
                       }
                       catch (Exception e) {
                           Toast.makeText(getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_LONG).show();
                       }
                   }
                else {
                        mAuth= FirebaseAuth.getInstance();final FirebaseUser user = mAuth.getCurrentUser();assert user != null;
                        if(!user.isEmailVerified()){
                            user.sendEmailVerification().addOnCompleteListener(login.this, new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) Toast.makeText(login.this,"VERIFICATION EMAIL SENT TO "+user.getEmail(),Toast.LENGTH_LONG).show();
                                    else Toast.makeText(login.this,"FAILED TO SEND VERIFICATION EMAIL.", Toast.LENGTH_LONG).show();

                                }
                            });
                        }
                        else{
                            Toast.makeText(login.this,"SUCCESSFUL SIGN-IN AND EMAIL VERIFICATION", Toast.LENGTH_LONG).show();
                            userEmail=email.getText().toString().trim();getUser(userEmail);
                        }
                    }
                    }
                    });
            }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);setContentView(R.layout.login);
        email =  findViewById(R.id.email);password = findViewById(R.id.password);
        setTitle("LOGIN");db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null){
        if (user.isEmailVerified()) {
            userEmail=user.getEmail();getUser(userEmail);
        }}
        forgot=findViewById(R.id.forgot);
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(email.getText()) && !TextUtils.isEmpty(password.getText())){
                mAuth.signInWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                        if(task.isSuccessful()&& user.isEmailVerified()){
                            FirebaseAuth.getInstance().sendPasswordResetEmail(""+email.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) Toast.makeText(getApplicationContext(),"PASSWORD RECOVERY EMAIL TO "+email.getText().toString().trim(),Toast.LENGTH_LONG).show();
                                    else Toast.makeText(getApplicationContext(),"UNABLE TO SEND PASSWORD RECOVERY EMAIL TO "+email.getText().toString().trim(),Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        else Toast.makeText(getApplicationContext(),"ACCOUNT NOT EMAIL VERIFIED OR NO SUCH ACCOUNT EXISTS",Toast.LENGTH_LONG).show();
                    }
                });
            }
                else Toast.makeText(getApplicationContext(),"EMPTY FIELDS",Toast.LENGTH_LONG).show();
            }
        });

        TextView mSignUp = findViewById(R.id.signUp);
        mSignUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                        ((ConstraintLayout)findViewById(R.id.log)).removeAllViews();
                        fragmentManager.beginTransaction().replace(R.id.log, new register()).commit();
                    }
                });
    }
}