package com.jarvis.sprinkle

import android.app.Application
import com.jarvis.sprinkle.di.component.DaggerSprinkleAppComponent
import com.jarvis.sprinkle.di.component.SprinkleAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import timber.log.Timber
import javax.inject.Inject

class SprinkleApp : Application(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    private lateinit var component: SprinkleAppComponent

    override fun onCreate() {
        super.onCreate()
        initComponent()
        initTimber()
    }

    private fun initComponent() {
        component = DaggerSprinkleAppComponent.factory().create(applicationContext)
        component.inject(this)
    }

    private fun initTimber() {
        Timber.plant(component.timberTree())
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return dispatchingAndroidInjector
    }
}