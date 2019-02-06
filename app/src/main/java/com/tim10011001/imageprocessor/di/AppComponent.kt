package com.tim10011001.imageprocessor.di

import android.app.Application
import android.content.Context
import com.tim10011001.imageprocessor.app.App
import com.tim10011001.imageprocessor.di.modules.ActivityBuilderModule
import com.tim10011001.imageprocessor.di.modules.ImplementationsBindingModule
import com.tim10011001.imageprocessor.di.modules.RepositoriesDepsModule
import com.tim10011001.imageprocessor.di.scopes.AppScope
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    AndroidInjectionModule::class,
    ActivityBuilderModule::class,
    RepositoriesDepsModule::class,
    ImplementationsBindingModule::class
])
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(app: Application): Builder

        @BindsInstance
        fun appContext(@AppScope context: Context): Builder

        fun build(): AppComponent
    }

    fun inject(app: App)
}