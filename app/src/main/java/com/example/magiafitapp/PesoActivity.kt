package com.example.magiafitapp

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.magiafitapp.databinding.ActivityPesoBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class PesoActivity : AppCompatActivity() {

    //Progress Dialog
    private lateinit var progressDialog: ProgressDialog

    //View binding
    private lateinit var binding: ActivityPesoBinding

    //Variable donde hacemos referencia a la BD
    private lateinit var database: DatabaseReference

    //Funcion principal
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPesoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Recuperamos el nickname
        val objetoIntent: Intent = intent
        val Nombre = objetoIntent.getStringExtra("Nombre")
        val usernick = "$Nombre"

        //Boton de volver atras
        binding.backBtn.setOnClickListener {

            onBackPressed()
            finish()

        }

        //Variable donde guardamos la instancia de la BD
        database = FirebaseDatabase.getInstance().getReference()

        progressDialog = ProgressDialog(this)

        //LLamada a la funcion del calendario
        calendarData(usernick)

    }
    /*Fin de la funcion principal*/


    //Funcion Calendario para setear la fecha
    private fun calendarData(usernick : String){

        //Calendario
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        //Boton que setea la fecha en el TextView
        binding.dateTv.setOnClickListener {
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                val completeDate = ("" + "${mDay}"  + "/" + "${mMonth}" + "/" + "${mYear}").trim()
                binding.dateTv.setText(completeDate)

                //Boton de registrar los datos
                binding.registrarPesoBtn.setOnClickListener {

                    //Llamada de las variables
                    val userdata : TextView = findViewById(R.id.dateTv) as TextView
                    var us : String = userdata.text.toString().trim()
                    val peso = binding.pesoKgSet.text.toString()

                    val docname : EditText = findViewById(R.id.pesoName)
                    var dcname : String = docname.text.toString().trim()


                    //HasMap donde se almacenan los datos
                    val hashMap: HashMap<String, Any?> = HashMap()

                    hashMap["Fecha"] = us
                    hashMap["Peso"] = peso

                    //Conexion a la base de datos
                    val ref = FirebaseDatabase.getInstance().getReference("Users")
                    ref.child(usernick)
                        .child("Pesaje")
                        .child("${dcname}")
                        .setValue(hashMap)
                        .addOnSuccessListener {
                            Toast.makeText(this,"Cuenta creada...", Toast.LENGTH_SHORT).show()
                        }
                }
            }, year, month, day)
            //Show dialog
            dpd.show()
        }
    }
}


