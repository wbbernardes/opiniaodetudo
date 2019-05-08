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

    @Query("DELETE FROM ${ReviewTableInfo.TABLE_NAME} WHERE ${ReviewTableInfo.COLUMN_ID} = :id")
    fun deleteReview(id: String?)

}