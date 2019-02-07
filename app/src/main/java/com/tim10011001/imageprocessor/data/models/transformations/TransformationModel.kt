package com.tim10011001.imageprocessor.data.models.transformations

import android.graphics.Bitmap

class TransformationModel {
    var cachedPath: String? = null
        set(value) {
            field = value
            onModelChange?.invoke()
        }

    var image: Bitmap? = null
        set(value) {
            field = value
            onModelChange?.invoke()
            needProgress = false
        }

    var needProgress: Boolean = true

    var backgroundColor: Int = 0
    var onModelChange: (() -> Unit)? = null


}