package com.example.magiafitapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.magiafitapp.databinding.ActivityUserInfoiBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class UserInfoiActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    //FVariable que registra los permisos de autotizacion
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var binding: ActivityUserInfoiBinding




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserInfoiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {

            onBackPressed()
        }

        binding.readdataBtn.setOnClickListener {
            val user : String = binding.etusername.text.toString()
            val email : String = binding.etusername.text.toString()
            // val userId : String
            if(user.isNotEmpty()){
                readData(user)
            }
        }

        binding.allusersBtn.setOnClickListener {
            val intent : Intent = Intent(this, RecyclerViewActivity::class.java)
            startActivity(intent)
        }


    }

    //Funcion que busca los datos del usuario
    private fun readData(user: String) {
        database = FirebaseDatabase.getInstance().getReference("Users")
        database.child(user).get().addOnSuccessListener {

            if (it.exists()) {
                val userid = it.child("Nick").value
                val userName = it.child("Nombre").value
                val usersurname = it.child("Apellido").value
                val userphone = it.child("Telefono").value
                val userAddres = it.child("Direccion").value

                //Seteamos los datos en los campos
                binding.userId.text = userid.toString()
                binding.tvfirstName.text = userName.toString()
                binding.tvsurName.text = usersurname.toString()
                binding.userPhoneTv.text = userphone.toString()
                binding.userAddresTv.text = userAddres.toString()

            }
            else
            {
                Toast.makeText(this, "El ID no exíste o es erróneo", Toast.LENGTH_SHORT).show()
            }

        }.addOnFailureListener{
            Toast.makeText(this, "Fallo al recuperar Datos", Toast.LENGTH_SHORT).show()
        }}
}