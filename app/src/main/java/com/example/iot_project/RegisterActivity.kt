package com.example.iot_project

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import java.util.*


class RegisterActivity : AppCompatActivity() {
    var etRegEmail: TextInputEditText? = null
    var etRegPassword: TextInputEditText? = null
    var tvLoginHere: TextView? = null
    var btnRegister: Button? = null
    var mAuth: FirebaseAuth? = null
    private var latitude: Double? = null
    private var longitude: Double? = null
    private val REQUEST_CODE: Int = 1
    private var locationManager: LocationManager? = null
    private var pincode: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        etRegEmail = findViewById(R.id.etRegEmail)
        etRegPassword = findViewById(R.id.etRegPass)
        tvLoginHere = findViewById(R.id.tvLoginHere)
        btnRegister = findViewById(R.id.btnRegister)
        mAuth = FirebaseAuth.getInstance()

        try {
            if (ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    101
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }





        val gpsTracker = GPSTracker(this)

        if(gpsTracker.canGetLocation()){
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
            Log.i("Latitude",latitude.toString())
            Log.i("Longitude",longitude.toString())
        }else{
            gpsTracker.showSettingsAlert();
        }

        ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),REQUEST_CODE)



        btnRegister!!.setOnClickListener(View.OnClickListener { view: View? -> createUser() })
        tvLoginHere!!.setOnClickListener(View.OnClickListener { view: View? ->
            startActivity(
                Intent(
                    this@RegisterActivity,
                    LoginActivity::class.java
                )
            )
        })

    }



    private fun createUser() {
        val gpsTracker = GPSTracker(this)

        if(gpsTracker.canGetLocation()){
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();

            val gcd: Geocoder = Geocoder(this)

            val addresses: List<Address> = gcd.getFromLocation(latitude!!,longitude!!,10)

            pincode = addresses.get(0).postalCode


            Log.i("Latitude",latitude.toString())
            Log.i("Longitude",longitude.toString())
        }else{
            gpsTracker.showSettingsAlert();
        }
        val email = etRegEmail!!.text.toString()
        val password = etRegPassword!!.text.toString()
        if (TextUtils.isEmpty(email)) {
            etRegEmail!!.error = "Email cannot be empty"
            etRegEmail!!.requestFocus()
        } else if (TextUtils.isEmpty(password)) {
            etRegPassword!!.error = "Password cannot be empty"
            etRegPassword!!.requestFocus()
        } else {
            mAuth!!.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this@RegisterActivity,
                        "User registered successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    val dao = DAOuser(pincode!!, flag = false.toString())
                    val user = User(
                        etRegEmail!!.text.toString(), etRegPassword!!.text.toString(),
                        latitude.toString(),longitude.toString()
                    )
                    try {


                        dao.add(user).addOnSuccessListener { succ: Void? ->
                            Toast.makeText(
                                this@RegisterActivity,
                                "Added Successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    startActivity(
                        Intent(
                            this@RegisterActivity, LoginActivity::class.java
                        )
                    )
                } else {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Registration Error: " + task.exception!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}