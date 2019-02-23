package com.example.test;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class UploadListAdapter extends RecyclerView.Adapter<UploadListAdapter.ViewHolder> {

    public List<String> fileNameList, fileDoneList;

    public UploadListAdapter(List<String> fileNameList, List<String>fileDoneList){
        this.fileDoneList = fileDoneList;
        this.fileNameList = fileNameList;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_single, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder( ViewHolder viewHolder, int i) {

        String fileName = fileNameList.get(i);
        viewHolder.fileNameView.setText(fileName);
        String fileDone = fileDoneList.get(i);

        if(fileDone.equals("uploading")){

            viewHolder.fileDoneView.setImageResource(R.mipmap.progress);

        } else {

            viewHolder.fileDoneView.setImageResource(R.mipmap.checked);

        }

    }

    @Override
    public int getItemCount() {
        return fileNameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public TextView fileNameView;
        public ImageView fileDoneView;

        public ViewHolder(View itemView){
            super(itemView);

            mView = itemView;

            fileNameView = (TextView) mView.findViewById(R.id.upload_filename);
            fileDoneView = (ImageView) mView.findViewById(R.id.upload_loading);
        }
    }
}
