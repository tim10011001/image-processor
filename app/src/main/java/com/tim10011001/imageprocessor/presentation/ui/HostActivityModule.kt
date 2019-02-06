package com.tim10011001.imageprocessor.presentation.ui

import com.tim10011001.imageprocessor.di.scopes.ActivityScope
import com.tim10011001.imageprocessor.di.scopes.FragmentScope
import com.tim10011001.imageprocessor.presentation.presenters.HostActivityPresenter
import com.tim10011001.imageprocessor.presentation.ui.fragments.gallery.GalleryFragment
import com.tim10011001.imageprocessor.presentation.ui.fragments.gallery.GalleryFragmentModule
import com.tim10011001.imageprocessor.presentation.ui.fragments.transformations.TransformationsFragment
import com.tim10011001.imageprocessor.presentation.ui.fragments.transformations.TransformationsFragmentModule
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module
abstract class HostActivityModule {

    @Binds
    @ActivityScope
    abstract fun bindHostActivityPresenter(presenter: HostActivityPresenter): HostActivityContract.Presenter

    @ContributesAndroidInjector(modules = [GalleryFragmentModule::class])
    @FragmentScope
    abstract fun provideGalleryFragment(): GalleryFragment

    @ContributesAndroidInjector(modules = [TransformationsFragmentModule::class])
    @FragmentScope
    abstract fun provideTransformationsFragment(): TransformationsFragment

}