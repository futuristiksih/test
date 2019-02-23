package com.example.test;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
public class multi_upload extends AppCompatActivity {
    private static final int RESULT_LOAD_IMAGE = 1, REQUEST_CAPTURE_IMAGE = 2;
    private ImageButton mSelectBtn;private RecyclerView mUploadList;
    private List<String> fileNameList, fileDoneList; // List to maintain the Recyler view
    private UploadListAdapter uploadListAdapter;private StorageReference mStorage;String imageFilePath;// image file path for new image created from camera
    FirebaseFirestore db;public FirebaseAuth mAuth;private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);setContentView(R.layout.multi_upload);

        Intent i=getIntent();
        Bundle bundle=i.getBundleExtra("bundle");
        mAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        db.collection("Email").document("parent "+user.getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

            }
        });
        mStorage = FirebaseStorage.getInstance().getReference();
        mSelectBtn = findViewById(R.id.select_btn);mUploadList =findViewById(R.id.upload_list);
        fileNameList = new ArrayList<>();fileDoneList = new ArrayList<>();
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
                    String fileName = getFileName(fileUri);
                    fileNameList.add(fileName);
                    fileDoneList.add("uploading");
                    uploadListAdapter.notifyDataSetChanged();

                    StorageReference fileToUpload = mStorage.child("Images").child(fileName);

                    final int finalI = (fileDoneList.isEmpty()) ? i : i + sz;
//                    Log.i("finalI: ", Integer.toString(finalI));
                    fileToUpload.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            fileDoneList.remove(finalI);
                            fileDoneList.add(finalI, "done");
//                            Log.i("Sucess",":");
//                            Log.i("fileDoneList:", Integer.toString(fileDoneList.size()));
//                            Log.i("fileNameList: ",Integer.toString(fileNameList.size()));
                            uploadListAdapter.notifyDataSetChanged();

                        }
                    });
                    //Toast.makeText(this, "Uploaded "+totalItemsSelected+" files Sucessfully", Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(this, "Selected Multiple Files", Toast.LENGTH_SHORT).show();
            } else if (data.getData() != null)//Selected one images
            {

                int totalItemsSelected = 1;

                for (int i = 0; i < totalItemsSelected; i++) {
                    Uri fileUri = data.getData();

                    String fileName = getFileName(fileUri);

                    fileNameList.add(fileName);
                    fileDoneList.add("uploading");

//                    Log.i("fileDoneList:", Integer.toString(fileDoneList.size()));
//                    Log.i("fileNameList: ", Integer.toString(fileNameList.size()));
                    uploadListAdapter.notifyDataSetChanged();

                    StorageReference fileToUpload = mStorage.child("Images").child(fileName);

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
        // Activity Result for Camera Intent
        else if (requestCode == REQUEST_CAPTURE_IMAGE && resultCode == RESULT_OK) {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 3;
            Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath, options);


            fileNameList.add(imageFilePath);
            fileDoneList.add("uploading");
            uploadListAdapter.notifyDataSetChanged();


            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] byteData = baos.toByteArray();

            final int finalI = (fileDoneList.isEmpty())?0: fileDoneList.size() - 1;


            UploadTask uploadTask = mStorage.child("Images").putBytes(byteData);
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

            galleryAddPic();

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
    // Open the Camera Intent
    private void openCameraIntent() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if(pictureIntent.resolveActivity(getPackageManager()) != null){
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                Log.i("File"," created");
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.multiupload.fileprovider", photoFile);
                Log.i("Photouri",photoURI.toString());
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        photoURI);
                startActivityForResult(pictureIntent,
                        REQUEST_CAPTURE_IMAGE);
            }
        }
    }
    // Create the path uri along for the temperory image File
    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        // File storageDir = Environment.getExternalStorageDirectory();
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        imageFilePath = image.getAbsolutePath();
        Log.i("img: ",imageFilePath);
        return image;
    }
    // Broadcast the saved camera image to gallery
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imageFilePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
        Toast.makeText(this, "Gallery saved", Toast.LENGTH_SHORT).show();
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
                        openCameraIntent();
                    }
                });
        myAlertDialog.show();
    }
}
