package com.example.mobileappgroup;

import com.example.mobileappgroup.Models.Post;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;

public class PostDAO {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public PostDAO() {
        firebaseDatabase = FirebaseDatabase.getInstance("https://groupassignment-be15d-default-rtdb.firebaseio.com/");
        databaseReference = firebaseDatabase.getReference("Posts");
    }

    public Task<Void> add(Post post) {
        return databaseReference.push().setValue(post);
    }

    public Query get(String postID) {
        return databaseReference.child(postID);
    }

    public Task<Void> update(String postID, HashMap<String, Object> post) {
            return databaseReference.child(postID).updateChildren(post);

    }
}
