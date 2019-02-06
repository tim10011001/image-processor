package com.tim10011001.imageprocessor.presentation.ui.fragments.transformations

import android.graphics.Bitmap
import com.tim10011001.imageprocessor.data.models.transformations.TransformationModel
import com.tim10011001.imageprocessor.presentation.BaseContract

interface TransformationsContract {
    interface View : BaseContract.View {
        fun showImage(currentImageSource: Bitmap?)
        fun showProgress()
        fun hideProgress()
        fun updateTransformationsView(transformationsResults: List<TransformationModel?>)
        fun openGallery()
        fun showSourceChoiceDialog(loadVariants: Array<String>)
        fun openCameraForCapture()
        fun showDownloadDialog()
        fun showDownloadError()
    }

    interface Presenter: BaseContract.Presenter<TransformationsContract.View> {
        fun openGalleryScreen()
        fun rotateImage()
        fun invertColors()
        fun reflectImage()
        fun loadSourceImage()
        fun removeModel(model: TransformationModel?)
        fun useAsSource(model: TransformationModel?)
        fun openLoadVariants()
        fun prepareCamera()
        fun handleLoadVariant(loadVariant: Int)
        fun handleCapturedData(image: Bitmap)
        fun downloadImage(url: String)
    }
}