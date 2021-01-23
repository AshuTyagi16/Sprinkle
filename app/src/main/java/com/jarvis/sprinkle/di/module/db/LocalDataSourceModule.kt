package com.jarvis.sprinkle.di.module.db

import android.content.Context
import androidx.room.Room
import com.jarvis.sprinkle.data.db.ProductDB
import com.jarvis.sprinkle.data.db.dao.ProductDao
import com.jarvis.sprinkle.data.db.repository.LocalDataSource
import com.jarvis.sprinkle.di.scope.SprinkleAppScope
import dagger.Module
import dagger.Provides

@Module
class LocalDataSourceModule {

    @Provides
    @SprinkleAppScope
    fun productDB(context: Context): ProductDB {
        return Room.databaseBuilder(
            context,
            ProductDB::class.java, "product_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @SprinkleAppScope
    fun productDao(productDB: ProductDB): ProductDao {
        return productDB.productDao()
    }

    @Provides
    @SprinkleAppScope
    fun localDataSource(productDao: ProductDao): LocalDataSource {
        return LocalDataSource(productDao)
    }
}