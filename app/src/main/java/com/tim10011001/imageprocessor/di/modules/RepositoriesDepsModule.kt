package com.tim10011001.imageprocessor.di.modules

import android.content.Context
import android.content.SharedPreferences
import com.tim10011001.imageprocessor.BuildConfig
import com.tim10011001.imageprocessor.di.scopes.AppScope
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoriesDepsModule {

    @Provides
    @Singleton
    fun provideSharePreferences(@AppScope context: Context): SharedPreferences {
        return context.getSharedPreferences(BuildConfig.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }
}