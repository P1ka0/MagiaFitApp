package com.example.magiafitapp

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.magiafitapp.databinding.ActivityCategoryAddBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class CategoryAddActivity : AppCompatActivity() {

    //View binding
    private lateinit var binding: ActivityCategoryAddBinding

    //Firebause Auth
    private lateinit var firebaseAuth: FirebaseAuth

    //Progress Dialog
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Iniciamos y obtnemos la instancia de Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()

        //Configuramos el Progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espera por favor...")
        progressDialog.setCanceledOnTouchOutside(false)


        //Boton de Ir Atras
        binding.backBtn.setOnClickListener {
            onBackPressed()
        }

        //Boton para crear el tipo de directorio
        binding.submitBtn.setOnClickListener {
            validateData()
        }
    }

    private var category = ""
    //Funcion donde se validan los datos a subir
    private fun validateData() {


        //Nos setea el nombre del directorio al ser creado
        category = binding.categoryEt.text.toString().trim()


        //Obetener datos
        if (category.isEmpty()){
            Toast.makeText(this, "Introduce Actividad... ",Toast.LENGTH_SHORT).show()
        }
        else{
            addCategoryFirebase()
        }
    }

    //Funcion que add el tipo de categoria
    private fun addCategoryFirebase() {

        //Mostramos el progreso
        progressDialog.show()

        //Obtenemos el timestamp
        val timestamp = System.currentTimeMillis()

        //Seteamos datos en la db de Firebase
        val hashMap = HashMap<String, Any>()
        hashMap["id"] = "$timestamp"
        hashMap["Actividad"] = category
        hashMap["timestamp"] = timestamp
        hashMap["uid"] = "${firebaseAuth.uid}"

        //Add a la base de datos de Firebasse Categorias > CategoryId > Info de category
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child("$timestamp")
            .setValue(hashMap)
            .addOnSuccessListener {

                //Mostramos el mensaje de que esta Succesfully
                progressDialog.dismiss()
                Toast.makeText(this, "Added Succesfully....", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{ e ->

                //Si falla se muestra el mensaje
                progressDialog.dismiss()
                Toast.makeText(this, "Fallo al cargar los datos ${e.message} ", Toast.LENGTH_SHORT).show()
            }

    }
}