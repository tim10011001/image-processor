package com.tim10011001.imageprocessor.presentation.ui.fragments.gallery

import com.tim10011001.imageprocessor.di.scopes.FragmentScope
import com.tim10011001.imageprocessor.presentation.presenters.GalleryPresenter
import dagger.Binds
import dagger.Module

@Module
abstract class GalleryFragmentModule {

    @FragmentScope
    @Binds
    abstract fun bindGalleryPresenter(presenter: GalleryPresenter): GalleryContract.Presenter
}