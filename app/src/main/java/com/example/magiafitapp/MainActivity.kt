package com.example.magiafitapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

class MainActivity : AppCompatActivity() {


  //   private lateinit var recyclerView: RecyclerView
   private lateinit var userArrayList: ArrayList<User>
    private lateinit var myAdapter: MyAdapter
   private lateinit var db : FirebaseFirestore



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



       // recyclerView = findViewById(R.id.recyclerView)
       // recyclerView.layoutManager = LinearLayoutManager(this)
       // recyclerView.setHasFixedSize(true)

        userArrayList = arrayListOf()

       recyclerView.adapter = myAdapter

        EventChangeListener()

    }

    private fun EventChangeListener(){

        db = FirebaseFirestore.getInstance()
        db.collection("User").
                addSnapshotListener(object : EventListener<QuerySnapshot>{
                    override fun onEvent(
                        value: QuerySnapshot?,
                        error: FirebaseFirestoreException?
                    ) {
                        if (error != null){

                            Log.e("Firestore Error", error.message.toString())
                            return
                        }

                        for (dc : DocumentChange in value?.documentChanges!!){

                            if (dc.type == DocumentChange.Type.ADDED){

                                userArrayList.add(dc.document.toObject(User::class.java))
                            }
                        }

                        myAdapter.notifyDataSetChanged()
                    }
                })


    }

}