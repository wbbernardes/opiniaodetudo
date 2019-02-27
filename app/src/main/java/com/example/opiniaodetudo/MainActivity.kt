package com.example.opiniaodetudo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.opiniaodetudo.model.ReviewRepository
import com.example.opiniaodetudo.model.test

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonSave = findViewById<Button>(R.id.bt_record)
        val textViewName = findViewById<TextView>(R.id.tv_name)
        val textViewReview = findViewById<TextView>(R.id.tv_op)

        buttonSave.setOnClickListener {
            val name = textViewName.text
            val review = textViewReview.text
            if (ReviewRepository.instance.save(name.toString(), review.toString())) {
                Toast.makeText(this, "Registrado com sucesso", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Erro ao registrar", Toast.LENGTH_LONG).show()
            }
        }
    }
}
