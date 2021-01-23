package com.jarvis.sprinkle.di.module.app

import com.jarvis.sprinkle.di.scope.SprinkleAppScope
import dagger.Module
import dagger.Provides
import timber.log.Timber

@Module
class SprinkleAppModule {

    @Provides
    @SprinkleAppScope
    fun timberTree(): Timber.Tree {
        return Timber.DebugTree()
    }
}