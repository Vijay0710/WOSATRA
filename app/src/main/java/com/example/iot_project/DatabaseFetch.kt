package com.example.iot_project

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import java.util.*
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin


class DatabaseFetch : AppCompatActivity() {
    var firebaseDatabase: FirebaseDatabase? = null
    var databaseReference: DatabaseReference? = null
    var userLatitude: Double? = null
    var userLongitude: Double? = null

    private var hm : HashMap<String,Double>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_database_fetch)

        hm = HashMap()

        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get
        userLatitude = 13.1207267
        userLongitude = 80.2212947

        hm!!.put("C",5.6)
        hm!!.put("E",0.9)
        // reference for our database.
        val myRef = firebaseDatabase!!.reference


        findViewById<Button>(R.id.searchBtn).setOnClickListener {
            fetchValues(myRef)
        }


        findViewById<Button>(R.id.recordFlag).setOnClickListener {
            setRecordFlag(myRef)
        }

    }

    private fun setRecordFlag(myRef: DatabaseReference) {
        try {


            val dao = DAOuser(pincode="0", flag = false.toString())
            val user = User(flag = true)
            dao.set(user).addOnSuccessListener { succ: Void? ->
                Toast.makeText(
                    this@DatabaseFetch,
                    "Added Successfully",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        Toast.makeText(this,"Camera recording will be started shortly",Toast.LENGTH_SHORT).show()
    }

    private fun fetchValues(myRef:DatabaseReference) {
        myRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                for (ds in snapshot.children) {
                    val uid = ds.key.toString()
                    Log.d("TESTING ds", ds.key.toString())
                    val latitude = ds.child("latitude").value
                    val longitude = ds.child("longitude").value

                    val distance: Double = distance(userLatitude!!, userLongitude!!,
                        latitude.toString().toDouble(), longitude.toString().toDouble()
                    )
                    Log.d("LATITUDE + LONGITUDE", "$latitude + $longitude")

                    Log.d("DISTANCE",distance.toString())

                    hm!!.put(uid,distance)
                    Log.d("HasMap",hm.toString())

                }

                sortedHashMap(hm)
            }





            override fun onCancelled(error: DatabaseError) {
                println(error)
            }

        })



    }


    private fun sortedHashMap(hashMap: HashMap<String,Double>?) {

        Log.d("HasMap",hashMap.toString())

//        val tm : TreeMap<String,Double> = TreeMap(hashMap!!)
//
//        Log.d("TreeMap",tm.toString())

        val list: List<Map.Entry<String,Double>> = LinkedList<Map.Entry<String,Double>>(hashMap!!.entries)

        Collections.sort(list,object: Comparator<Map.Entry<String, Double>> {
            override fun compare(
                o1: Map.Entry<String, Double>?,
                o2: Map.Entry<String, Double>?
            ): Int {
                return o1!!.value.compareTo(o2!!.value)
            }
        })

        val temp: HashMap<String, Double> = LinkedHashMap()
        for ((key, value) in list) {
            temp[key] = value
        }

        Log.d("LIST",list.toString())
    }

    private fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val theta = lon1 - lon2
        var dist = (sin(deg2rad(lat1))
                * sin(deg2rad(lat2))
                + (cos(deg2rad(lat1))
                * cos(deg2rad(lat2))
                * cos(deg2rad(theta))))
        dist = acos(dist)
        dist = rad2deg(dist)
        dist *= 60 * 1.1515
        return dist
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }

}