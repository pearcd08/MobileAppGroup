package com.example.mobileappgroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mobileappgroup.Models.Location;
import com.example.mobileappgroup.Models.Post;
import com.example.mobileappgroup.rest.LocationControllerRESTAPI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class EditPost extends AppCompatActivity implements View.OnClickListener {

    String userID;
    TextView tv_editPost_Location, tv_editPost_Username, tv_editPost_Caption;
    ImageView img_editPost_Photo, img_editPost_ProfilePic;
    Button btn_editPost_back, btn_editPost_update;
    PostDAO postDAO = new PostDAO();
    Post post = new Post();
    String postID;
    LocationControllerRESTAPI locationControllerRESTAPI = new LocationControllerRESTAPI();;
    private Double longitude;
    private Double latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String currentUser = firebaseAuth.getCurrentUser().getUid();
        Intent intent = getIntent();
        postID = intent.getStringExtra("postID");
        userID = intent.getStringExtra("userID");

        if (currentUser.equals(userID)) {
            tv_editPost_Username = findViewById(R.id.tv_editPost_Username);
            tv_editPost_Location = findViewById(R.id.tv_editPost_Location);
            tv_editPost_Caption = findViewById(R.id.tv_editPost_Caption);
            img_editPost_Photo = findViewById(R.id.img_editPost_photo);
            img_editPost_ProfilePic = findViewById(R.id.img_editPost_ProfilePic);
            btn_editPost_back = findViewById(R.id.btn_editPost_back);
            btn_editPost_update = findViewById(R.id.btn_editPost_update);

            postDAO.get(postID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    post = snapshot.getValue(Post.class);
                    tv_editPost_Location.setText(post.getLocation());
                    tv_editPost_Caption.setText(post.getBlurb());
                    tv_editPost_Username.setText(post.getUsername());
                    Glide.with(img_editPost_ProfilePic.getContext())
                            .load(post.getProfileURL())
                            .circleCrop()
                            .into(img_editPost_ProfilePic);
                    Glide.with(img_editPost_Photo.getContext())
                            .load(post.getImageURL())
                            .into(img_editPost_Photo);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("POST_ERROR", error.toString());
                }
            });


            btn_editPost_back.setOnClickListener(this);
            btn_editPost_update.setOnClickListener(this);

            tv_editPost_Location.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    locationControllerRESTAPI.start(getApplicationContext(),
                            tv_editPost_Location.getText().toString());
                }
            });
        }
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == btn_editPost_back.getId()) {
            finish();
        } else if (view.getId() == btn_editPost_update.getId()) {
            HashMap<String, Object> postHashmap = new HashMap<>();
            postHashmap.put("username", post.getUsername());
            postHashmap.put("userID", post.getUserID());
            postHashmap.put("imageURL", post.getImageURL());
            postHashmap.put("profileURL", post.getProfileURL());
            postHashmap.put("location", tv_editPost_Location.getText().toString());
            postHashmap.put("blurb", tv_editPost_Caption.getText().toString());
//            Location location = locationControllerRESTAPI.getLocation();
            postHashmap.put("longitude", post.getLongitude());
            postHashmap.put("latitude", post.getLatitude());

            postDAO.update(postID, postHashmap);
            Toast.makeText(getApplicationContext(), "Post updated.", Toast.LENGTH_SHORT).show();
            finish();



        }
    }




}