package com.example.opiniaodetudo.viewModel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import com.example.opiniaodetudo.model.Review

class EditReviewViewModel : ViewModel() {

    var data: MutableLiveData<Review> = MutableLiveData()

}