package com.tim10011001.imageprocessor.interactors.gallery

import com.tim10011001.imageprocessor.core.threading.ThreadHelper
import com.tim10011001.imageprocessor.data.models.gallery.GalleryModel
import com.tim10011001.imageprocessor.data.repositories.bitmaps.BitmapHelper
import com.tim10011001.imageprocessor.data.repositories.files.FilesRepository
import com.tim10011001.imageprocessor.data.repositories.prefs.PreferenceRepository
import java.io.File
import java.lang.Exception
import javax.inject.Inject

class GalleryInteractor @Inject constructor(private val filesRepository: FilesRepository,
                                            private val preferenceRepository: PreferenceRepository,
                                            private val bitmapHelper: BitmapHelper) {

    fun loadImages(onSuccess: (List<GalleryModel>) -> Unit, onError: (Exception) -> Unit) {
        filesRepository.loadImages({
            fetchGalleryModels(it) { galleryModels ->
                onSuccess(galleryModels)
            }
        }, onError)
    }

    private fun fetchGalleryModels(files: List<File>, callback: (List<GalleryModel>) -> Unit) {
        ThreadHelper.getInstance()?.execute {
            val galleryModels = filesToGalleryModels(files)
            callback.invoke(galleryModels)
        }
    }

    private fun filesToGalleryModels(files: List<File>): List<GalleryModel> {
        val result = arrayListOf<GalleryModel>()

        files.forEach { file ->
            val bitmap = bitmapHelper.fileToThumbnail(file)
            result.add(GalleryModel(file, bitmap))
        }

        return result
    }


    fun savePickedImage(galleryItem: GalleryModel) {
        preferenceRepository.savePickedImage(galleryItem.imageFile.absolutePath)
    }
}