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

    fun save(name: String, review: String, photoPath: String?, thumbnailBytes: ByteArray?): Review {
        val entity = Review(UUID.randomUUID().toString(),
            name, review, photoPath, thumbnailBytes)
        reviewDao.save(entity)
        return entity
    }

    fun listAll(): List<Review> {
        return reviewDao.listAll()
    }

    fun delete(item: Review) {
        reviewDao.delete(item)
    }

    fun update(id: String, name: String, review: String): Review {
        val entity = Review(id, name, review, null, null, null, null)
        reviewDao.update(entity)
        return entity
    }

    fun updateLocation(entity: Review, lat: Double, long: Double) {
        entity.latitude = lat
        entity.longitude = long
        reviewDao.update(entity)
    }
}