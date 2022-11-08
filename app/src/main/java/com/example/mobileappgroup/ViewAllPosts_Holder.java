package com.example.mobileappgroup;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewAllPosts_Holder extends RecyclerView.ViewHolder {
    public TextView txt_userName, txt_location, txt_blurb, txt_option;
    public CircleImageView civ_profilePic;
    public ImageView img_photo;

    public ViewAllPosts_Holder(@NonNull View itemView) {
        super(itemView);
        //find the elements in the card_post layout for recycler view
        txt_userName = itemView.findViewById(R.id.textView_post_username);
        txt_location = itemView.findViewById(R.id.textView_post_location);
        txt_blurb = itemView.findViewById(R.id.textView_post_blurb);
        civ_profilePic = itemView.findViewById(R.id.post_profilePic);
        img_photo = itemView.findViewById(R.id.imageview_post_image);
        txt_option = itemView.findViewById(R.id.txt_option);
    }
}
