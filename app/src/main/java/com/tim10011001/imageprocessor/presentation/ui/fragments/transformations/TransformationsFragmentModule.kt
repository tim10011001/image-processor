package com.tim10011001.imageprocessor.presentation.ui.fragments.transformations

import com.tim10011001.imageprocessor.di.scopes.FragmentScope
import com.tim10011001.imageprocessor.presentation.presenters.TransformationsPresenter
import dagger.Binds
import dagger.Module

@Module
abstract class TransformationsFragmentModule {

    @FragmentScope
    @Binds
    abstract fun bindTransformationsPresenter(presenter: TransformationsPresenter): TransformationsContract.Presenter
}