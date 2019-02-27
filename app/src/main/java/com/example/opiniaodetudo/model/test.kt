package com.example.opiniaodetudo.model

import java.util.*

object test {

    private val data = mutableListOf<Review>()

    fun save(name: String, review: String) {
        data.add(Review(UUID.randomUUID().toString(), name, review));
    }
}