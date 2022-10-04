package com.example.mobileappgroup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class ViewAllPosts extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_userName;
    private ImageButton imgBtn_profilePic, imgBtn_newPost, imgBtn_editProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_posts);

        imgBtn_profilePic = findViewById(R.id.imageButton_profileCard_profilePic);
        imgBtn_profilePic.setOnClickListener(this);

        imgBtn_editProfile = findViewById(R.id.imageButton_profileCard_editProfile);
        imgBtn_editProfile.setOnClickListener(this);

        imgBtn_newPost = findViewById(R.id.imageButton_profileCard_newPost);
        imgBtn_newPost.setOnClickListener(this);

    }

    private void loadUser(){
        //load user info into profile card
    }

    private void loadPosts(){
        //load all posts into a recyclerview
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageButton_profileCard_profilePic:
                //open users posts
                break;
            case R.id.imageButton_profileCard_newPost:
                //open new post
                openNewPost();
                break;
            case R.id.imageButton_profileCard_editProfile:
                //open edit profile
                openEditProfile();
                break;

        }
    }

    private void openNewPost() {
        Intent i = new Intent(this, NewPost.class);
        startActivity(i);
    }

    private void openEditProfile() {
        Intent i = new Intent(this, EditProfile.class);
        startActivity(i);
    }


}