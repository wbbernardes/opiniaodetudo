package com.example.opiniaodetudo.View

import android.arch.lifecycle.ViewModelProviders
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.opiniaodetudo.R
import com.example.opiniaodetudo.model.Review
import com.example.opiniaodetudo.model.ReviewRepository
import com.example.opiniaodetudo.viewModel.EditReviewViewModel

class EditDialogFragment : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
//        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustiomDialog)
        val view = inflater.inflate(R.layout.new_review_form_layout, null)
        populateView(view)
        configureSaveButton(view)
        return view
    }
    private fun configureSaveButton(view: View) {
        val textName = view.findViewById<EditText>(R.id.et_nameInfor)
        val textReview = view.findViewById<EditText>(R.id.et_op)
        val button = view.findViewById<Button>(R.id.bt_record)
        val viewModel = ViewModelProviders.of(activity!!).get(EditReviewViewModel::class.java)
        var review = viewModel.data.value!!
        button.setOnClickListener {
            val review = Review(review.id, textName.text.toString(), textReview.text.toString())
            object: AsyncTask<Void, Void, Unit>(){
                override fun doInBackground(vararg params: Void?) {
                    ReviewRepository(activity!!.applicationContext).update(review)
                }

                override fun onPostExecute(result: Unit?) {
                    viewModel.data.value = review
                    dismiss()
                }

            }.execute()
        }
    }
    private fun populateView(view: View) {
        val review = ViewModelProviders.of(activity!!).get(EditReviewViewModel::class.java).data.value
        view.findViewById<EditText>(R.id.et_nameInfor).setText(review!!.name)
        view.findViewById<EditText>(R.id.et_op).setText(review!!.review)
    }
    override fun onResume() {
        val params = dialog.window.attributes.apply {
            width = ViewGroup.LayoutParams.MATCH_PARENT
            height = ViewGroup.LayoutParams.MATCH_PARENT
        }
        dialog.window.attributes = params
        super.onResume()
    }
}