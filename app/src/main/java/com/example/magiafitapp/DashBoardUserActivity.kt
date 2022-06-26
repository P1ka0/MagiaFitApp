package com.example.magiafitapp

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.magiafitapp.databinding.ActivityDashBoardUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File


class DashBoardUserActivity : AppCompatActivity(){

    private lateinit var binding: ActivityDashBoardUserBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var dialog: AlertDialog.Builder
    private lateinit var storage: FirebaseStorage
   // private lateinit var selectedImg : Uri
    private lateinit var storageRef : StorageReference
    lateinit var filepath : Uri


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


        //Dialogo de alerta
        dialog = AlertDialog.Builder(this)
            .setMessage("Actualizando Foto de Perfil")
            .setCancelable(false)

        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()

        //Funcion que carga la imagen de ususario
        userProfileImage(userid)

        //Selector de Perfil
        PickImageBtn()

    }

    //----------Funcion para seleccionar foto de perfil----------//

    private fun PickImageBtn(){
        binding.userphoto.setOnClickListener{
            val dialog = AlertDialog.Builder(this)
                .setTitle("Elige una opcion")
                .setMessage("Puede escoger una foto o hacerte una para tu perfil")
                .setNegativeButton("CANCELAR"){view, _->
                    view.dismiss()
                }

                .setPositiveButton("ELEGIR FOTO"){view,_ ->
                    startFileChooser()
                    Toast.makeText(this,"Has escogido elegir foto", Toast.LENGTH_SHORT).show()
                }
                .setCancelable(false)
                .create()
            dialog.show()
        }
    }

    //--------------------UploadImage Function------------------------------//


    private fun startFileChooser() {
        var i = Intent()
        i.setType("image/*")
        i.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(i, "Elige una foto"),111)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val objetoIntent: Intent = intent
        val Nombre = objetoIntent.getStringExtra("Nombre")

        val subtitle = findViewById<TextView>(R.id.subTitleTv)
        subtitle.text = "$Nombre"

        //Enviamos el user a las otras Activities
        val useride = "$Nombre"
        subtitle.text = useride


        if (requestCode==111 && resultCode == Activity.RESULT_OK && data != null){
            filepath = data.data!!
            var bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filepath)
            binding.userphoto.setImageBitmap(bitmap)
            uploadFile(useride)
        }
    }

    private fun uploadFile(useride: String) {

        if (filepath != null){
            val pd = ProgressDialog(this)
            pd.setTitle("Uploading")
            pd.show()

            var imageRef = FirebaseStorage.getInstance().reference.child("Users").child("$useride").child("ProfileImage/pic.jpg")
            imageRef.putFile(filepath)
                .addOnSuccessListener {p0 ->
                    pd.dismiss()
                    Toast.makeText(applicationContext, "Archivo subido con exito!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener{p0 ->
                    pd.dismiss()

                    Toast.makeText(applicationContext, p0.message, Toast.LENGTH_SHORT).show()
                }
                .addOnProgressListener {p0 ->
                    var progress : Double = (100.0 * p0.bytesTransferred) / p0.totalByteCount
                    pd.setMessage("Uplodaded ${progress.toInt()}%")
                }
        }
    }



    //-------------------------User profile photo GET--------------------------------//

    private fun userProfileImage(userid: String){

        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Cargando Perfil...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        //val imageName = binding.etimageId.text.toString()
        val storageRef = FirebaseStorage.getInstance().reference.child("Users").child("$userid").child("ProfileImage/pic.jpg")

        val localFile = File.createTempFile("tempImage","jpg")
        storageRef.getFile(localFile).addOnSuccessListener {

            if (progressDialog.isShowing)
                progressDialog.dismiss()

            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            binding.userphoto.setImageBitmap(bitmap)

        }.addOnFailureListener{

            if (progressDialog.isShowing)
                progressDialog.dismiss()

            Toast.makeText(this,"Fallo al cargar la imagen desde la base de datos",Toast.LENGTH_SHORT).show()
        }
    }

    //-------------------------END User profile GET--------------------------------//

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

