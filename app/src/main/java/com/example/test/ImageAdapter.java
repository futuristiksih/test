package com.example.test;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private StorageReference mStorageRef;
    private Context context;
    private ArrayList<String> uploads,tagList;


    //    private List<Upload> uploads;
    public ImageAdapter(Context context, ArrayList<String> uploads){//List<Upload> uploads) {
        this.uploads = uploads;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_image, parent, false);

        tagList = new ArrayList<String>();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ViewHolder h = holder;
        final String upload = uploads.get(position);

        holder.textViewName.setText(upload);

        mStorageRef.child("Untag_images").child(upload).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(h.imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("error","database not loaded");
            }
        });
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "You Clicked "+upload, Toast.LENGTH_SHORT).show();
                final View child = (LinearLayout)v;
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.prompt_tag, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(promptsView);
                final AutoCompleteTextView textView = (AutoCompleteTextView)promptsView.findViewById(R.id.autoCompleteTextView);

                //textView.setText(holder.textViewName.getText().toString());

                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String s = textView.getText().toString();
                        tagList.add(position,s);
                        h.textViewName.setText(textView.getText());

                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog=builder.create();alertDialog.show();
            }
        });

    }


    public String getItem(int position) {
        return tagList.get(position);
    }
    @Override
    public int getItemCount() {
        return uploads.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder  {

        public TextView textViewName;
        public ImageView imageView;
        public RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativeLayout);
           /* itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(context, ""+getAdapterPosition(), Toast.LENGTH_SHORT).show();

                }
            });*/
        }

    }
    }
