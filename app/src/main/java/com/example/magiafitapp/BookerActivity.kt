package com.example.magiafitapp

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.magiafitapp.databinding.ActivityBookerBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class BookerActivity : AppCompatActivity() {

    //Firebause Auth
    private lateinit var firebaseAuth: FirebaseAuth

    //Progress Dialog
    private lateinit var progressDialog: ProgressDialog

    //View binding
    private lateinit var binding: ActivityBookerBinding

    //Variable donde hacemos referencia a la BD
    private lateinit var database: DatabaseReference

    //Arrays que guardan los datos de las listas desplegables
    val Actividades = arrayOf("Hipertrofia", "Flexibilidad", "Tonificación")
    val DiasSemana = arrayOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes")
    val HorasDia = arrayOf("10:00", "11:00", "12:00")

    //Main
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Llamada al userid
        val objetoIntent: Intent = intent
        val Nombre = objetoIntent.getStringExtra("Nombre")
        val usernick = "$Nombre"

        //Iniciamos y obtnemos la instancia de Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()

        //Configuramos el Progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espera por favor...")
        progressDialog.setCanceledOnTouchOutside(false)

        //Funciones donde se setean los datos en la pantalla
        seleccion()

        binding.backBtn.setOnClickListener {

            onBackPressed()
            finish()

        }

        binding.reservasBtn.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
                .setTitle("ATENCIÓN!!")
                .setMessage("¿Estás segur@ de que has elegido correctamente?")
                .setNegativeButton("Cancelar") { view, _ ->
                    Toast.makeText(this, "Cancelado, revisa tus datos", Toast.LENGTH_SHORT).show()
                    view.dismiss()
                }
                .setPositiveButton("Aceptar") { view, _ ->
                    Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
                    updateApointments(usernick)
                    view.dismiss()
                }
                .setCancelable(false)
                .create()
            dialog.show()
        }
    }

    //Variables donde almacenamos los datos de las reservas
    private var Actividad1 = ""
    private var Actividad2 = ""
    private var Actividad3 = ""
    private var Dia1 = ""
    private var Dia2 = ""
    private var Dia3 = ""
    private var Hora1 = ""
    private var Hora2 = ""
    private var Hora3 = ""

    //Funcion de la primera linea de seleccion
    private fun seleccion() {
        val adapter = ArrayAdapter(this, R.layout.lista_actividades1, Actividades)
        val adapter2 = ArrayAdapter(this, R.layout.lista_actividades1, DiasSemana)
        val adapter3 = ArrayAdapter(this, R.layout.lista_actividades1, HorasDia)

        //-----------------------------------------------------------------------------------------//
        val Activities1 : AutoCompleteTextView = findViewById(R.id.activity1) as AutoCompleteTextView
        Activities1.setAdapter(adapter)
        // activity1.adapter = arrayAdapter
        binding.activity1.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, id ->
            val selectedItem = adapterView.getItemAtPosition(position) as String

            Actividad1 = selectedItem
        }

        //-----------------------------------------------------------------------------------------//
        val WeekDays1: AutoCompleteTextView = findViewById(R.id.autoDay1) as AutoCompleteTextView
        WeekDays1.setAdapter(adapter2)

        binding.autoDay1.onItemClickListener = AdapterView.OnItemClickListener{adapterView, view, position, id ->
            val selectedItem1 = adapterView.getItemAtPosition(position) as String

            Dia1 = selectedItem1
        }

        //-----------------------------------------------------------------------------------------//
        val Hours1: AutoCompleteTextView = findViewById(R.id.autoHr1) as AutoCompleteTextView
        Hours1.setAdapter(adapter3)

        binding.autoHr1.onItemClickListener = AdapterView.OnItemClickListener{adapterView, view, position, id ->
            val selectedItem2 = adapterView.getItemAtPosition(position) as String

            Hora1 = selectedItem2
        }

        //-----------------------------------------------------------------------------------------//
        val Activities2 : AutoCompleteTextView = findViewById(R.id.activity2) as AutoCompleteTextView
        Activities2.setAdapter(adapter)
        // activity1.adapter = arrayAdapter
        binding.activity2.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, id ->
            val selectedItem3 = adapterView.getItemAtPosition(position) as String

            Actividad2 = selectedItem3
        }

        //-----------------------------------------------------------------------------------------//
        val WeekDays2: AutoCompleteTextView = findViewById(R.id.autoDay2) as AutoCompleteTextView
        WeekDays2.setAdapter(adapter2)

        binding.autoDay2.onItemClickListener = AdapterView.OnItemClickListener{adapterView, view, position, id ->
            val selectedItem4 = adapterView.getItemAtPosition(position) as String

            Dia2 = selectedItem4
        }

        //-----------------------------------------------------------------------------------------//
        val Hours2: AutoCompleteTextView = findViewById(R.id.autoHr2) as AutoCompleteTextView
        Hours2.setAdapter(adapter3)

        binding.autoHr2.onItemClickListener = AdapterView.OnItemClickListener{adapterView, view, position, id ->
            val selectedItem5 = adapterView.getItemAtPosition(position) as String

            Hora2 = selectedItem5
        }

        //-----------------------------------------------------------------------------------------//
        val Activities3 : AutoCompleteTextView = findViewById(R.id.activity3) as AutoCompleteTextView
        Activities3.setAdapter(adapter)
        // activity1.adapter = arrayAdapter
        binding.activity3.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, id ->
            val selectedItem6 = adapterView.getItemAtPosition(position) as String

            Actividad3 = selectedItem6
        }

        //-----------------------------------------------------------------------------------------//
        val WeekDays3: AutoCompleteTextView = findViewById(R.id.autoDay3) as AutoCompleteTextView
        WeekDays3.setAdapter(adapter2)

        binding.autoDay3.onItemClickListener = AdapterView.OnItemClickListener{adapterView, view, position, id ->
            val selectedItem7 = adapterView.getItemAtPosition(position) as String

            Dia3 = selectedItem7
        }

        //-----------------------------------------------------------------------------------------//
        val Hours3: AutoCompleteTextView = findViewById(R.id.autoHr3) as AutoCompleteTextView
        Hours3.setAdapter(adapter3)

        binding.autoHr3.onItemClickListener = AdapterView.OnItemClickListener{adapterView, view, position, id ->
            val selectedItem8 = adapterView.getItemAtPosition(position) as String

           Hora3 = selectedItem8
        }
    }

    //Funcion para setear la informacion en Firebase
    private fun updateApointments(usernick : String){

        val hasMap : HashMap<String, Any?> = HashMap()

        hasMap["Actividad 1"] = Actividad1
        hasMap["Actividad 2"] = Actividad2
        hasMap["Actividad 3"] = Actividad3

        hasMap["Dia Semana 1"] = Dia1
        hasMap["Dia Semana 2"] = Dia2
        hasMap["Dia Semana 3"] = Dia3

        hasMap["Hora Elegida 1"] = Hora1
        hasMap["Hora Elegida 2"] = Hora2
        hasMap["Hora Elegida 3"] = Hora3

        //Conectamos con la base de datos y guardamos la informacion
        val ref = FirebaseDatabase.getInstance().getReference("Citas")
        ref.child(usernick)
            .setValue(hasMap)
            .addOnSuccessListener {

                progressDialog.dismiss()
                Toast.makeText(this,"Informacion guardada", Toast.LENGTH_SHORT).show()

            }.addOnFailureListener{
                progressDialog.dismiss()
                Toast.makeText(this, "Fallo al guardar informacion",Toast.LENGTH_SHORT).show()
            }
    }
}






