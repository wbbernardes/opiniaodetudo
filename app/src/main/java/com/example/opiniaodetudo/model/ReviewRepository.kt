package com.example.opiniaodetudo.model

import java.util.*

class ReviewRepository {

    private constructor()

    companion object {
        val instance : ReviewRepository = ReviewRepository()
    }

    private val data = mutableListOf<Review>()

    fun save(name: String, review: String): Boolean {
        return data.add(Review(UUID.randomUUID().toString(), name, review))
    }

    fun listAll(): List<Review> {
        return data.toList()
    }
}