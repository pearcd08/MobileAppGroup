package com.example.mobileappgroup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class NewPost extends AppCompatActivity implements View.OnClickListener{

    ImageButton img_btn_uploadPostImage_camera, img_btn_uploadPostImage_gallery;
    ImageView imageview_post_image;
    Button btn_newPost_back, btn_newPost_next;
    Uri selectedImageUri;
    String userID;
    // For gallery
    int SELECT_PICTURE = 200;
    // For camera
    private static final int TAKE_PICTURE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        img_btn_uploadPostImage_gallery = findViewById(R.id.img_btn_uploadPostImage_gallery);
        img_btn_uploadPostImage_camera = findViewById(R.id.img_btn_uploadPostImage_camera);
        imageview_post_image = findViewById(R.id.imageview_post_image);
        btn_newPost_back = findViewById(R.id.btn_newPost_back);
        btn_newPost_next = findViewById(R.id.btn_newPost_next);

        img_btn_uploadPostImage_gallery.setOnClickListener(this);
        img_btn_uploadPostImage_camera.setOnClickListener(this);
        btn_newPost_back.setOnClickListener(this);
        btn_newPost_next.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == img_btn_uploadPostImage_gallery.getId()){
            imageChooser();
        }
        else if (view.getId() == img_btn_uploadPostImage_camera.getId()){
            imageTaker();
        }
        else if (view.getId() == btn_newPost_back.getId()){
            finish();
        }
        else if (view.getId() == btn_newPost_next.getId()){
            Intent next_page_intent = new Intent();
            next_page_intent.setClass(this, NewPost2nd.class);
            next_page_intent.putExtra("Image Uri", selectedImageUri.toString());
            startActivity(next_page_intent);
        }

    }

    private void imageTaker(){
        // Create the camera intent to open the camera and capture the image.
        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Start activity, and pass through pic_id to compare with the returned request code.
        startActivityForResult(camera_intent, TAKE_PICTURE);

    }

    private void imageChooser() {
        Intent gallery_intent = new Intent();
        // Declare intent type to be image.
        gallery_intent.setType("image/*");
        gallery_intent.setAction(Intent.ACTION_GET_CONTENT);
        // Pass the SELECT_PICTURE constant so that it can be compared with the returned request code.
        startActivityForResult(Intent.createChooser(gallery_intent, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            // compare the returned requestCode with the SELECT_PICTURE constant.
            if (requestCode == SELECT_PICTURE){
                // get the URL of the image from the returned data.
                selectedImageUri = data.getData();
                if (null != selectedImageUri){
                    // update the image in the layout.
                    imageview_post_image.setImageURI(selectedImageUri);
                }
            }
            else if (requestCode == TAKE_PICTURE){
                // get the photo just taken
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                selectedImageUri = data.getData();
                // update the image in the layout
                imageview_post_image.setImageBitmap(photo);
                selectedImageUri = getImageUri(getApplicationContext(), photo);
            }
        }

    }

    private Uri getImageUri(Context applicationContext, Bitmap photo) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.
                insertImage(applicationContext.getContentResolver(), photo,
                        "Title", null);
        return Uri.parse(path);
    }


}