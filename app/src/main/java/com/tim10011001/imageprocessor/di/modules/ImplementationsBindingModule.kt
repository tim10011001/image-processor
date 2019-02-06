package com.tim10011001.imageprocessor.di.modules

import com.tim10011001.imageprocessor.data.repositories.bitmaps.BitmapHelper
import com.tim10011001.imageprocessor.data.repositories.bitmaps.BitmapHelperImpl
import com.tim10011001.imageprocessor.data.repositories.files.FilesRepository
import com.tim10011001.imageprocessor.data.repositories.files.FilesRepositoryImpl
import com.tim10011001.imageprocessor.data.repositories.prefs.PreferenceRepository
import com.tim10011001.imageprocessor.data.repositories.prefs.PreferenceRepositoryImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class ImplementationsBindingModule {

    @Binds
    @Singleton
    abstract fun bindFilesRepository(filesRepository: FilesRepositoryImpl): FilesRepository

    @Binds
    @Singleton
    abstract fun bindPreferencesRepository(preferenceRepository: PreferenceRepositoryImpl): PreferenceRepository

    @Binds
    @Singleton
    abstract fun bindBitmapHelper(bitmapHelper: BitmapHelperImpl): BitmapHelper
}