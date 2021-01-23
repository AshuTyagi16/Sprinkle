package com.jarvis.sprinkle.di.component

import android.content.Context
import com.jarvis.sprinkle.SprinkleApp
import com.jarvis.sprinkle.di.module.activity.ActivityBindingModule
import com.jarvis.sprinkle.di.module.app.SprinkleAppModule
import com.jarvis.sprinkle.di.module.db.LocalDataSourceModule
import com.jarvis.sprinkle.di.module.network.RemoteDataSourceModule
import com.jarvis.sprinkle.di.module.util.SharedPreferenceModule
import com.jarvis.sprinkle.di.module.util.ViewModelFactoryModule
import com.jarvis.sprinkle.di.scope.SprinkleAppScope
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import timber.log.Timber

@SprinkleAppScope
@Component(
    modules = [
        SprinkleAppModule::class,
        AndroidSupportInjectionModule::class,
        ActivityBindingModule::class,
        SharedPreferenceModule::class,
        ViewModelFactoryModule::class,
        RemoteDataSourceModule::class,
        LocalDataSourceModule::class
    ]
)
interface SprinkleAppComponent : AndroidInjector<SprinkleApp> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): SprinkleAppComponent
    }

    fun timberTree(): Timber.Tree
}