package com.tim10011001.imageprocessor.presentation.presenters

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import com.tim10011001.imageprocessor.R
import com.tim10011001.imageprocessor.data.models.transformations.TransformationModel
import com.tim10011001.imageprocessor.interactors.transformations.TransformationsInteractor
import com.tim10011001.imageprocessor.presentation.ui.fragments.transformations.TransformationsContract
import java.lang.Exception
import java.lang.ref.WeakReference
import javax.inject.Inject

class TransformationsPresenter @Inject constructor(private val interactor: TransformationsInteractor): TransformationsContract.Presenter {

    private val loadVariants = arrayOf("Gallery", "Camera", "Network")
    private var viewRef: WeakReference<TransformationsContract.View>? = null
    private var currentImageSource: Bitmap? = null
    private var currentTransformationModel: TransformationModel? = null
    private val transformationsResults = arrayListOf<TransformationModel?>()

    private fun view(): TransformationsContract.View? {
        return viewRef?.get()
    }

    override fun attachView(view: TransformationsContract.View) {
        viewRef = WeakReference(view)
        loadSourceImage()
        loadCustomImages()
    }

    override fun detachView() {
        viewRef = null
    }

    override fun openGalleryScreen() {
        view()?.openGallery()
    }

    override fun openLoadVariants() {
        view()?.showSourceChoiceDialog(loadVariants)
    }

    override fun rotateImage() {
        val rotated = TransformationModel()
        calculateColor(rotated)
        rotated.onModelChange = {
            view()?.updateTransformationsView(transformationsResults)
        }

        transformationsResults.add(0, rotated)
        view()?.updateTransformationsView(transformationsResults)
        interactor.rotateImageAndCache(currentTransformationModel, rotated)
    }

    override fun invertColors() {
        val inverted = TransformationModel()
        calculateColor(inverted)
        inverted.onModelChange = {
            view()?.updateTransformationsView(transformationsResults)
        }

        transformationsResults.add(0, inverted)
        view()?.updateTransformationsView(transformationsResults)
        interactor.invertImageColorsAndCache(currentTransformationModel, inverted)
    }

    override fun reflectImage() {
        val reflected = TransformationModel()
        calculateColor(reflected)
        reflected.onModelChange = {
            view()?.updateTransformationsView(transformationsResults)
        }

        transformationsResults.add(0, reflected)
        view()?.updateTransformationsView(transformationsResults)
        interactor.reflectImageAndCache(currentTransformationModel, reflected)
    }


    private fun calculateColor(model: TransformationModel?) {
        if(transformationsResults.isNotEmpty()) {
            val firstModel = transformationsResults[0]
            if (firstModel != null) {
                if (firstModel.backgroundColor == R.color.colorPrimaryDark) {
                    model?.backgroundColor = android.R.color.darker_gray
                } else {
                    model?.backgroundColor = R.color.colorPrimaryDark
                }
            } else {
                model?.backgroundColor = R.color.colorPrimaryDark
            }
        } else {
            model?.backgroundColor = R.color.colorPrimaryDark
        }
    }

    override fun loadSourceImage() {
        view()?.showProgress()
        interactor.loadSourceImageFromStorage {
            if(it == null) {
                view()?.hideProgress()
                view()?.showLoadSourceBtn()
                return@loadSourceImageFromStorage
            }

            currentTransformationModel = it
            currentImageSource = currentTransformationModel?.image
            view()?.hideProgress()
            view()?.showImage(currentImageSource)
        }
    }


    private fun loadCustomImages() {
        interactor.loadCustomImages {
            transformationsResults.clear()
            transformationsResults.addAll(it)
            view()?.updateTransformationsView(transformationsResults)
        }
    }

    override fun removeModel(model: TransformationModel?) {
        if(currentTransformationModel == model) {
            currentTransformationModel = null
            currentImageSource = null
            view()?.showLoadSourceBtn()
        }

        transformationsResults.remove(model)
        view()?.updateTransformationsView(transformationsResults)
        interactor.removeModel(model)
    }

    override fun useAsSource(model: TransformationModel?) {
        currentTransformationModel = model
        currentImageSource = currentTransformationModel?.image
        interactor.changeSource(model)
        view()?.showImage(currentImageSource)
    }

    override fun prepareCamera() {
        view()?.openCameraForCapture()
    }

    override fun handleLoadVariant(loadVariant: Int) {
        when(loadVariant) {
            GALLERY -> openGalleryScreen()
            CAMERA -> prepareCamera()
            NETWORK -> prepareNetworkRequest()
        }
    }

    private fun prepareNetworkRequest() {
        view()?.showDownloadDialog()
    }

    override fun handleCapturedData(image: Bitmap) {
        interactor.saveCapturedData(image) {
            currentTransformationModel = it
            currentImageSource = it.image
            view()?.showImage(currentImageSource)
        }
    }

    override fun downloadImage(url: String) {
        Picasso.get().load(url).into(object: Target {
            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                view()?.showProgress()
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                view()?.showDownloadError()
                view()?.hideProgress()
            }

            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                interactor.saveCapturedData(bitmap) {
                    currentTransformationModel = it
                    currentImageSource = it.image
                    view()?.hideProgress()
                    view()?.showImage(currentImageSource)
                }
            }

        })
    }

    override fun loadExifInformation() {
        val cameraOwner = interactor.loadExifInfo(currentTransformationModel)
        view()?.showExifInfo(cameraOwner)
    }

    companion object {
        private const val GALLERY = 0
        private const val CAMERA = 1
        private const val NETWORK = 2
    }
}