package com.example.magiafitapp

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UsersActivity : AppCompatActivity() {

    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvAddres: TextView

    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)


        tvName = findViewById(R.id.tv_name)
        tvEmail = findViewById(R.id.tv_email)
        tvAddres = findViewById(R.id.tv_address)


        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val ref = db.collection("User").document(userId)
        ref.get().addOnSuccessListener {
            if (it != null){

                val name = it.data?.get("Nombre")?.toString()
                val address = it.data?.get("Direccion")?.toString()
                val email = it.data?.get("Email")?.toString()

                tvName.text = name
                tvEmail.text = email
                tvAddres.text = address

            }

        }.addOnFailureListener {

            Toast.makeText(this, "Fallo al recuperar Datos", Toast.LENGTH_SHORT).show()
        }
    }
}
