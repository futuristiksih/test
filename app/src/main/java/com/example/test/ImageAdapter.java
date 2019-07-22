package com.example.test;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private StorageReference mStorageRef;private Context context;private ArrayList<String> uploads,tagList;
    ImageAdapter(Context context, ArrayList<String> uploads){
        this.uploads = uploads;this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_image, parent, false);
        tagList = new ArrayList<>();mStorageRef = FirebaseStorage.getInstance().getReference();
        return new ViewHolder(v);
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final String upload = uploads.get(position);
        final String msg = "Tag "+String.valueOf(position);
        holder.textViewName.setText(msg);
        mStorageRef.child("Untag_images").child(upload).getDownloadUrl().addOnSuccessListener(uri ->
                Glide.with(context).load(uri).into(holder.imageView)).addOnFailureListener(e -> Log.e("error","database not loaded"));
        holder.relativeLayout.setOnClickListener(v -> {
            LayoutInflater li = LayoutInflater.from(context);
            View promptsView = li.inflate(R.layout.prompt_tag, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setView(promptsView);
            final AutoCompleteTextView textView =promptsView.findViewById(R.id.autoCompleteTextView);
            builder.setPositiveButton("Add", (dialog, which) -> {
                String s = textView.getText().toString();
                tagList.add(position,s);
                holder.textViewName.setText(textView.getText());
            });
            builder.setNegativeButton("cancel", (dialog, which) -> dialog.cancel());
            AlertDialog alertDialog=builder.create();alertDialog.show();
        });
    }
    String getItem(int position) { return tagList.get(position); }
    int getTagCount(){ return tagList.size();}
    @Override
    public int getItemCount() { return uploads.size(); }
    class ViewHolder extends RecyclerView.ViewHolder  {
        TextView textViewName;ImageView imageView;RelativeLayout relativeLayout;
        ViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            imageView = itemView.findViewById(R.id.imageView);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
        }
    }
}
