package com.example.opiniaodetudo.View

import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import com.example.opiniaodetudo.R
import com.example.opiniaodetudo.model.Review
import com.example.opiniaodetudo.model.ReviewRepository

class ListActivity : AppCompatActivity() {

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_list)
//        val listView = findViewById<ListView>(R.id.list_recycleview)
//
//        val reviews = ReviewRepository.instance.listAll() //ainda vamos implementar a listagem
////        val stringList = reviews.map { "${it.name} - ${it.review}" }
////        val adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, stringList )
//        val adapter = object : ArrayAdapter<Review>(this, -1, reviews ){
//            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
//                val itemView = layoutInflater.inflate(R.layout.review_list_item_layout, null)
//                val item = reviews[position]
//                val textViewName = itemView.findViewById<TextView>(R.id.item_name)
//                val textViewReview = itemView.findViewById<TextView>(R.id.item_review)
//                textViewName.text = item.name
//                textViewReview.text = item.review
//                return itemView
//            }
//        }
//        listView.adapter = adapter
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        val listView = findViewById<ListView>(R.id.list_recycleview)
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initList(listView)
    }

    private fun initList(listView: ListView) {
        object: AsyncTask<Void, Void, ArrayAdapter<Review>>() {
            override fun doInBackground(vararg params: Void?): ArrayAdapter<Review> {
                val reviews = ReviewRepository(this@ListActivity.applicationContext)
                    .listAll()
                val adapter =
                    object : ArrayAdapter<Review>(this@ListActivity, -1, reviews ){
                        override fun getView(
                            position: Int,
                            convertView: View?,
                            parent: ViewGroup?): View {
                            val itemView = layoutInflater.inflate(R.layout.review_list_item_layout, null)
                            val item = reviews[position]
                            val textViewName = itemView
                                .findViewById<TextView>(R.id.item_name)
                            val textViewReview = itemView
                                .findViewById<TextView>(R.id.item_review)
                            textViewName.text = item.name
                            textViewReview.text = item.review
                            return itemView
                        }
                    }
                return adapter
            }
            override fun onPostExecute(adapter: ArrayAdapter<Review>) {
                listView.adapter = adapter
            }
        }.execute()
    }
}
