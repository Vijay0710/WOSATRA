package com.example.iot_project

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import android.os.Bundle
import com.example.iot_project.R
import android.content.Intent
import android.view.View
import android.widget.Button
import com.example.iot_project.LoginActivity
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {
    var btnLogOut: Button? = null
    var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnLogOut = findViewById(R.id.btnLogout)
        mAuth = FirebaseAuth.getInstance()
        btnLogOut!!.setOnClickListener(View.OnClickListener { view: View? ->
            mAuth!!.signOut()
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        })
    }

    override fun onStart() {
        super.onStart()
        val user = mAuth!!.currentUser
        if (user == null) {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        }
    }
}