package com.jarvis.sprinkle.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jarvis.sprinkle.data.db.dao.ProductDao
import com.jarvis.sprinkle.data.model.Product

@Database(entities = [Product::class], version = 1, exportSchema = false)
abstract class ProductDB : RoomDatabase() {
    abstract fun productDao(): ProductDao
}