package com.example.opiniaodetudo.View

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import com.example.opiniaodetudo.R
import com.example.opiniaodetudo.model.ReviewRepository

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonSave = findViewById<Button>(R.id.bt_record)
        val name = findViewById<EditText>(R.id.et_nameInfor)
        val review = findViewById<EditText>(R.id.et_op)

        buttonSave.setOnClickListener {
            object: AsyncTask<Void, Void, Unit>() {

                override fun doInBackground(vararg params: Void?) {

                    val repository = ReviewRepository(this@MainActivity.applicationContext)
                    repository.save(name.text.toString(), review.text.toString())
                    startActivity(Intent(this@MainActivity, ListActivity::class.java))
                }
            }.execute()
//            val name = textViewName.text
//            val review = textViewReview.text
//            if (ReviewRepository.instance.save(name.toString(), review.toString())) {
//                Toast.makeText(this, "Registrado com sucesso", Toast.LENGTH_LONG).show()
//                startActivity(Intent(this, ListActivity::class.java))
//            } else {
//                Toast.makeText(this, "Erro ao registrar", Toast.LENGTH_LONG).show()
//            }
        }
    }
}
