package com.example.opiniaodetudo.View

import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.PopupMenu
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import com.example.opiniaodetudo.R
import com.example.opiniaodetudo.model.Review
import com.example.opiniaodetudo.model.ReviewRepository

class ListActivity : AppCompatActivity() {

    private lateinit var reviews : MutableList<Review>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        val listView = findViewById<ListView>(R.id.list_recyclerview)
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initList(listView)
        configureOnLongClick(listView)
    }

    private fun initList(listView: ListView) {
        object: AsyncTask<Void, Void, ArrayAdapter<Review>>() {
            override fun doInBackground(vararg params: Void?): ArrayAdapter<Review> {
                val reviews = ReviewRepository(this@ListActivity.applicationContext)
                    .listAll().toMutableList()
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

    private fun delete(item: Review) {
        object: AsyncTask<Unit, Void, Unit>(){
            override fun doInBackground(vararg params: Unit?) {
                ReviewRepository(this@ListActivity.applicationContext).delete(item)
                reviews.remove(item)
            }
            override fun onPostExecute(result: Unit?) {
                val listView = findViewById<ListView>(R.id.list_recyclerview)
                val adapter = listView.adapter as ArrayAdapter<Review>
                adapter.notifyDataSetChanged()
            }
        }.execute()
    }

    private fun configureOnLongClick(listView: ListView) {
        listView.setOnItemLongClickListener { parent, view, position, id ->
            val popupMenu = PopupMenu(this@ListActivity, view)
            popupMenu.inflate(R.menu.list_review_item_menu)
            popupMenu.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.item_list_delete -> this@ListActivity.delete(reviews[position])
                }
                true
            }
            popupMenu.show()
            true
        }
    }



}
