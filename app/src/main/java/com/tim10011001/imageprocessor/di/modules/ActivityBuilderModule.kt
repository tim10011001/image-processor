package com.tim10011001.imageprocessor.di.modules

import com.tim10011001.imageprocessor.di.scopes.ActivityScope
import com.tim10011001.imageprocessor.presentation.ui.HostActivity
import com.tim10011001.imageprocessor.presentation.ui.HostActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ActivityBuilderModule {

    @ContributesAndroidInjector(modules = [HostActivityModule::class])
    @ActivityScope
    fun provideHostActivity(): HostActivity

}