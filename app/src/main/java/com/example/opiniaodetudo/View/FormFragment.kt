package com.example.opiniaodetudo.View

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.Button
import android.widget.EditText
import com.example.opiniaodetudo.R
import com.example.opiniaodetudo.model.Review
import com.example.opiniaodetudo.model.ReviewRepository

class FormFragment :Fragment() {

    private lateinit var mainView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        mainView = inflater.inflate(R.layout.new_review_form_layout, null)

        val buttonSave = mainView.findViewById<Button>(R.id.bt_record)
        val textViewName = mainView.findViewById<EditText>(R.id.et_nameInfor)
        val textViewReview = mainView.findViewById<EditText>(R.id.et_op)

        val reviewToEdit = (activity!!.intent?.getSerializableExtra("item") as Review?)?.also { review ->
            textViewName.setText(review.name)
            textViewReview.setText(review.review)
        }

        buttonSave.setOnClickListener {
            val name = textViewName.text
            val review = textViewReview.text
            object : AsyncTask<Void, Void, Unit>() {
                override fun doInBackground(vararg params: Void?) {
                    val repository = ReviewRepository(activity!!.applicationContext)
                    if (reviewToEdit == null) {
                        repository.save(name.toString(), review.toString())
//                        val i = Intent(activity!!.applicationContext, ListActivity::class.java)
//                        startActivity(i)

//                    } else {
//                        repository.update(reviewToEdit.id, name.toString(), review.toString())
//                        activity!!.finish()
                    }
                }

                override fun onPostExecute(result: Unit?) {
                    if (requireActivity() is MainActivity) {
                        (activity as MainActivity).navigateTo(MainActivity.LIST_FRAGMENT)
                    }
                }
            }.execute()
            true
        }
        return mainView

    }
}