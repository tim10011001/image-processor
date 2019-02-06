package com.tim10011001.imageprocessor.interactors.transformations

import android.graphics.Bitmap
import com.tim10011001.imageprocessor.R
import com.tim10011001.imageprocessor.core.threading.ThreadHelper
import com.tim10011001.imageprocessor.data.models.transformations.TransformationModel
import com.tim10011001.imageprocessor.data.repositories.bitmaps.BitmapHelper
import com.tim10011001.imageprocessor.data.repositories.files.FilesRepository
import com.tim10011001.imageprocessor.data.repositories.prefs.PreferenceRepository
import javax.inject.Inject

class TransformationsInteractor @Inject constructor(private val filesRepository: FilesRepository,
                                                    private val preferenceRepository: PreferenceRepository,
                                                    private val bitmapHelper: BitmapHelper) {

    fun loadSourceImage(callback: (TransformationModel?) -> Unit) {
        val path = preferenceRepository.loadPickedImage()
        path?.apply {
            filesRepository.loadImage(this) { file ->
                ThreadHelper.getInstance()?.execute {
                    val model = TransformationModel()
                    model.cachedPath = file?.absolutePath
                    model.progress = 100
                    model.image = bitmapHelper.fileToThumbnail(file)
                    callback(model)
                }
            }
        }
    }

    fun rotateImageAndCache(source: TransformationModel?, result: TransformationModel) {
        filesRepository.loadImage(source?.cachedPath!!) { image ->
            bitmapHelper.rotateImage(image) { rotated ->
                filesRepository.cacheBitmap(rotated) { path ->
                    filesRepository.loadImage(path) { file ->
                        result.image = bitmapHelper.fileToThumbnail(file)
                        result.cachedPath = path
                        result.progress = 100
                    }
                }
            }
        }
    }


    fun invertImageColorsAndCache(source: TransformationModel?, result: TransformationModel) {
        filesRepository.loadImage(source?.cachedPath!!) { image ->
            bitmapHelper.invertImageColors(image) { inverted ->
                filesRepository.cacheBitmap(inverted) { path ->
                    filesRepository.loadImage(path) { file ->
                        result.image = bitmapHelper.fileToThumbnail(file)
                        result.cachedPath = path
                        result.progress = 100
                    }
                }
            }
        }
    }

    fun reflectImageAndCache(source: TransformationModel?, result: TransformationModel) {
        filesRepository.loadImage(source?.cachedPath!!) { image ->
            bitmapHelper.mirrorImage(image) { reflected ->
                filesRepository.cacheBitmap(reflected) { path ->
                    filesRepository.loadImage(path) { file ->
                        result.image = bitmapHelper.fileToThumbnail(file)
                        result.cachedPath = path
                        result.progress = 100
                    }
                }
            }
        }
    }

    fun loadCustomImages(callback: (List<TransformationModel?>) -> Unit) {
        filesRepository.loadSavedImages {
            ThreadHelper.getInstance()?.execute {
                val models = arrayListOf<TransformationModel>()
                val sorted = it.sorted().reversed()
                sorted.forEach { file ->
                    val bitmap = bitmapHelper.fileToBitmap(file)
                    val model = TransformationModel()
                    model.cachedPath = file.absolutePath
                    model.image = bitmap
                    model.progress = 100
                    model.backgroundColor = if (sorted.indexOf(file) % 2 == 0) {
                        R.color.colorPrimaryDark
                    } else {
                        android.R.color.darker_gray
                    }

                    models.add(model)
                    callback(models)
                }
            }
        }
    }

    fun removeModel(model: TransformationModel?) {
        filesRepository.removeFromStorage(model?.cachedPath)
    }

    fun changeSource(model: TransformationModel?) {
        preferenceRepository.savePickedImage(model?.cachedPath)
    }

    fun saveCapturedData(image: Bitmap?, callback: (TransformationModel) -> Unit) {
        filesRepository.cacheBitmap(image) { path ->
            preferenceRepository.savePickedImage(path)
            filesRepository.loadImage(path) { file ->
                ThreadHelper.getInstance()?.execute {
                    val model = TransformationModel()
                    model.cachedPath = file?.absolutePath
                    model.progress = 100
                    model.image = bitmapHelper.fileToThumbnail(file)
                    callback(model)
                }
            }
        }
    }
}