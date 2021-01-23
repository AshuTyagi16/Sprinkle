package com.jarvis.sprinkle.data.network.service

import com.jarvis.sprinkle.data.model.Product
import retrofit2.Response
import retrofit2.http.GET

interface ProductService {

    @GET("b/600c7ac6973914580689055c/3")
    suspend fun getProducts(): Response<List<Product>>
}