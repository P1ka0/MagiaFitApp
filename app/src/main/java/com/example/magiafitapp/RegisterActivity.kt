package com.example.magiafitapp

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.magiafitapp.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference

class RegisterActivity : AppCompatActivity() {


    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    private lateinit var storageRef : StorageReference
    private lateinit var progressBar: ProgressBar
    private lateinit var database: DatabaseReference
    private var db = Firebase.firestore
    lateinit var filepath : Uri
    private val TAG = "PDF_ADD_TAG"


    //Variable donde guardamos la referencia a la BD
    //var database = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Iniciar Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()

        //Inicializamos el firebase auth
        firebaseAuth = Firebase.auth

        database = Firebase.database.reference

        //Iniciar el progress dialog y mostrar la creacion de usuario o registro
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Por favor espera...")
        progressDialog.setCanceledOnTouchOutside(false)

        //Volver a la pantalla anterior
        binding.backBtn.setOnClickListener {
            onBackPressed()
        }

        //Accion completa al pulsar sobre el boton Registrar
        binding.registerlBtn.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
                .setTitle("Comprobar información")
                .setMessage("¿Ha comprobado que toda su información sea correcta?")
                .setNegativeButton("Cancelar"){view,_->
                    view.dismiss()
                }
                .setPositiveButton("Aceptar"){view, _ ->
                    validateDate()
                }
                .setCancelable(false)
                .create()
            dialog.show()
        }
    }
    /*Fin de la funcion principal*/

    //Funcion que introduce los datos
    private var nick = ""
    private var name = ""
    private var surname = ""
    private var phone = ""
    private var address = ""
    private var email = ""
    private var password = ""

    //Funcion que valida los datos
    private fun validateDate() {

       //1)Introduccion de Datos
        nick = binding.nicknameEt.text.toString().trim()
        name = binding.nameEt.text.toString().trim()
        email = binding.emailEt.text.toString().trim()
        password = binding.passwordEt.text.toString().trim()
        surname = binding.surnameEt.text.toString().trim()
        phone = binding.phoneUserEt.text.toString().trim()
        address = binding.useraddresEt.text.toString().trim()
        val cPassword = binding.cPasswordEt.text.toString().trim()

        //2) Validate Data

        if (nick.isEmpty())
        {
            //Nombre Vacio
            Toast.makeText(this,"Introduce tu Nick...", Toast.LENGTH_SHORT).show()
        }
       else if (name.isEmpty())
        {
            //Nombre Vacio
            Toast.makeText(this,"Introduce tu nombre...", Toast.LENGTH_SHORT).show()
        }
        else if (surname.isEmpty())
        {
            //Apellido Vacio
            Toast.makeText(this,"Introduce tu apellido...", Toast.LENGTH_SHORT).show()
        }
        else if (phone.isEmpty())
        {
            //Telefono Vacio
            Toast.makeText(this,"Introduce tu telefono..", Toast.LENGTH_SHORT).show()
        }
        else if (address.isEmpty())
        {
            //Telefono Vacio
            Toast.makeText(this,"Introduce tu direccion..", Toast.LENGTH_SHORT).show()
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            //-------
            Toast.makeText(this,"Formato de Pass invalido...", Toast.LENGTH_SHORT).show()
        }
        else if (password.isEmpty())
        {
            //---------------
            Toast.makeText(this,"Introduce el password...", Toast.LENGTH_SHORT).show()
        }
        else if (cPassword.isEmpty())
        {
            //----------
            Toast.makeText(this,"Confirma el Password...", Toast.LENGTH_SHORT).show()
        }
        else if (password != cPassword)
        {
            //--------
            Toast.makeText(this,"Pass no corresponde...", Toast.LENGTH_SHORT).show()
        }
        else{

            createUserAccount()
        }
    }

    //3) Crear Cuenta - Firebase Auth
    private fun createUserAccount() {

        //Show progress message
        progressDialog.setMessage("Creando Cuenta...")
        progressDialog.show()

        //Crear cuenta en Firebase Auth
        firebaseAuth.createUserWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                //Cuenta creada
                updateUserInfo()
            }
            .addOnFailureListener{ e->
                //Mensaje de fallo
                progressDialog.dismiss()
                Toast.makeText(this,"Fallo al crear la cuenta ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    //Funcion que guarda la informacion de usuario
    private fun updateUserInfo() {

       // val uid = firebaseAuth.uid

        val hashMap: HashMap<String, Any?> = HashMap()

        //hashMap["uid"] = uid
        hashMap["Nick"] = nick
        hashMap["Email"] = email
        hashMap["Password"] = password
        hashMap["Nombre"] = name
        hashMap["Apellido"] = surname
        hashMap["Direccion"] = address
        hashMap["Telefono"] = phone
        hashMap["Edad"] = "50"
        hashMap["profileImage"] = "" //Dejamos esta vacia, para editarla en el perfil
        hashMap["userType"] = "user"//possible values are user/admin

        //Introducir datos en la Base de Datos
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(nick!!)
            .setValue(hashMap)
            .addOnSuccessListener {

                progressDialog.dismiss()
                Toast.makeText(this,"Cuenta creada con Exito!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                finish()

            }.addOnFailureListener{
                progressDialog.dismiss()
                Toast.makeText(this, "Fallo al guardar los datos usuario",Toast.LENGTH_SHORT).show()
            }

        val userMap = hashMapOf(
            "name" to name,
            "email" to email,
            "address" to address,
            "surname" to surname
         )

        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        db.collection("User").document(userId).set(userMap)
            .addOnSuccessListener {
                Toast.makeText(this, "Datos subidos con exito",Toast.LENGTH_SHORT).show()


            }.addOnFailureListener {
                Toast.makeText(this, "Fallo al guardar los datos usuario",Toast.LENGTH_SHORT).show()
            }


    }
}

