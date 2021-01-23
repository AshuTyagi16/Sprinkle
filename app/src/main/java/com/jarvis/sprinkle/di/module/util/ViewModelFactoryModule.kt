package com.jarvis.sprinkle.di.module.util

import androidx.lifecycle.ViewModelProvider
import com.jarvis.sprinkle.util.DaggerViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: DaggerViewModelFactory): ViewModelProvider.Factory
}