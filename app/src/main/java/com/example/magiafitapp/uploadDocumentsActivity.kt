package com.example.magiafitapp

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.magiafitapp.databinding.ActivityUploadDocumentsBinding
import com.google.firebase.storage.FirebaseStorage

class uploadDocumentsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadDocumentsBinding

    lateinit var filepath : Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadDocumentsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.button.setOnClickListener {
            startFileChooser()
        }

        binding.button2.setOnClickListener{

            uploadFile()
        }

    }



    private fun startFileChooser() {
        var i = Intent()
        i.setType("image/*")
        i.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(i, "Elige una foto"),111)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==111 && resultCode == Activity.RESULT_OK && data != null){
            filepath = data.data!!
            var bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filepath)
            binding.imageView.setImageBitmap(bitmap)


        }
    }

    private fun uploadFile() {
        if (filepath != null){
            val pd = ProgressDialog(this)
            pd.setTitle("Uploading")
            pd.show()

            var imageRef = FirebaseStorage.getInstance().reference.child("Images/pic.jpg")
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
}