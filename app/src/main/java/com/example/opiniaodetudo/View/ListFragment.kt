package com.example.opiniaodetudo.View

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.widget.PopupMenu
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import com.example.opiniaodetudo.R
import com.example.opiniaodetudo.model.Review
import com.example.opiniaodetudo.model.ReviewRepository
import com.example.opiniaodetudo.viewModel.EditReviewViewModel

class ListFragment: Fragment() {
    private lateinit var reviews: MutableList<Review>
    private lateinit var rootView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.list_review_activity, null)
        val listView = rootView.findViewById<ListView>(R.id.list_recyclerview)
        initList(listView)
        configureOnLongClick(listView)
        configureListObserver()
        return rootView
    }

    override fun onResume() {
        super.onResume()
        object : AsyncTask<Unit, Void, Unit>() {
            override fun doInBackground(vararg params: Unit?) {
                reviews.clear()
                reviews.addAll(ReviewRepository(activity!!.applicationContext).listAll())
            }
            override fun onPostExecute(result: Unit?) {
                val listView = rootView.findViewById<ListView>(R.id.list_recyclerview)
                val adapter = listView.adapter as ArrayAdapter<Review>
                adapter.notifyDataSetChanged()
            }
        }.execute()
    }

    private fun configureListObserver() {
        val reviewViewModel = ViewModelProviders.of(activity!!).get(EditReviewViewModel::class.java)
        reviewViewModel.data.observe(this, Observer {
            onResume()
        })
    }

    private fun initList(listView: ListView) {
        object: AsyncTask<Void, Void, ArrayAdapter<Review>>() {
            override fun doInBackground(vararg params: Void?): ArrayAdapter<Review> {
                reviews = ReviewRepository(activity!!.applicationContext)
                    .listAll().toMutableList()
                val adapter =
                    object : ArrayAdapter<Review>(activity!!, -1, reviews ){
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
                ReviewRepository(activity!!.applicationContext).delete(item)
                reviews.remove(item)
            }
            override fun onPostExecute(result: Unit?) {
                val listView = rootView.findViewById<ListView>(R.id.list_recyclerview)
                val adapter = listView.adapter as ArrayAdapter<Review>
                adapter.notifyDataSetChanged()
            }
        }.execute()
    }

//    private fun configureOnLongClick(listView: ListView) {
//        listView.setOnItemLongClickListener { parent, view, position, id ->
//            val popupMenu = PopupMenu(activity!!, view)
//            popupMenu.inflate(R.menu.list_review_item_menu)
//            popupMenu.setOnMenuItemClickListener {
//                when(it.itemId){
//                    R.id.item_list_delete -> this.delete(reviews[position])
//                    R.id.item_list_edit -> this.openItemForEdition(reviews[position])
//                }
//                true
//            }
//            popupMenu.show()
//            true
//        }
//    }

    private fun openItemForEdition(item: Review) {
        val reviewViewModel = ViewModelProviders.of(activity!!).get(EditReviewViewModel::class.java)
        val data = reviewViewModel.data
        data.value = item
        EditDialogFragment().show(fragmentManager, "edit_dialog")
    }

    private fun configureOnLongClick(listView: ListView) {
        listView.setOnItemLongClickListener { _, view, position, _ ->
            val popupMenu = PopupMenu(activity!!, view)
            popupMenu.inflate(R.menu.list_review_item_menu)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.item_list_delete -> askForDelete(reviews[position])
                    R.id.item_list_edit -> openItemForEdition(reviews[position])
                }
                true
            }
            popupMenu.show()
            true
        }
    }

    private fun askForDelete(item: Review) {
        AlertDialog.Builder(activity!!)
            .setMessage(R.string.delete_confirmation)
            .setPositiveButton(R.string.ok) { _, _ ->
                this.delete(item)
            }
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}