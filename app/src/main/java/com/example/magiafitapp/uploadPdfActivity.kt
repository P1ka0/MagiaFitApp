package com.example.magiafitapp

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.magiafitapp.databinding.ActivityUploadPdfBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage


class uploadPdfActivity : AppCompatActivity() {

    //Setup view binding
    private lateinit var binding: ActivityUploadPdfBinding

    //Firebase auth
    private lateinit var firebaseAuth: FirebaseAuth

    //Setup progressDialog
    private lateinit var progressDialog: ProgressDialog

   // private var pdfUri: Uri? = null
    lateinit var filepath : Uri

    //TAG
    private val TAG = "PDF_ADD_TAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadPdfBinding.inflate(layoutInflater)
        setContentView(binding.root)



        //Iniciamos el firebase auth
        firebaseAuth = FirebaseAuth.getInstance()

        //Seteamos los progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espera por favor... ")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.button.setOnClickListener {
            pdfPickIntent()
        }

        binding.button2.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
                .setTitle("Alerta!")
                .setMessage("¿Estás segur@ de que has escogido el documento correctamente?")
                .setNegativeButton("Cancelar"){view,_->
                    view.dismiss()
                }
                .setPositiveButton("Aceptar"){view, _ ->
                    uploadToStorage()
                }
                .setCancelable(false)
                .create()
            dialog.show()
        }

        binding.backBtn.setOnClickListener{
            onBackPressed()
            finish()
        }
    }

    private fun pdfPickIntent(){
        Log.d(TAG, "pdfPickIntent: starting pdf pick intent")
        var i = Intent()
        i.type="application/pdf"
        i.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(i, "Elige un documento"),222)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==222 && resultCode == Activity.RESULT_OK && data != null){
            filepath = data.data!!

        }
    }

    private var docName = ""

    private fun uploadToStorage() {

        val objetoIntent: Intent = intent
        val Nombre = objetoIntent.getStringExtra("Nombre")

        docName = binding.docNameEt.text.toString().trim()
        if (docName.isEmpty())
        {
            Toast.makeText(this, "Debes introducir un nombre para el Documento!", Toast.LENGTH_SHORT).show()
        }
        else
        {
            uploadDocs(Nombre)
        }

    }

    private fun uploadDocs(Nombre: String?) {
        //Subimos el pdf a firebase
        Log.d(TAG, "uploadPdfToStorage: subiendo al storage....")

           if (filepath != null){
               val pd = ProgressDialog(this)
               pd.setTitle("Uploading")
               pd.show()

               //Storage reference
               val storageReference = FirebaseStorage.getInstance().reference.child("Mediciones/${Nombre}/${docName}.pdf")
               storageReference.putFile(filepath)
                   .addOnSuccessListener {p0->
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
    }
