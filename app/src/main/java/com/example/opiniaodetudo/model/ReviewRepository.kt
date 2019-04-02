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

    fun save(name: String, review: String, photoPath: String?, thumbnailBytes: ByteArray?) {
        reviewDao.save(Review(UUID.randomUUID().toString(), name, review, photoPath, thumbnailBytes))
    }

    fun listAll(): List<Review> {
        return reviewDao.listAll()
    }

    fun delete(item: Review) {
        reviewDao.delete(item)
    }

    fun update(review:Review) {
        reviewDao.update(review)
    }
}