package com.example.magiafitapp

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FetchDataActivity : AppCompatActivity() {

    private lateinit var Name: TextView
    private lateinit var Email: TextView
    private lateinit var Telephone: TextView


    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetch_data)

        Name = findViewById(R.id.tvfirstName)
        Email = findViewById(R.id.tvsurName)


        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val ref = db.collection("User").document(userId)
        ref.get().addOnSuccessListener {
            if (it != null){

                val name = it.data?.get("Nombre")?.toString()
                val telefono = it.data?.get("Telefono")?.toString()
                val email = it.data?.get("Email")?.toString()

                Name.text = name
                Email.text = email
                Telephone.text = telefono

            }

        }.addOnFailureListener {

            Toast.makeText(this, "Fallo al recuperar Datos", Toast.LENGTH_SHORT).show()
        }


    }
}