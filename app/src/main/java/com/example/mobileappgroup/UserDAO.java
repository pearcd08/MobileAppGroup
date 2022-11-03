package com.example.mobileappgroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class UserDAO {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public UserDAO() {
        firebaseDatabase = FirebaseDatabase.getInstance("https://groupassignment-be15d-default-rtdb.firebaseio.com/");
        databaseReference = firebaseDatabase.getReference("Users");
    }

    public Query get() {
        return databaseReference.orderByKey();
    }
}
