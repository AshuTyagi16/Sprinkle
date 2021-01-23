package com.jarvis.sprinkle.data.network.repository

import com.jarvis.sprinkle.data.network.service.ProductService
import com.jarvis.sprinkle.data.network.base.BaseDataSource
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val productService: ProductService) :
    BaseDataSource() {

    suspend fun getProducts() = getResult { productService.getProducts() }
}