package com.example.iot_project

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DAOuser(pincode: String, flag: String) {


    private var databaseReference: DatabaseReference
    private var dbFlagReference: DatabaseReference

    fun add(user: User): Task<Void> {
        Log.d("hello", user.toString())
        return databaseReference.push().setValue(user)
    }

    init{
        val db = FirebaseDatabase.getInstance()
        dbFlagReference = db.reference
    }


    fun set(user: User): Task<Void>{
        Log.d("Flag Value: ", user.toString())

        return dbFlagReference.child("flag/").setValue(user)

    }

    init {
        val db = FirebaseDatabase.getInstance()
        databaseReference = db.getReference("$pincode/")
    }
}