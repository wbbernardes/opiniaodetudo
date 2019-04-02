package com.example.opiniaodetudo.model

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.migration.Migration
import android.content.Context

@Database(entities = arrayOf(Review::class), version = 2)
abstract class ReviewDatabase : RoomDatabase(){

    companion object {
        private var instance: ReviewDatabase? = null

        private var migration_1_2 = object: Migration(1,2){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE ${ReviewTableInfo.TABLE_NAME} " +
                        "ADD COLUMN ${ReviewTableInfo.COLUMN_PHOTO_PATH} TEXT")
                database.execSQL("ALTER TABLE ${ReviewTableInfo.TABLE_NAME} " +
                        "ADD COLUMN ${ReviewTableInfo.COLUMN_THUMBNAIL} BLOB")
            }
        }

        fun getInstance(context: Context): ReviewDatabase {
            if(instance == null){
                instance = Room
                    .databaseBuilder(context, ReviewDatabase::class.java, "review_database")
                    .addMigrations(migration_1_2)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return instance!!
        }
    }
    abstract fun reviewDao():ReviewDao
}