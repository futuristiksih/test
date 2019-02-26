package com.example.test;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.view.menu.ActionMenuItem;
import android.text.Editable;
import android.text.TextWatcher;
import com.bumptech.glide.Glide;

import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;

public class parentProfile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    TextView email,address,name,phone;
    CircularImageView parentPic,navPic;String emailid;
    FirebaseFirestore db;private StorageReference imageref;FirebaseUser user;MenuItem menuItem;
    public static final int RESULT_LOAD_IMAGE = 1;Uri selectedImage;TextView navName;
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    public void updateParent(objectParent parent){
        name.setText(parent.getName());email.setText(parent.getEmail());address.setText(parent.getAddress());phone.setText(parent.getPhone());
        final NavigationView navigationView = findViewById(R.id.nav_view);navigationView.setNavigationItemSelectedListener(this);
        final View hView =  navigationView.getHeaderView(0);
        navName= hView.findViewById(R.id.navName);navName.setText(name.getText().toString());
        navPic=hView.findViewById(R.id.navPic);
        imageref.child(emailid+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext()).load(uri).into(parentPic);
                Glide.with(getApplicationContext()).load(uri).into(navPic);
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RESULT_LOAD_IMAGE && resultCode==RESULT_OK && data!=null ){
            selectedImage=data.getData();parentPic.setImageURI(selectedImage);navPic.setImageURI(selectedImage);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);setContentView(R.layout.activity_main);
        Toolbar toolbar =findViewById(R.id.toolbar);setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);toggle.syncState();
        name=findViewById(R.id.name);name.setEnabled(false);
        phone=findViewById(R.id.phone);phone.setEnabled(false);
        email=findViewById(R.id.email);email.setEnabled(false);
        address=findViewById(R.id.address);address.setEnabled(false);
        parentPic=findViewById(R.id.parentPic);parentPic.setEnabled(false);

        imageref = FirebaseStorage.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        emailid = user.getEmail();
        db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Email").document("parent "+emailid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    assert document != null;
                    if (document.exists()) {
                        objectParent parent = document.toObject(objectParent.class);updateParent(parent);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Document not found", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(getApplicationContext(),login.class);startActivity(intent);finish();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Document not found", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getApplicationContext(),login.class);startActivity(intent);finish();
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menuItem=menu.findItem(R.id.editProfile);menuItem.setVisible(true);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.editProfile) {
            if(item.getTitle()=="EDIT PROFILE"){
                item.setTitle("SAVE CHANGES");
                name.setEnabled(true);address.setEnabled(true);phone.setEnabled(true);parentPic.setEnabled(true);
                parentPic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i,RESULT_LOAD_IMAGE);
                    }
                });

            }
            else{
                item.setTitle("EDIT PROFILE");
                name.setEnabled(false);address.setEnabled(false);phone.setEnabled(false);parentPic.setEnabled(false);
                name.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                    @Override
                    public void afterTextChanged(Editable editable) {
                        NavigationView navigationView = findViewById(R.id.nav_view);
                        View hView =  navigationView.getHeaderView(0);
                        navName = hView.findViewById(R.id.navName);navName.setText(name.getText().toString());
                    }
                });
                db.collection("Email").document("parent "+user.getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            db.collection("Email").document("parent "+user.getEmail()).update("name",name.getText().toString().trim(),"address",address.getText().toString().trim(),"phone",phone.getText().toString().trim());
                            if(selectedImage!=null){
                                imageref = mStorageRef.child(email.getText().toString()+".jpg");
                            imageref.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                }
                            });
                            }
                        }

                    }
                });
            }
        }
        return super.onOptionsItemSelected(item);
    }
    //@SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        ((LinearLayout)findViewById(R.id.contentpage)).removeAllViews();
        if (id == R.id.appointment) {
            menuItem.setVisible(false);
            fragmentManager.beginTransaction().replace(R.id.contentpage, new doclist()).commit();
        }
        
        else if (id == R.id.viewprofile) {
            Intent i=new Intent(getApplicationContext(),parentProfile.class);startActivity(i);finish();
        }
        else if(id==R.id.prev_appointments){
            menuItem.setVisible(false);
            fragmentManager.beginTransaction().replace(R.id.contentpage, new previous_appointment_list()).commit();
        }
        else if (id == R.id.signout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent=new Intent(getApplicationContext(),login.class);startActivity(intent);finish();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
