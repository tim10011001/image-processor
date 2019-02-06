package com.tim10011001.imageprocessor.data.repositories.bitmaps

import android.graphics.*
import com.tim10011001.imageprocessor.core.threading.ThreadHelper
import java.io.File
import javax.inject.Inject

class BitmapHelperImpl @Inject constructor(): BitmapHelper {
    override fun fileToThumbnail(file: File?): Bitmap {
        return prepareBitmap(file, false)
    }

    override fun fileToBitmap(file: File?): Bitmap {
        return prepareBitmap(file, true)
    }

    private fun prepareBitmap(file: File?, originalSize: Boolean): Bitmap {
        return BitmapFactory.Options().run {
            inJustDecodeBounds = true
            BitmapFactory.decodeFile(file?.absolutePath, this)

            inSampleSize = if(originalSize) {
                1
            } else {
                fetchSampleSize(this, REQ_WIDTH, REQ_HEIGHT)
            }

            inJustDecodeBounds = false

            BitmapFactory.decodeFile(file?.absolutePath, this)
        }
    }

    private fun fetchSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var sampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2

            while (halfHeight / sampleSize >= reqHeight && halfWidth / sampleSize >= reqWidth) {
                sampleSize *= 2
            }
        }

        return sampleSize
    }


    override fun rotateImage(original: File?, callback: (Bitmap?) -> Unit) {
        ThreadHelper.getInstance()?.execute {
            val source = fileToBitmap(original)
            val matrix = Matrix()
            matrix.postRotate(90f)
            val rotatedBitmap = Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
            source.recycle()
            callback(rotatedBitmap)
        }
    }

    override fun invertImageColors(original: File?, callback: (Bitmap?) -> Unit) {
        ThreadHelper.getInstance()?.execute {
            val source = fileToBitmap(original)
            val monoSource = Bitmap.createBitmap(source.width, source.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(monoSource)
            val colorMatrix = ColorMatrix()
            colorMatrix.setSaturation(0f)
            val paint = Paint()
            paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
            canvas.drawBitmap(source, 0f, 0f, paint)
            source.recycle()
            callback(monoSource)
        }
    }

    override fun mirrorImage(original: File?, callback: (Bitmap?) -> Unit) {
        ThreadHelper.getInstance()?.execute {
            val source = fileToBitmap(original)
            val matrix = Matrix()
            matrix.setScale(-1f, 1f)
            val reflected = Bitmap.createBitmap(source, 0,0, source.width, source.height, matrix, true)
            source.recycle()
            callback(reflected)
        }
    }



    companion object {
        private const val REQ_WIDTH = 100
        private const val REQ_HEIGHT = 100
    }
}