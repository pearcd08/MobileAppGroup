package com.example.mobileappgroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class UploadProfileImageActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnNext, btnBack;
    ImageButton imgbtnCamera, imgbtnGallery;
    ImageView imgProfilePic;
    //the photo to pass to the user database
    Bitmap selectedPic;

    public static final int GALLERY_CODE = 1;
    public static final int CAMERA_CODE = 112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_profile_image);

        imgProfilePic = (ImageView) findViewById(R.id.img_uploadProfileImage_image);

        btnNext = (Button) findViewById(R.id.btn_uploadProfileImage_next);
        btnBack = (Button) findViewById(R.id.btn_uploadProfileImage_back);
        imgbtnCamera = (ImageButton) findViewById(R.id.imgbtn_uploadProfileImage_camera);
        imgbtnGallery = (ImageButton) findViewById(R.id.imgbtn_uploadProfileImage_gallery);

        btnNext.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        imgbtnCamera.setOnClickListener(this);
        imgbtnGallery.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == btnNext.getId()) {
            // confirm image is uploaded
            // create user on firebase
        }
        if (view.getId() == btnBack.getId()) {
            //go back to registration step 2
        }
        if (view.getId() == imgbtnCamera.getId()) {
            //open camera intent
            openCamera();
        }
        if (view.getId() == imgbtnGallery.getId()) {
            //open photo gallery
            openGallery();
        }

    }


    private void openCamera() {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMERA_CODE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void openGallery() {
        try {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_CODE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_CODE) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            selectedPic = photo;
            imgProfilePic.setImageBitmap(photo);
        } else if (requestCode == GALLERY_CODE) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            selectedPic = photo;
            imgProfilePic.setImageBitmap(photo);
        }
    }


}