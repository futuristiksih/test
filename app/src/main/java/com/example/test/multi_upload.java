package com.example.test;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
public class multi_upload extends AppCompatActivity {
    String immunization,bowel_movement,fever,inception,infected_area,intake,environment,crying,doc_name,doc_email,dob,birth_weight,child_name,gender,
            vomit,breast_feed,dehydration,img_filename;
    private static final int RESULT_LOAD_IMAGE = 1, REQUEST_CAPTURE_IMAGE = 2, CAMERA_RESULT= 3;
    private Button mSelectBtn;
    private RecyclerView mUploadList;
    private ProgressBar mMessageProgress;
    private List<String> fileNameList, fileDoneList,fileUrlList; // List to maintain the Recyler view
    private UploadListAdapter uploadListAdapter;
    private StorageReference mStorage;String imageFilePath;// image file path for new image created from camera
    FirebaseFirestore db;
    String getDate(){
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat sdformat=new SimpleDateFormat("ddMMyyyy");
        return sdformat.format(calendar.getTime());
    }

    public void final_upload(View view){
        mMessageProgress.setVisibility(View.VISIBLE);
        db=FirebaseFirestore.getInstance();final FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        final objectChild Child=new objectChild(child_name,dob,gender,birth_weight);

        final DocumentReference parentReference = db.collection("Email").document("parent "+user.getEmail());
        parentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                final String count = documentSnapshot.getString("count");
                final String guardian = documentSnapshot.getString("name");
                final String doc_count=""+(String.valueOf(Integer.parseInt(count)+1));
                final DocumentReference childReference=parentReference.collection("sent_appointments").document(child_name+" "+doc_email);
                childReference.set(Child).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        final childDetails child = new childDetails(immunization, bowel_movement, fever, inception, infected_area, intake, environment, crying, vomit, breast_feed, dehydration);
                        childReference.collection("Dates").document("dp_"+getDate()+"_"+doc_count).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(final DocumentSnapshot documentSnapshot) {
                                Map<String,Object> details=new HashMap<>();
                                details.put("child_details",child);
                                Map<String, Object> diagnosis = new HashMap<>();
                                diagnosis.put("diagnosis",details);
                                childReference.collection("Dates").document(documentSnapshot.getId()).set(diagnosis);
                                for(int i = 0;i < fileNameList.size();i++){
                                    Map<String,Object> n = new HashMap<>();
                                    n.put("filename",fileNameList.get(i));
                                    childReference.collection("Dates").document(documentSnapshot.getId())
                                            .collection("Untag_images").document(""+i).set(n)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(multi_upload.this, "Done", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(multi_upload.this, "Not Done", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                                myPatients patients=new myPatients(child_name,"dp_"+getDate()+"_"+doc_count,guardian,"pending",getDate());
                                myDetails det=new myDetails("dp_"+getDate()+"_"+doc_count,doc_name,"pending",getDate());
                                db.collection("Email").document("doctor "+doc_email).collection("received_appointments").document(child_name+" "+user.getEmail()).set(patients).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getApplicationContext(),"APPOINTMENT REQUEST SENT WITH CHILD'S DETAILS",Toast.LENGTH_LONG).show();
                                    }
                                });
                                childReference.set(det, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Intent i=new Intent(getApplicationContext(),parentProfile.class);startActivity(i);finish();
                                        mMessageProgress.setVisibility(View.INVISIBLE);
                                    }
                                });

                            }
                        });
                    }
                });
                Map<String,Object> notificationMessage = new HashMap<>();
                notificationMessage.put("message",child_name+" requested appointment.");
                notificationMessage.put("from","parent "+user.getEmail());
                db.collection("Email/"+"doctor "+doc_email+"/Notifications").document(child_name+"_"+getDate()).set(notificationMessage)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(multi_upload.this, "Notification Sent.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(multi_upload.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });
                db.collection("Email").document("parent " + user.getEmail()).update("count", doc_count);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);setContentView(R.layout.multi_upload);

        mMessageProgress = findViewById(R.id.messageProgress);

        Intent i=getIntent();
        Bundle bundle=i.getBundleExtra("bundle");
        doc_name=bundle.getString("doc_name");
        doc_email=bundle.getString("doc_email");
        birth_weight=bundle.getString("birth_weight");
        child_name=bundle.getString("child_name");
        dob=bundle.getString("dob");
        gender=bundle.getString("gender");

        breast_feed=bundle.getString("breast_feed");crying=bundle.getString("crying");vomit=bundle.getString("vomit");
        dehydration=bundle.getString("dehydration");environment=bundle.getString("environment");immunization=bundle.getString("immunization");
        intake=bundle.getString("intake");bowel_movement=bundle.getString("bowel_movement");fever=bundle.getString("fever");
        inception=bundle.getString("inception");infected_area=bundle.getString("infected_area");

        mStorage = FirebaseStorage.getInstance().getReference();
        mSelectBtn = findViewById(R.id.select_btn);mUploadList =findViewById(R.id.upload_list);
        fileNameList = new ArrayList<>();fileDoneList = new ArrayList<>();fileUrlList = new ArrayList<>();
        uploadListAdapter = new UploadListAdapter(fileNameList, fileDoneList);

        //Set the Recycler View adapter
        mUploadList.setLayoutManager(new LinearLayoutManager(this));
        mUploadList.setHasFixedSize(true);
        mUploadList.setAdapter(uploadListAdapter);
        //Opening the Dialog box for Camera and Gallery Intent
        mSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDialog();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Activity Result for Gallery intent
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
            if (data.getClipData() != null) {// Return value only when no. of images>1
                int totalItemsSelected = data.getClipData().getItemCount();
                int sz = fileDoneList.size();
                Log.i("sz:", Integer.toString(sz));
                for (int i = 0; i < totalItemsSelected; i++) {
                    Uri fileUri = data.getClipData().getItemAt(i).getUri();
                    final String fileName = getFileName(fileUri);
                    fileNameList.add(fileName);
                    fileDoneList.add("uploading");
                    uploadListAdapter.notifyDataSetChanged();

                    final StorageReference fileToUpload = mStorage.child("Untag_images").child(fileName);

                    final int finalI = (fileDoneList.isEmpty()) ? i : i + sz;
                    fileToUpload.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            fileDoneList.remove(finalI);
                            fileDoneList.add(finalI, "done");
                            uploadListAdapter.notifyDataSetChanged();

                        }
                    });

                }
            }
            else if (data.getData() != null)//Selected one images
            {

                int totalItemsSelected = 1;

                for (int i = 0; i < totalItemsSelected; i++) {
                    Uri fileUri = data.getData();

                    String fileName = getFileName(fileUri);

                    fileNameList.add(fileName);
                    fileDoneList.add("uploading");

                    uploadListAdapter.notifyDataSetChanged();

                    StorageReference fileToUpload = mStorage.child("Untag_images").child(fileName);

                    final int finalI = (fileDoneList.isEmpty()) ? i : i + fileDoneList.size() - 1;
                    Log.i("finalI: ", Integer.toString(finalI));
                    fileToUpload.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            fileDoneList.remove(finalI);
                            fileDoneList.add(finalI, "done");
//                            Log.i("fileDoneList:", Integer.toString(fileDoneList.size()));
//                            Log.i("fileNameList: ", Integer.toString(fileNameList.size()));
                            uploadListAdapter.notifyDataSetChanged();

                        }
                    });

                }
            }
        }

        else if(requestCode == CAMERA_RESULT && resultCode == RESULT_OK){


            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            String timeStamp =
                    new SimpleDateFormat("yyyyMMdd_hhmmss",
                            Locale.getDefault()).format(new Date());
            String imageFileName = "IMG_" + timeStamp;
            String fn = imageFileName+".jpeg";
            fileNameList.add(fn);
            fileDoneList.add("uploading");
            uploadListAdapter.notifyDataSetChanged();


            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            byte[] byteData = baos.toByteArray();

            final int finalI = (fileDoneList.isEmpty())?0: fileDoneList.size() - 1;


            UploadTask uploadTask = mStorage.child("Untag_images/"+fn).putBytes(byteData);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads

                    Toast.makeText(multi_upload.this, "Upload failed " + exception , Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    fileDoneList.remove(finalI);
                    fileDoneList.add(finalI, "done");
                    uploadListAdapter.notifyDataSetChanged();

                    Toast.makeText(multi_upload.this, "Upload success", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
    //Function to get the filename For the Gallery Intent Result
    public String getFileName(Uri uri){
        String result = null;
        if (uri.getScheme().equals("content")){
            Cursor cursor = getContentResolver().query(uri, null, null, null,null);
            try{
                if (cursor != null && cursor.moveToFirst()){
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null){
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1){
                result = result.substring(cut + 1);
            }
        }
        return  result;
    }



    // Open the Dialog box for camera and gallery intent
    public void startDialog() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(multi_upload.this);
        myAlertDialog.setTitle("Upload Pictures Option");
        myAlertDialog.setMessage("How do you want to set your picture?");
        myAlertDialog.setPositiveButton("Gallery",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"),RESULT_LOAD_IMAGE);
                    }
                });
        myAlertDialog.setNegativeButton("Camera",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        //openCameraIntent();

                        if(checkSelfPermission(Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                            requestPermissions(new String[]{Manifest.permission.CAMERA},CAMERA_RESULT);
                        }
                        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(it, CAMERA_RESULT);


                    }
                });
        myAlertDialog.show();
    }
}
