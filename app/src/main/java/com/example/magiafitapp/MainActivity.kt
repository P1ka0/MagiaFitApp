package com.example.magiafitapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var userList: ArrayList<User>
    private var db = Firebase.firestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        userList = arrayListOf()

        db = FirebaseFirestore.getInstance()

        db.collection("User").get().addOnSuccessListener {
            if (!it.isEmpty){
                for (data in it.documents){
                    val user: User? = data.toObject(User::class.java)
                    if (user != null){

                        userList.add(user)
                    }
                }

                recyclerView.adapter = MyAdapter(userList)
            }

        }
            .addOnFailureListener {
                Toast.makeText(this, "Fallo al recuperar Datos", Toast.LENGTH_SHORT).show()
            }

    }
}


