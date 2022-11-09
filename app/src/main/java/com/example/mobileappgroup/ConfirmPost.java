package com.example.mobileappgroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mobileappgroup.Models.Post;
import com.example.mobileappgroup.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ConfirmPost extends AppCompatActivity implements View.OnClickListener {

    Uri imageUri;
    String imageUriStr;
    String caption;
    String location;
    String userID;
    TextView tv_confirmPost_Location, tv_confirmPost_Username, tv_confirmPost_Caption;
    ImageView img_confirmPost_Photo, img_confirmPost_ProfilePic;
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
        imageUriStr = intent.getStringExtra("Image Uri");
        caption = intent.getStringExtra("Caption");
        location = intent.getStringExtra("Location");
        longitude = intent.getDoubleExtra("Longitude", 0);
        latitude = intent.getDoubleExtra("Latitude", 0);


        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();

        getUser();
Log.d("LINE_INFO", "hit line 56");
        Log.d("CURRENT_USER", userID);

        tv_confirmPost_Username = findViewById(R.id.tv_confirmPost_Username);
        tv_confirmPost_Location = findViewById(R.id.tv_confirmPost_Location);
        tv_confirmPost_Caption = findViewById(R.id.tv_confirmPost_Caption);
        img_confirmPost_Photo = findViewById(R.id.img_confirmPost_photo);
        img_confirmPost_ProfilePic = findViewById(R.id.img_confirmPost_ProfilePic);
        btn_confirmPost_back = findViewById(R.id.btn_confirmPost_back);
        btn_confirmPost_post = findViewById(R.id.btn_confirmPost_post);

        tv_confirmPost_Location.setText(location);
        tv_confirmPost_Caption.setText(caption);
        img_confirmPost_Photo.setImageURI(Uri.parse(imageUriStr));

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
                    tempUser.setUID(data.getKey());
                    if (tempUser.getUID().equals(userID)) {
                        user = tempUser;
                        tv_confirmPost_Username.setText(user.getUsername());
                        if (user.getProfileURL().length() == 0) {
                            img_confirmPost_ProfilePic.setImageResource(R.drawable.ic_baseline_person);
                        }
                        else {
                            Glide.with(img_confirmPost_ProfilePic.getContext())
                                    .load(user.getProfileURL())
                                    .circleCrop()
                                    .into(img_confirmPost_ProfilePic);
                        }
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
            uploadImage();
        }

    }

    private void uploadImage() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading Post...");
        progressDialog.show();

        imageUri = Uri.parse(imageUriStr);
        if (imageUri != null){
            StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                    .child("post_images")
                    .child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            addPost(url);
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Post uploaded.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), ViewAllPosts.class);
                            startActivity(intent);
                        }
                    });
                }
            });
        }
    }

    private void addPost(String url) {
        Post post = new Post();
        post.setBlurb(caption);
        post.setUsername(user.getUsername());
        post.setUserID(user.getUID());
        post.setLocation(location);
        post.setLongitude(longitude);
        post.setLatitude(latitude);
        post.setPostImageURL(url);
        post.setProfileURL(user.getProfileURL());
        PostDAO postDAO = new PostDAO();
        postDAO.add(post);
    }

    private String getFileExtension(Uri imageUri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
    }
}