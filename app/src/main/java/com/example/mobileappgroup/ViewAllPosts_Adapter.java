package com.example.mobileappgroup;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mobileappgroup.Models.Post;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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


        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uID = currentUser.getUid();
        System.out.println("test-uid" + uID);
        String userID = model.getUserID();
        System.out.println("test-uid2" + userID);

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

        if (userID.equals(uID)) {
            System.out.println("test-if");
            holder.txt_option.setOnClickListener(view -> {
                PopupMenu popupMenu = new PopupMenu(view.getContext(), holder.txt_option);
                popupMenu.inflate(R.menu.option_menu);
                popupMenu.setOnMenuItemClickListener(menuItem -> {
                    switch (menuItem.getItemId()) {
                        case R.id.menu_edit:

                            String postFullAddress = String.valueOf(FirebaseDatabase.getInstance().getReference().child("Posts")
                                    .child(getRef(holder.getAdapterPosition()).getKey()));
                            String[] postAddressArray = postFullAddress.split("/");
                            String postID = postAddressArray[postAddressArray.length - 1];
                            Intent intent = new Intent(view.getContext(), EditPost.class);
                            intent.putExtra("postID", postID);
                            intent.putExtra("userID", model.getUserID());
                            view.getContext().startActivity(intent);
                            break;

                        case R.id.menu_delete:
                            AlertDialog.Builder builder = new AlertDialog.Builder(holder.txt_userName.getContext());
                            builder.setTitle("Are you sure?");
                            builder.setMessage("Are you sure to delete the data?");

                            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    FirebaseDatabase.getInstance().getReference().child("Posts")
                                            .child(getRef(holder.getAdapterPosition()).getKey()).removeValue();
                                }
                            });

                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(holder.txt_userName.getContext(), "Cancelled", Toast.LENGTH_LONG).show();
                                }
                            });
                            builder.show();
                            break;
                    }
                    return false;
                });
                popupMenu.show();
            });
        }



    }

}