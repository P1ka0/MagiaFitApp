package com.example.magiafitapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.magiafitapp.databinding.ActivityDashBoardUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*


class DashBoardUserActivity : AppCompatActivity(){

    private lateinit var binding: ActivityDashBoardUserBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var dialog: AlertDialog.Builder
    private lateinit var storage: FirebaseStorage
    private lateinit var selectedImg : Uri
    private lateinit var storageRef : StorageReference

    //Funcion principal de la Clase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashBoardUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //-----------------LLamada al id Usuario--------------------//
        val objetoIntent: Intent = intent
        val Nombre = objetoIntent.getStringExtra("Nombre")

        val subtitle = findViewById<TextView>(R.id.subTitleTv)
        subtitle.text = "$Nombre"

        //Enviamos el user a las otras Activities
        val userid = "$Nombre"
        subtitle.text = userid
        //-----------------Fin LLamada al id Usuario--------------------//

        //Inicio de Firebase Auth
        auth = FirebaseAuth.getInstance()

        //LLamada funcion de los botones y se pasa el parametro del id
        ButtonsSetup(userid)

        //Funcion para elegir o hacer foto
        PickImageBtn()

        //Dialogo de alerta
        dialog = AlertDialog.Builder(this)
            .setMessage("Actualizando Foto de Perfil")
            .setCancelable(false)

        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()


        //----------Funcion que recupera imagen de perfil----------//
        profileImageRecover()



    }

    private fun profileImageRecover(){

        val storageRef = FirebaseStorage.getInstance().reference.child("ProfileImg").child("riki123")

    }


    private fun PickImageBtn(){
        binding.profileImg.setOnClickListener{
            val dialog = AlertDialog.Builder(this)
                .setTitle("Elige una opcion")
                .setMessage("Puede escoger una foto o hacerte una para tu perfil")
                .setNegativeButton("CANCELAR"){view, _->
                    view.dismiss()
                }
                .setPositiveButton("ELEGIR FOTO"){view,_ ->
                    Toast.makeText(this,"Has escogido elegir foto",Toast.LENGTH_SHORT).show()
                   //fileManager();
                    choosePhoto()
                    view.dismiss()
                }

                .setCancelable(false)
                .create()
            dialog.show()
        }

    }

//-----------------------------Elegir Foto y subirla a Firebase---------------------//
    private fun choosePhoto(){
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null){
            if (data.data != null){
                selectedImg = data.data!!

                binding.profileImg.setImageURI(selectedImg)

                uploadData(userid = "riki123")
            }
        }
    }

    private fun uploadData(userid: String){
        val reference = storage.reference.child("Profile Img").child("$userid").child(Date().time.toString())
        reference.putFile(selectedImg).addOnCompleteListener {
            if (it.isSuccessful){
                reference.downloadUrl.addOnSuccessListener { task ->
                    uploadInfo(task.toString())
                }
            }
        }
    }

    private fun uploadInfo(imgUrl: String){
        val user = UserModel(imgUrl)

        database.reference.child("users")
            .child(auth.uid.toString())
            .setValue(user)
            .addOnSuccessListener {
                Toast.makeText(this, "imagen subida", Toast.LENGTH_SHORT).show()
            }
    }

   //-----------------------------Fin Elegir Foto y subirla a Firebase---------------------//

    //-----------------Funcion del boton que llama Activities--------------------//
    private fun ButtonsSetup(userid: String) {

        //Open Peso Activity
        binding.pesoActivity.setOnClickListener {

            val intent : Intent = Intent(this,PesoActivity::class.java)
            intent.putExtra("Nombre",userid)
            startActivity(intent)


        }

        //Open IMC Activity
        binding.openImcButton.setOnClickListener {
            startActivity(Intent(this,ImcCalculator::class.java))

        }

        //Boton de cerrar sesion
        binding.logoutBtn.setOnClickListener {
            auth.signOut()
            finish()
            startActivity(Intent(this, LoginActivity::class.java))

        }

        binding.reservasBtn.setOnClickListener {

            val intent : Intent = Intent(this,BookerActivity::class.java)
            intent.putExtra("Nombre",userid)
            startActivity(intent)


        }

        binding.userPdfUp.setOnClickListener {
            val intent : Intent = Intent(this,uploadPdfActivity::class.java)
            intent.putExtra("Nombre",userid)
            startActivity(intent)

        }

    }

    //-----------------Fin Funcion del boton que llama Activities--------------------//
}

