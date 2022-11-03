package com.example.mobileappgroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mobileappgroup.Models.Post;
import com.example.mobileappgroup.Models.User;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewAllPosts extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener  {

    private TextView tv_userName;
    private ImageButton imgBtn_newPost, imgBtn_editProfile;
    private CircleImageView profilePic;
    private RecyclerView recyclerview;
    private String userName, userProfile;
    ViewAllPosts_Adapter allPostsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_posts);

        profilePic = findViewById(R.id.profileCard_profilePic);
        profilePic.setOnClickListener(this);

        imgBtn_editProfile = findViewById(R.id.imageButton_profileCard_editProfile);
        imgBtn_editProfile.setOnClickListener(this);

        imgBtn_newPost = findViewById(R.id.imageButton_profileCard_newPost);
        imgBtn_newPost.setOnClickListener(this);

        recyclerview = findViewById(R.id.recyclerView);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        tv_userName = findViewById(R.id.textView_post_username);

        loadUser();

        FirebaseRecyclerOptions<Post> options =
                new FirebaseRecyclerOptions.Builder<Post>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Posts"), Post.class)
                        .build();

        allPostsAdapter = new ViewAllPosts_Adapter(options);
        recyclerview.setAdapter(allPostsAdapter);
    }


    private void loadUser(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        //String uID = currentUser.getUid();
        String email = currentUser.getEmail();
        //System.out.println("test-email" + email);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (ds.child("email").getValue().equals(email)) {
                        userName = ds.child("username").getValue(String.class);
                        System.out.println("test-name" + userName);
                        userProfile = ds.child("profileURL").getValue(String.class);
                }

                    Glide.with(profilePic.getContext())
                            .load(userProfile)
                            .circleCrop()
                            .into(profilePic);

                        tv_userName.setText(userName);
                    }
                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewAllPosts.this, "Something wrong happens", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void loadPosts(){
        //load all posts into a recyclerview
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.profileCard_profilePic:
                //open users posts
                break;
            case R.id.imageButton_profileCard_newPost:
                //open new post
                openNewPost();
                break;
            case R.id.imageButton_profileCard_editProfile:
                //open edit profile
                showMenu(view);
                break;

        }
    }

    private void openNewPost() {
        Intent i = new Intent(this, NewPost.class);
        startActivity(i);
    }

    private void openEditProfile() {
        //Intent i = new Intent(this, EditProfile.class);
        Intent i = new Intent(this, UploadProfileImageActivity.class);
        startActivity(i);
    }

    private void showMenu(View v){
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.profile_menu);
        popup.show();

    }

    private void logout(){
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    private void reload() { }

    @Override
    protected void onStart() {
        super.onStart();
        allPostsAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        allPostsAdapter.stopListening();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_updateProfile:
                openEditProfile();
                return true;
            case R.id.menu_logout:
                logout();
                return true;
            default:
                return false;

        }
    }
}