package com.example.magiafitapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity



class ImcCalculator : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imc_calculator)

        val btnCalculator = findViewById<Button>(R.id.btnCalculator)
        val weight = findViewById<EditText>(R.id.weight)
        val height = findViewById<EditText>(R.id.height)
        val result = findViewById<TextView>(R.id.result)

        val backbtn = findViewById<ImageButton>(R.id.backBtn)

        backbtn.setOnClickListener {
            onBackPressed()

        }


        btnCalculator.setOnClickListener {

                val h = (height.text).toString().toFloat() /100
                val w = weight.text.toString().toFloat()
                val res = w/(h*h)
                result.text = "%.2f".format(res)
        }
    }
}