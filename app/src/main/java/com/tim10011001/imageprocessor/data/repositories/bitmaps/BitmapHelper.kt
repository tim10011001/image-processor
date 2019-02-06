package com.tim10011001.imageprocessor.data.repositories.bitmaps

import android.graphics.Bitmap
import java.io.File

interface BitmapHelper {
    fun fileToThumbnail(file: File?): Bitmap
    fun fileToBitmap(file: File?): Bitmap
    fun rotateImage(original: File?, callback: (Bitmap?) -> Unit)
    fun invertImageColors(original: File?, callback: (Bitmap?) -> Unit)
    fun mirrorImage(original: File?, callback: (Bitmap?) -> Unit)
}