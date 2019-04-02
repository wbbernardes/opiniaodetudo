package com.example.opiniaodetudo.model

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.migration.Migration
import android.provider.MediaStore
import java.io.Serializable

@Entity
data class Review(
    @PrimaryKey
    val id: String,
    val name: String,
    val review: String?,
    @ColumnInfo(name="photo_path")
    val photoPath: String?,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val thumbnails: ByteArray?

) : Serializable {
    @Ignore
    constructor(id: String, name: String, review: String?): this(id, name, review, null, null)
}

//data class Review(val id: String, val name: String, val review: String)