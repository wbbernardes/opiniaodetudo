package com.example.opiniaodetudo.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class Review(
    @PrimaryKey
    val id: String,
    val name: String,
    val review: String?
)

//data class Review(val id: String, val name: String, val review: String)