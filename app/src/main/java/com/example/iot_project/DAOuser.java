package com.example.iot_project;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DAOuser {
    private DatabaseReference databaseReference;
    public DAOuser(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference();
    }
    public Task<Void> add(User user)
    {
        Log.d("hello",user.toString());
        return databaseReference.push().setValue(user);
    }
}
