package com.example.mobileappgroup;

import com.example.mobileappgroup.Models.Post;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostDAO {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public PostDAO() {
        firebaseDatabase = FirebaseDatabase.getInstance("https://groupassignment-be15d-default-rtdb.firebaseio.com/");
        databaseReference = firebaseDatabase.getReference("Posts");
    }

    public Task<Void> add (Post post){
        return databaseReference.push().setValue(post);
    }
}
