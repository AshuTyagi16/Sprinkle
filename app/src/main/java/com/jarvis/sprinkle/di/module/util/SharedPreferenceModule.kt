package com.jarvis.sprinkle.di.module.util

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.jarvis.sprinkle.di.scope.SprinkleAppScope
import com.jarvis.sprinkle.util.SharedPreferenceUtil
import dagger.Module
import dagger.Provides

@Module
class SharedPreferenceModule {

    @Provides
    @SprinkleAppScope
    fun sharedPreferenceUtil(preferences: SharedPreferences): SharedPreferenceUtil {
        return SharedPreferenceUtil(preferences)
    }

    @Provides
    @SprinkleAppScope
    fun preferences(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }
}