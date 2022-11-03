package com.example.mobileappgroup;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewAllPosts_Holder extends RecyclerView.ViewHolder {
    public TextView txt_userName, txt_location, txt_blurb;
    public ImageButton imgBtn_profilePic;
    public ImageView img_photo;

    public ViewAllPosts_Holder(@NonNull View itemView) {
        super(itemView);
        //find the elements in the card_post layout for recycler view
        txt_userName = itemView.findViewById(R.id.textView_post_username);
        txt_location = itemView.findViewById(R.id.textView_post_location);
        txt_blurb = itemView.findViewById(R.id.textView_post_blurb);
        imgBtn_profilePic = itemView.findViewById(R.id.post_profilePic);
        img_photo = itemView.findViewById(R.id.imageview_post_image);
    }
}
