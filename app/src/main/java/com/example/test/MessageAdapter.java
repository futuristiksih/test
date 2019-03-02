package com.example.test;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{
    private List<Messages> mMessageList;FirebaseFirestore db;FirebaseUser user;
    private DatabaseReference mUserDatabase;
    public MessageAdapter(List<Messages> mMessageList) {
        this.mMessageList = mMessageList;
    }
    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_single_layout, parent, false);
        return new MessageViewHolder(v);
    }
    public class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView messageText;public CircleImageView profileImage;public TextView displayName;public ImageView messageImage;
        public MessageViewHolder(View view) {
            super(view);
            messageText = view.findViewById(R.id.message_text_layout);
            profileImage = view.findViewById(R.id.message_profile_layout);
            displayName = view.findViewById(R.id.name_text_layout);
            messageImage = view.findViewById(R.id.message_image_layout);
        }
    }
    @Override
    public void onBindViewHolder(final MessageViewHolder viewHolder, int i) {
        Messages c = mMessageList.get(i);String from_user = c.getFrom(),message_type = c.getType();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(from_user);
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {//current user name
                String name=dataSnapshot.child("name").getValue().toString();
                viewHolder.displayName.setText(name);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
        if(message_type.equals("text")) {
            viewHolder.messageText.setText(c.getMessage());viewHolder.messageImage.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.messageText.setVisibility(View.INVISIBLE);
        }
    }
    @Override
    public int getItemCount() {
        return mMessageList.size();
    }
}
