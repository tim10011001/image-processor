package com.tim10011001.imageprocessor.di

import com.tim10011001.imageprocessor.app.App

object AppInjector {
    fun inject(app: App) {
        DaggerAppComponent.builder()
                .application(app)
                .appContext(app)
                .build()
                .inject(app)
    }
}