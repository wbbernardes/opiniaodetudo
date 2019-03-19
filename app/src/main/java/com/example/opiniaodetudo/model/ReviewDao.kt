package com.example.opiniaodetudo.model

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface ReviewDao {

    @Insert
    fun save(review:Review)

    @Query("SELECT * from ${ReviewTableInfo.TABLE_NAME}")
    fun listAll():List<Review>

    @Delete
    fun delete(item: Review)

}