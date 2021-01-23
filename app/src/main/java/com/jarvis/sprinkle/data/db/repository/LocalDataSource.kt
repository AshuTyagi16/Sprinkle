package com.jarvis.sprinkle.data.db.repository

import com.jarvis.sprinkle.data.db.dao.ProductDao
import com.jarvis.sprinkle.data.model.Product
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val productDao: ProductDao) {

    fun getAllProducts() = productDao.getAllProducts()

    fun getAllProductsSync() = productDao.getAllProductsSync()

    fun getProductsForCategory(category: Int) = productDao.getProductsForCategory(category)

    fun getBookmarkedProducts() = productDao.getBookmarkedProducts()

    fun getProductForId(id: Int) = productDao.getProductForId(id)

    fun isProductUpVoted(id: Int) = productDao.isProductUpVoted(id)

    suspend fun upVoteProduct(id: Int) = productDao.upVoteProduct(id)

    suspend fun downVoteProduct(id: Int) = productDao.downVoteProduct(id)

    suspend fun toggleBookmarkState(id: Int) = productDao.toggleBookmarkState(id)

    suspend fun insertAll(products: List<Product>) = productDao.insertAll(products)

}