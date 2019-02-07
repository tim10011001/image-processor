package com.tim10011001.imageprocessor.data.models.transformations

import android.graphics.Bitmap

class TransformationModel {
    var cachedPath: String? = null

    var image: Bitmap? = null
        set(value) {
            field = value
            onModelChange?.invoke()
        }

    var backgroundColor: Int = 0
    var onModelChange: (() -> Unit)? = null


}