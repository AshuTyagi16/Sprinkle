package com.jarvis.sprinkle.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jarvis.sprinkle.data.model.Product

@Dao
interface ProductDao {

    @Query("SELECT * from product")
    fun getAllProducts(): LiveData<List<Product>>

    @Query("SELECT * from product")
    fun getAllProductsSync(): List<Product>

    @Query("SELECT * from product WHERE category = :category")
    fun getProductsForCategory(category: Int): LiveData<List<Product>>

    @Query("SELECT * from product WHERE isBookmarked = 1")
    fun getBookmarkedProducts(): LiveData<List<Product>>

    @Query("SELECT * from product WHERE id =:id")
    fun getProductForId(id: Int): LiveData<Product?>

    @Query("SELECT isUpVoted from product WHERE id = :id")
    fun isProductUpVoted(id: Int): Boolean

    @Query("UPDATE product SET isBookmarked = NOT isBookmarked WHERE id = :id")
    suspend fun toggleBookmarkState(id: Int)

    @Query("UPDATE product SET isUpVoted = 1 , upvotes = upvotes + 1 WHERE id = :id")
    suspend fun upVoteProduct(id: Int)

    @Query("UPDATE product SET isUpVoted = 0 , upvotes = upvotes - 1 WHERE id = :id")
    suspend fun downVoteProduct(id: Int)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(products: List<Product>)
}