package com.example.mobileappgroup;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewAllPosts_Adapter extends RecyclerView.Adapter<ViewAllPosts_Holder> {

    @NonNull
    @Override
    public ViewAllPosts_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View view = inflater.inflate(R.layout.card_post,
                parent, false);
        return new ViewAllPosts_Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewAllPosts_Holder holder, int position) {
        //holder.txt_userName.setText =
        //holder.txt_location.setText =
        //holder.txt_blurb.setText =
        //holder.imgBtn_profilePic.setImageURI();
        //holder.img_photo.setImageURI();

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}