package com.example.mobileappgroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.core.ViewPort;
import androidx.camera.core.impl.PreviewConfig;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.LifecycleOwner;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ImageDecoder;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Rational;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class UploadProfileImageActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnNext, btnBack;
    ImageButton imgbtnCamera, imgbtnGallery, imgbtnCapture;
    ImageView imgProfilePic;
    PreviewView previewView;
    ImageCapture imageCapture;
    ProcessCameraProvider cameraProvider;

    public static final int GALLERY_CODE = 1;
    public static final String SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImg";
    private static final int CAMERA_REQUEST_CODE = 111;
    public static final int CAMERA_CODE = 112;

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private Executor executor = Executors.newSingleThreadExecutor();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_profile_image);

        imgProfilePic = (ImageView) findViewById(R.id.img_uploadProfileImage_image);
        previewView = (PreviewView) findViewById(R.id.preview_camera);

        btnNext = (Button) findViewById(R.id.btn_uploadProfileImage_next);
        btnBack = (Button) findViewById(R.id.btn_uploadProfileImage_back);
        imgbtnCamera = (ImageButton) findViewById(R.id.imgbtn_uploadProfileImage_camera);
        imgbtnGallery = (ImageButton) findViewById(R.id.imgbtn_uploadProfileImage_gallery);
        imgbtnCapture = (ImageButton) findViewById(R.id.imgbtn_uploadProfileImage_capture);
        imgbtnCapture.setVisibility(View.INVISIBLE);

        btnNext.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        imgbtnCamera.setOnClickListener(this);
        imgbtnGallery.setOnClickListener(this);
        imgbtnCapture.setOnClickListener(this);
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
            cameraPermission();
        }
        if (view.getId() == imgbtnGallery.getId()) {
            //open photo gallery
            openGallery();
        }
        if (view.getId() == imgbtnCapture.getId()) {
            takePhoto();
        }

    }

    private void cameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        CAMERA_REQUEST_CODE);
            } else {
                openCamera();
            }

        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_REQUEST_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "Camera permission granted.", Toast.LENGTH_LONG).show();

                // Do stuff here for Action Image Capture.
                openCamera();

            } else {

                Toast.makeText(this, "Camera permission denied.", Toast.LENGTH_LONG).show();

            }

        }
    }


    private void openCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
                previewView.setVisibility(View.VISIBLE);
                imgProfilePic.setVisibility(View.INVISIBLE);
                imgbtnCapture.setVisibility(View.VISIBLE);

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, getExecutor());

    }

    Executor getExecutor() {
        return ContextCompat.getMainExecutor(this);
    }

    private void bindPreview(ProcessCameraProvider cameraProvider) {

        cameraProvider.unbindAll();

        //get the front facing camera
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                .build();

        Preview preview = new Preview.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .setTargetRotation(Surface.ROTATION_0)
                .build();

        cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview, imageCapture);
    }

    private void takePhoto() {
        long timestamp = System.currentTimeMillis();

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, timestamp);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");


        imageCapture.takePicture(
                new ImageCapture.OutputFileOptions.Builder(
                        getContentResolver(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        contentValues
                ).build(),
                getExecutor(),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        Toast.makeText(UploadProfileImageActivity.this, "Photo has been saved successfully.", Toast.LENGTH_SHORT).show();
                        Uri cameraUri = outputFileResults.getSavedUri();
                        Bitmap cameraBitmap = null;

                        try {
                            if (Build.VERSION.SDK_INT < 28) {
                                cameraBitmap = MediaStore.Images.Media
                                        .getBitmap(getContentResolver(), cameraUri);
                            } else {
                                ImageDecoder.Source source = ImageDecoder
                                        .createSource(getContentResolver(), cameraUri);
                                cameraBitmap = ImageDecoder.decodeBitmap(source);
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Bitmap mutableBitmap = cameraBitmap.copy(Bitmap.Config.ARGB_8888, true);

                        Bitmap scaledImage = scaleBitmap(mutableBitmap);
                        cameraProvider.unbindAll();
                        imgbtnCapture.setVisibility(View.INVISIBLE);
                        previewView.setVisibility(View.INVISIBLE);
                        imgProfilePic.setVisibility(View.VISIBLE);
                        imgProfilePic.setImageBitmap(scaledImage);

                    }


                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Toast.makeText(UploadProfileImageActivity.this, "Error saving photo: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );


    }

    private void openGallery() {

        cameraProvider.unbindAll();
        imgProfilePic.setVisibility(View.VISIBLE);
        startActivityForResult(new Intent().setAction(Intent.ACTION_GET_CONTENT)
                .setType("image/*"), GALLERY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_CODE) {
                Uri imageUri = data.getData();
                if (imageUri != null) {
                    cropImage(imageUri);
                }


            } else if (requestCode == CAMERA_CODE) {

                Uri cameraUri = data.getData();
                if (cameraUri != null) {
                    cropImage(cameraUri);
                } else {
                    Toast.makeText(this, "CAMERA CROP ERROR", Toast.LENGTH_SHORT).show();
                }

            } else if (requestCode == UCrop.REQUEST_CROP) {
                Uri imageUriResultCrop = UCrop.getOutput(data);

                if (imageUriResultCrop != null) {

                    imgProfilePic.setImageURI(imageUriResultCrop);
                    Toast.makeText(this, "CROPPED", Toast.LENGTH_SHORT).show();
                }

            }

        }

    }

    private void cropImage(@NonNull Uri uri) {
        //filename will be userid and date
        String destinationFileName = SAMPLE_CROPPED_IMAGE_NAME;
        destinationFileName += ".jpg";

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)));
        uCrop.withAspectRatio(1, 1);

        uCrop.withMaxResultSize(250, 250);

        uCrop.withOptions(selectedOptions());


        uCrop.start(UploadProfileImageActivity.this);


    }

    private UCrop.Options selectedOptions() {
        UCrop.Options options = new UCrop.Options();
        options.setCircleDimmedLayer(true);
        options.setCompressionQuality(50);
        options.setCompressionFormat(Bitmap.CompressFormat.PNG);
        options.setHideBottomControls(false);

        options.setStatusBarColor(getResources().getColor(com.google.android.material.R.color.design_default_color_primary_dark));
        options.setToolbarColor(getResources().getColor(com.google.android.material.R.color.design_default_color_primary));
        options.setToolbarTitle("Crop for Profile Image");
        return options;
    }

    public Bitmap scaleBitmap(Bitmap cameraBitmap) {

        float xScale = (float) 200 / 480;
        float yScale = (float) 200 / 640;
        float scale = Math.max(xScale, yScale);

        float scaledWidth = scale * 480;
        float scaledHeight = scale * 640;

        float left = (200 - scaledWidth) / 2;
        float top = (200 - scaledHeight) / 2;

        // The target rectangle for the new, scaled version of the source bitmap
        RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);

        // Create new Bitmap of desired size and
        Bitmap scaledBitmap = Bitmap.createBitmap(200, 200, cameraBitmap.getConfig());
        Canvas canvas = new Canvas(scaledBitmap);
        canvas.drawBitmap(cameraBitmap, null, targetRect, null);

        //Flip the bitmap as the front facing camera is mirrored
        Matrix matrix = new Matrix();
        matrix.preScale(-1.0f, 1.0f);
        Bitmap flippedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, false);

        return flippedBitmap;
    }


}
