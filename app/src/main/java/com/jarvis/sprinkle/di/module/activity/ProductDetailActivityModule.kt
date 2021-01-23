package com.jarvis.sprinkle.di.module.activity

import androidx.lifecycle.ViewModel
import com.jarvis.sprinkle.di.mapkey.ViewModelKey
import com.jarvis.sprinkle.ui.detail.ProductDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ProductDetailActivityModule {

    @Binds
    @IntoMap
    @ViewModelKey(ProductDetailViewModel::class)
    abstract fun bindProductDetailViewModel(productDetailViewModel: ProductDetailViewModel): ViewModel
}