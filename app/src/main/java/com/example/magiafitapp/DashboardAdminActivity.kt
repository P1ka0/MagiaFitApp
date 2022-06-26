package com.example.magiafitapp

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.magiafitapp.databinding.ActivityDashboardAdminBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class DashboardAdminActivity : AppCompatActivity() {

    //View binding
    private lateinit var binding: ActivityDashboardAdminBinding

    //Firebase Auth
    private lateinit var firebaseAuth: FirebaseAuth


    //Instanciamos para obtener las referencias
    private lateinit var database: DatabaseReference

    //Adaptador
   // private lateinit var adapterCategory: AdapterCategory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val objetoIntent: Intent = intent
        val Nombre = objetoIntent.getStringExtra("Nombre")

        val subtitle = findViewById<TextView>(R.id.subTitleTv)
        subtitle.text = "$Nombre"

        //Funcion que abre los Activities
        ButtonSetup()

        //UserName
        //database = FirebaseDatabase.getInstance().getReferenceFromUrl("Users")
    }


    private fun ButtonSetup(){

        //Open IMC Activity
        binding.uploadDoc.setOnClickListener {
            startActivity(Intent(this,uploadPdfActivity::class.java))
        }

        //Open ApointementActivity
        binding.openUserInfo.setOnClickListener {
            startActivity(Intent(this, UserInfoiActivity::class.java))
        }

        //Boton de cerrar sesion
        binding.logoutBtn.setOnClickListener {
            firebaseAuth.signOut()
            //checkUser()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}