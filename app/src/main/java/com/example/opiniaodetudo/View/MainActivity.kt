package com.example.opiniaodetudo.View

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import com.example.opiniaodetudo.R
import com.example.opiniaodetudo.model.Review
import com.example.opiniaodetudo.model.ReviewRepository

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonSave = findViewById<Button>(R.id.bt_record)
        val textViewName = findViewById<EditText>(R.id.et_nameInfor)
        val textViewReview = findViewById<EditText>(R.id.et_op)

        val reviewToEdit = (intent?.getSerializableExtra("item") as Review?)?.also { review ->
            textViewName.setText(review.name)
            textViewReview.setText(review.review)
        }

        buttonSave.setOnClickListener {
            val name = textViewName.text
            val review = textViewReview.text
            object: AsyncTask<Void, Void, Unit>() {
                override fun doInBackground(vararg params: Void?) {
                    val repository = ReviewRepository(this@MainActivity.applicationContext)
                    if(reviewToEdit == null){
                        repository.save(name.toString(), review.toString())
                        startActivity(Intent(this@MainActivity, ListActivity::class.java))
                    }else{
                        repository.update(reviewToEdit.id, name.toString(), review.toString())
                        finish()
                    }
                }
            }.execute()
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == R.id.menu_list_reviews){
            startActivity(Intent(this, ListActivity::class.java))
            return true
        }
        return false
    }

}
