package com.example.opiniaodetudo.model

import android.arch.persistence.room.*

@Dao
interface ReviewDao {

    @Insert
    fun save(review:Review)

    @Query("SELECT * from ${ReviewTableInfo.TABLE_NAME}")
    fun listAll():List<Review>

    @Delete
    fun delete(item: Review)

    @Update
    fun update(review: Review)

}