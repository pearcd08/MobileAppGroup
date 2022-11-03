package com.example.mobileappgroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static String loginId;
    private TextView tvPasswordReset;
    private EditText etEmailAddress, etPassword;
    private Button signIn, register;
    private ImageButton showPassword;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register = (Button) findViewById(R.id.btn_register);
        register.setOnClickListener(this);
        tvPasswordReset = (TextView) findViewById(R.id.tv_passwordReset);
        tvPasswordReset.setOnClickListener(this);
        signIn = (Button) findViewById(R.id.btn_userLogin);
        signIn.setOnClickListener(this);
        showPassword = (ImageButton) findViewById(R.id.imageButton_showPassword);
        showPassword.setOnClickListener(this);

        //you can delete this, just for testing
        etEmailAddress = (EditText) findViewById(R.id.et_userEmail);
        etPassword = (EditText) findViewById(R.id.et_userPassword);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == register.getId()) {
            startActivity(new Intent(this, RegistrationActivity.class));
        } else if (view.getId() == signIn.getId()) {
            userLogin();
        } else if (view.getId() == tvPasswordReset.getId()) {
            startActivity(new Intent(this, PasswordResetActivity.class));
        }
        else if (view.getId() == tvPasswordReset.getId()) {
            startActivity(new Intent(this, PasswordResetActivity.class));
        }
        else if(view.getId() == showPassword.getId()){
            if (etPassword.getTransformationMethod()
                    .equals(HideReturnsTransformationMethod.getInstance())){
                etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());


            }else{
                etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }



        }

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        } else {
        }
    }


    private void userLogin() {
        //String email = etEmailAddress.getText().toString().trim();
        //String password = etPassword.getText().toString().trim();
        String email = "test3@gmail.com";
        String password = "123456";
        if (email.isEmpty()) {
            etEmailAddress.setError("Please enter the email address");
            etEmailAddress.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmailAddress.setError("Please enter a valid email!");
            etEmailAddress.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Please enter the password");
            etPassword.requestFocus();
            return;
        }

        if (password.length()  <4 || password.length() > 8) {
            etPassword.setError("Please enter the correct password");
            etPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                FirebaseUser uUser = mAuth.getCurrentUser();
                updateUI(uUser);
                MainActivity.loginId = uUser.getUid();
                System.out.println("loginID = " + loginId);
                startActivity(new Intent(MainActivity.this, ViewAllPosts.class));
            } else {
                Toast.makeText(MainActivity.this, "Failed to login! Please check your credentials", Toast.LENGTH_LONG).show();
            }
            progressBar.setVisibility(View.GONE);
        });
    }

    private void reload() { }

    private void updateUI(FirebaseUser uUser) {

    }


}