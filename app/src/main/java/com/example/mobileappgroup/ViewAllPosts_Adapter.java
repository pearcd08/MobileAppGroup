package com.example.mobileappgroup;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mobileappgroup.Models.Post;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class ViewAllPosts_Adapter extends FirebaseRecyclerAdapter<Post, ViewAllPosts_Holder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ViewAllPosts_Adapter(@NonNull FirebaseRecyclerOptions<Post> options) {
        super(options);
    }

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
    protected void onBindViewHolder(@NonNull ViewAllPosts_Holder holder, int position, @NonNull Post model) {
        holder.txt_userName.setText(model.getUsername());
        holder.txt_location.setText(model.getLocation());
        holder.txt_blurb.setText(model.getBlurb());

        Glide.with(holder.civ_profilePic.getContext())
                .load(model.getProfileURL())
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.civ_profilePic);

        Glide.with(holder.img_photo.getContext())
                .load(model.getImageURL())
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img_photo);

    }

}