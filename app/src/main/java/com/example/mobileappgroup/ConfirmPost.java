package com.example.mobileappgroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mobileappgroup.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ConfirmPost extends AppCompatActivity implements View.OnClickListener {

    String imageUri;
    String caption;
    String location;
    String userID;
    TextView tv_confirmPost_Location, tv_confirmPost_Username, tv_confirmPost_Caption;
    ImageView img_confirmPost_Photo;
    Button btn_confirmPost_back, btn_confirmPost_post;
    double longitude;
    double latitude;
    FirebaseAuth firebaseAuth;
    UserDAO userDAO = new UserDAO();
    User user = new User();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_post);

        Intent intent = getIntent();
        imageUri = intent.getStringExtra("Image Uri");
        caption = intent.getStringExtra("Caption");
        location = intent.getStringExtra("Location");
        longitude = intent.getDoubleExtra("Longitude", 0);
        latitude = intent.getDoubleExtra("Latitude", 0);


        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getCurrentUser().toString();

        getUser();
Log.d("LINE_INFO", "hit line 56");
        Log.d("CURRENT_USER", userID);
        tv_confirmPost_Username = findViewById(R.id.tv_confirmPost_Username);
        tv_confirmPost_Location = findViewById(R.id.tv_confirmPost_Location);
        tv_confirmPost_Caption = findViewById(R.id.tv_confirmPost_Caption);
        img_confirmPost_Photo = findViewById(R.id.img_confirmPost_photo);
        btn_confirmPost_back = findViewById(R.id.btn_confirmPost_back);
        btn_confirmPost_post = findViewById(R.id.btn_confirmPost_post);

        tv_confirmPost_Username.setText(user.getUsername());
        tv_confirmPost_Location.setText(location);
        tv_confirmPost_Caption.setText(caption);
        img_confirmPost_Photo.setImageURI(Uri.parse(imageUri));

        btn_confirmPost_back.setOnClickListener(this);
        btn_confirmPost_post.setOnClickListener(this);
    }

    private void getUser() {
        Log.d("LINE_INFO", "hit line 75");
        userDAO.get().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    User tempUser = data.getValue(User.class);
                    if (tempUser.getUID().equals(userID)) {
                        user = tempUser;
                        break;
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == btn_confirmPost_back.getId()) {
            finish();
        } else if (view.getId() == btn_confirmPost_post.getId()) {

        }

    }
}