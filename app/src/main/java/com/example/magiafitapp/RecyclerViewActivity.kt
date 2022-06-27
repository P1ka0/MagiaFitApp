package com.example.magiafitapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RecyclerViewActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var userList: ArrayList<User>
    private var db = Firebase.firestore

    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvAddress: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)


        tvName = findViewById(R.id.name)
        tvEmail = findViewById(R.id.email)
        tvAddress = findViewById(R.id.address)


        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val ref = db.collection("User").document(userId)
        ref.get().addOnSuccessListener {

            if (it != null){
                val name = it.data?.get("name")?.toString()
                val address = it.data?.get("address")?.toString()
                val email = it.data?.get("email")?.toString()

                tvName.text = name
                tvAddress.text = address
                tvEmail.text = email

            }
        }

            .addOnFailureListener {

                //toast fail
            }

    }
}