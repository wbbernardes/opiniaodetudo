package com.example.opiniaodetudo.model

import android.content.Context
import java.util.*

class ReviewRepository {

    private val reviewDao: ReviewDao

    constructor(context: Context){
        val reviewDatabase = ReviewDatabase.getInstance(context)
        reviewDao = reviewDatabase.reviewDao()
    }

//    private constructor()
//
//    companion object {
//        val instance : ReviewRepository = ReviewRepository()
//    }

//    private val data = mutableListOf<Review>()

    fun save(name: String, review: String) {
//        return data.add(Review(UUID.randomUUID().toString(), name, review))
        reviewDao.save(Review(UUID.randomUUID().toString(), name, review))
    }

    fun listAll(): List<Review> {
        return reviewDao.listAll()
    }

    fun delete(item: Review) {
        reviewDao.delete(item)
    }
}