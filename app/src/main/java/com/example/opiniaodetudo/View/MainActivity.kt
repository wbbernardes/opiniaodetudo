package com.example.opiniaodetudo.View

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import com.example.opiniaodetudo.R
import com.example.opiniaodetudo.model.Review
import com.example.opiniaodetudo.model.ReviewRepository

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, FormFragment())
            .commit()
        configureAutoHiddenKeyboard()
    }

    private fun configureAutoHiddenKeyboard() {
        val mainContainer = findViewById<ConstraintLayout>(R.id.main_container)
        mainContainer.setOnTouchListener { v, event ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }

}
