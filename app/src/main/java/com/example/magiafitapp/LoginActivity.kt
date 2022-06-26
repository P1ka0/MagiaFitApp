package com.example.magiafitapp

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.magiafitapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class LoginActivity : AppCompatActivity() {

    //View Binding
    private lateinit var binding: ActivityLoginBinding

    //FVariable que registra los permisos de autotizacion
    private lateinit var firebaseAuth: FirebaseAuth

    //Progress Dialog
    private lateinit var progressDialog: ProgressDialog

    //Variable que referencia elementos dentro de la base de datos
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //Iniciar el progress dialog y mostrar la creacion de usuario o registro
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Por favor espera...")
        progressDialog.setCanceledOnTouchOutside(false)

        //Volver a la pantalla anterior
        binding.noAccountTv.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.forgotTv.setOnClickListener {
         startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        //Boton de Login que nos lleva a validar los datos
        binding.loginButton.setOnClickListener {
            val username = findViewById<EditText>(R.id.emailEt)
            var Nombre : String = username.text.toString()


           validateData()
        }
    }

    // private var email = ""
    private var userID = ""
    private var userPass = ""

    //Funcion que valida que los campos nos esten vacios (EMPTY)
    private fun validateData() {

        userID = binding.emailEt.text.toString().trim()
        userPass = binding.paswordEt.text.toString().trim()

        if (userID.isEmpty()) {
            Toast.makeText(this, "Introduce Usuario...", Toast.LENGTH_SHORT).show()
        } else if (userPass.isEmpty()) {
            Toast.makeText(this, "Introduce Pass...", Toast.LENGTH_SHORT).show()
        } else {

            checkUser(userID)
        }
    }

    //Funcion que comprueba que el usuario es "user" o "admin"
    private fun checkUser(userID: String) {
        database = FirebaseDatabase.getInstance().getReference("Users")
        database.child(userID).get().addOnSuccessListener {

            if (it.exists()) {

                //Variables que comparan y verifican al usuario
                val userType = it.child("userType").value
                val userP = it.child("Password").value

                if (userType == "user" && userPass == userP){

                    //Es el usuario simple
                    val intent : Intent = Intent(this@LoginActivity, DashBoardUserActivity::class.java)
                    intent.putExtra("Nombre", userID)
                    startActivity(intent)
                    finish()

                }else if (userType == "admin" && userPass == userP){

                    //Es el administrador
                    val intent : Intent = Intent(this@LoginActivity, DashboardAdminActivity::class.java)
                    intent.putExtra("Nombre", userID)
                    startActivity(intent)
                    finish()
                }
            }
            else
            {
                Toast.makeText(this, "Nombre no encontrado",Toast.LENGTH_SHORT).show()
            }

        }.addOnFailureListener{
            Toast.makeText(this, "No se puede conectar con la Base de Datos",Toast.LENGTH_SHORT).show()
        }}
    }
