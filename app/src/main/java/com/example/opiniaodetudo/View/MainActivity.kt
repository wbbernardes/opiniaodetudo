package com.example.opiniaodetudo.View

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.opiniaodetudo.R

class MainActivity : AppCompatActivity() {

    private val fragments = mapOf(FORM_FRAGMENT to ::FormFragment, LIST_FRAGMENT to ::ListFragment)

    companion object {
        val FORM_FRAGMENT = R.id.menuitem_newitem
        val LIST_FRAGMENT = R.id.menuitem_listitem
        val GPS_PERMISSION_REQUEST: Int = 0
    }

    fun navigateTo(item: Int) {

        this.runOnUiThread {
            val bottomNavigationMenu = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
            val menuItem = bottomNavigationMenu.menu.findItem(item)

            menuItem.isChecked = true
        }

        val fragmentInstance: Fragment = fragments[item]?.invoke()!!
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragmentInstance)
//            .addToBackStack(null)
            .commit()
    }

    private fun askForGPSPermission() {
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MainActivity.GPS_PERMISSION_REQUEST
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            GPS_PERMISSION_REQUEST -> {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,
                        "Permissão para usar o GPS concedida",
                        Toast.LENGTH_SHORT)
                        .show()
                } }
        } }

    private fun configureBottomMenu() {
        val bottomNavigationMenu = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationMenu.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menuitem_newitem -> navigateTo(FORM_FRAGMENT)
                R.id.menuitem_listitem -> navigateTo(LIST_FRAGMENT)
            }
            true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigateTo(FORM_FRAGMENT)
        configureBottomMenu()
        configureAutoHiddenKeyboard()
        askForGPSPermission()
    }

    private fun configureAutoHiddenKeyboard() {
        val mainContainer = findViewById<ConstraintLayout>(R.id.main_container)
        mainContainer.setOnTouchListener { v, event ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }
}
