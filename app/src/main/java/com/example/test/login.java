package com.example.test;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class login extends AppCompatActivity {
    private FirebaseAuth mAuth;final Context context=this;
    private EditText email, password;FirebaseFirestore db;String userEmail="",userPass="";TextView forgot;
    private FirebaseFirestore mFirestore;
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
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
    public void signIn(View view) {
        mFirestore = FirebaseFirestore.getInstance();
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
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        changeStatusBarColor();
        setContentView(R.layout.login);
        email =  findViewById(R.id.email);password = findViewById(R.id.password);
        setTitle("LOGIN");db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        forgot=findViewById(R.id.forgot);
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userPass="";userEmail="";
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.prompts, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(promptsView);
                final EditText mail =promptsView.findViewById(R.id.email);
                final EditText pass =promptsView.findViewById(R.id.pass);
                builder.setPositiveButton("SEND", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userEmail = mail.getText().toString().trim();
                        userPass= pass.getText().toString().trim();
                        if(!userEmail.equals("") && !userPass.equals("")){
                            mAuth.signInWithEmailAndPassword(userEmail,userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                                    if(task.isSuccessful()&& user.isEmailVerified()){
                                        FirebaseAuth.getInstance().sendPasswordResetEmail(""+userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) Toast.makeText(getApplicationContext(),"PASSWORD RECOVERY EMAIL TO "+userEmail,Toast.LENGTH_LONG).show();
                                                else Toast.makeText(getApplicationContext(),"UNABLE TO SEND PASSWORD RECOVERY EMAIL TO "+userEmail,Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                    else Toast.makeText(getApplicationContext(),"ACCOUNT NOT EMAIL VERIFIED OR NO SUCH ACCOUNT EXISTS",Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog=builder.create();
                alertDialog.show();

            }
        });

        TextView mSignUp = findViewById(R.id.signUp);
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),register.class);startActivity(intent);
            }
        });
    }
}