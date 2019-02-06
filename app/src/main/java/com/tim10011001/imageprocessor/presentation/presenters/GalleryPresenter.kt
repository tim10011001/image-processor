package com.tim10011001.imageprocessor.presentation.presenters

import com.tim10011001.imageprocessor.data.models.gallery.GalleryModel
import com.tim10011001.imageprocessor.interactors.gallery.GalleryInteractor
import com.tim10011001.imageprocessor.presentation.ui.fragments.gallery.GalleryContract
import java.lang.ref.WeakReference
import javax.inject.Inject

class GalleryPresenter @Inject constructor(private val galleryInteractor: GalleryInteractor) : GalleryContract.Presenter {

    private var viewRef: WeakReference<GalleryContract.View>? = null
    private val galleryCache = arrayListOf<GalleryModel>()

    private fun view(): GalleryContract.View? {
        return viewRef?.get()
    }

    override fun attachView(view: GalleryContract.View) {
        viewRef = WeakReference(view)
        loadImages()
    }

    override fun detachView() {
        viewRef = null
    }

    private fun loadImages() {
        if (galleryCache.isEmpty()) {
            galleryInteractor.loadImages({ it ->
                galleryCache.addAll(it)
                if (galleryCache.isNotEmpty()) {
                    view()?.hideProgress()
                }
                view()?.showGalleryModels(galleryCache)

            }, { e -> e.printStackTrace() })
        }
    }

    override fun pickImage(galleryItem: GalleryModel) {
        galleryInteractor.savePickedImage(galleryItem)
        closeScreen()
    }

    override fun closeScreen() {
        view()?.closeScreen()
    }
}