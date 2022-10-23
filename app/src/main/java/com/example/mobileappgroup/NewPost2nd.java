package com.example.mobileappgroup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class NewPost2nd extends AppCompatActivity implements View.OnClickListener{

    TextView et_newPost2_location, et_newPost2_caption;
    Button btn_newPost2_back, btn_newPost2_next;

    String imageUri, location, caption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post2nd);

        Intent intent = getIntent();
        imageUri = intent.getStringExtra("Image Uri");

        et_newPost2_location = findViewById(R.id.et_newPost2_location);
        et_newPost2_caption = findViewById(R.id.et_newPost2_caption);
        btn_newPost2_back = findViewById(R.id.btn_newPost2_back);
        btn_newPost2_next = findViewById(R.id.btn_newPost2_next);

        btn_newPost2_back.setOnClickListener(this);
        btn_newPost2_next.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == btn_newPost2_back.getId()){
            finish();
        }
        else if (view.getId() == btn_newPost2_next.getId()){
            location = et_newPost2_location.getText().toString();
            caption = et_newPost2_caption.getText().toString();
            Intent intent = new Intent(this, ConfirmPost.class);
            intent.putExtra("Image Uri", imageUri);
            intent.putExtra("Location", location);
            intent.putExtra("Caption", caption);
            startActivity(intent);
        }

    }
}