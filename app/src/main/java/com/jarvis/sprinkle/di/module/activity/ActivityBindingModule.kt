package com.jarvis.sprinkle.di.module.activity

import com.jarvis.sprinkle.di.scope.PerActivityScope
import com.jarvis.sprinkle.ui.MainActivity
import com.jarvis.sprinkle.ui.detail.ProductDetailActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @PerActivityScope
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    internal abstract fun mainActivity(): MainActivity

    @PerActivityScope
    @ContributesAndroidInjector(modules = [ProductDetailActivityModule::class])
    internal abstract fun productDetailActivity(): ProductDetailActivity

}