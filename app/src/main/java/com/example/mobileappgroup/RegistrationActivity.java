package com.example.mobileappgroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mobileappgroup.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private Button login, register;
    private EditText userEmail, userName, userPassword;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        login = (Button) findViewById(R.id.btn_login);
        register = (Button) findViewById(R.id.btn_userRegister);
        login.setOnClickListener(this);
        register.setOnClickListener(this);

        userEmail = (EditText) findViewById(R.id.et_userEmail);
        userName = (EditText) findViewById(R.id.et_username);
        userPassword = (EditText) findViewById(R.id.et_userPassword);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == login.getId()) {
            startActivity(new Intent(this, MainActivity.class));
        }
        else {
            registerUser();
        }
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }
    // [END on_start_check_user]

    private void registerUser() {
        String email = userEmail.getText().toString().trim();
        String username = userName.getText().toString().trim();
        String password = userPassword.getText().toString().trim();
        String UID = "";
        String profileURL = "";

        if (email.isEmpty()) {
            userEmail.setError("Please enter the email address");
            userEmail.requestFocus();
            return;
        }


        if (username.isEmpty()) {
            userName.setError("Please enter the user name");
            userName.requestFocus();
            return;
        }


        //Email address validation
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            userEmail.setError("Please provide valid email address");
            userEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            userPassword.setError("Please provide a password");
            userPassword.requestFocus();
            return;
        }

        if (password.length() <4 || password.length() > 8) {
            userPassword.setError("Please provide password between 4 and 8 character");
            userPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() > 0) {
                    userName.setError("User name exists, please use another user name");
                    userName.requestFocus();
                    progressBar.setVisibility(View.GONE);
                    return;
                } else {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {

                                        User user = new User(UID, email, username, profileURL);

                                        FirebaseDatabase.getInstance().getReference("Users")
                                                .child(mAuth.getCurrentUser().getUid())
                                                //.child(username)
                                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        FirebaseDatabase.getInstance().getReference("Users")
                                                                .child(mAuth.getCurrentUser().getUid().toString());
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(RegistrationActivity.this,"User has been registered successfully!", Toast.LENGTH_LONG).show();
                                                            FirebaseUser uUser = mAuth.getCurrentUser();
                                                            String stringUser = uUser.getUid();
                                                            System.out.println("test-userkey" + stringUser);
                                                            //Toast.makeText(RegistrationActivity.this, "Successfully.", Toast.LENGTH_SHORT).show();
                                                            updateUI(uUser);
                                                            startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                                                        } else {
                                                            Toast.makeText(RegistrationActivity.this, "Failed to register! Try again.", Toast.LENGTH_LONG).show();
                                                            updateUI(null);
                                                        }
                                                        progressBar.setVisibility(View.GONE);
                                                    }
                                                });
                                    } else {
                                        Toast.makeText(RegistrationActivity.this, "Failed to register", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });




    }



    private void reload() { }

    private void updateUI(FirebaseUser uUser) {

    }
}
