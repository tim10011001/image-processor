package com.tim10011001.imageprocessor.presentation.ui.fragments.gallery

import com.tim10011001.imageprocessor.data.models.gallery.GalleryModel
import com.tim10011001.imageprocessor.presentation.BaseContract

interface GalleryContract {
    interface View: BaseContract.View {
        fun showGalleryModels(models: List<GalleryModel>)
        fun closeScreen()
        fun hideProgress()
    }

    interface Presenter: BaseContract.Presenter<View> {
        fun pickImage(galleryItem: GalleryModel)
        fun closeScreen()
    }
}