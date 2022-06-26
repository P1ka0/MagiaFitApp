package com.example.magiafitapp

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.magiafitapp.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {

    //view binding
    private lateinit var binding: ActivityForgotPasswordBinding

    //firebase AUTH
    private lateinit var firebaseAuth: FirebaseAuth

    //progress dialog
    private lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //init firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()

        //init/ setup dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setCanceledOnTouchOutside(false)


        val backBtn = findViewById<ImageButton>(R.id.backBtn)
        backBtn.setOnClickListener {
            onBackPressed()
        }

        //Recover password screen
        binding.submitBtn.setOnClickListener {
            validateData()
        }
    }


    //------------------Funcion que introduce el email para ser recuperado----------------------//
    private var email = ""
    private fun validateData() {

        //get data
        email = binding.emailEt.text.toString()

        //validar datos
        if (email.isEmpty()){
            Toast.makeText(this, "Introduce Email...", Toast.LENGTH_SHORT).show()
        }
       /*else if (Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Email Inválido...", Toast.LENGTH_SHORT).show()
        }*/
        else{
            recoverPassword()
        }
    }
    //----------------------Fin Funcion que introduce el email para ser recuperado---------------//

    //----------------------Funcion recuperacion de la Pass---------------//
    private fun recoverPassword() {

        //Mostramos el Progreso
        progressDialog.setMessage("Enviando las intrucciones de recuperación a $email")
        progressDialog.show()

        firebaseAuth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                //Envio
                progressDialog.dismiss()
                Toast.makeText(this, "Instrucciones envidas a $email", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{ e->
                //fallo
                progressDialog.dismiss()
                Toast.makeText(this, "Fallo al enviar los datos a ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}