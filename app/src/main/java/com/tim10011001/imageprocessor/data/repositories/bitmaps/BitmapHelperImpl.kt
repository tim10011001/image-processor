package com.tim10011001.imageprocessor.data.repositories.bitmaps

import android.graphics.*
import android.media.ExifInterface
import android.media.ExifInterface.*
import android.util.Log
import com.tim10011001.imageprocessor.core.threading.ThreadHelper
import java.io.File
import javax.inject.Inject
import kotlin.random.Random

class BitmapHelperImpl @Inject constructor() : BitmapHelper {
    override fun fileToThumbnail(file: File?): Bitmap {
        return prepareBitmap(file, false)
    }

    override fun fileToBitmap(file: File?): Bitmap {
        return prepareBitmap(file, true)
    }

    private fun prepareBitmap(file: File?, originalSize: Boolean): Bitmap {
        val bitmap = BitmapFactory.Options().run {
            inJustDecodeBounds = true
            BitmapFactory.decodeFile(file?.absolutePath, this)

            inSampleSize = if (originalSize) {
                1
            } else {
                fetchSampleSize(this, REQ_WIDTH, REQ_HEIGHT)
            }

            inJustDecodeBounds = false

            BitmapFactory.decodeFile(file?.absolutePath, this)
        }

        val exifInterface = ExifInterface(file?.absolutePath)
        val exifOrientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

        val exifRotation = fetchExifRotation(exifOrientation)
        val exifTranslation = fetchExifTranslation(exifOrientation)

        val matrix = Matrix()
        if(exifRotation != 0) {
            matrix.preRotate(exifRotation.toFloat())
        }

        if(exifTranslation != 1) {
            matrix.preScale(exifTranslation.toFloat(), 1f)
        }

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun fetchExifRotation(exifOrientation: Int): Int {
        return when (exifOrientation) {
            ORIENTATION_ROTATE_90, ORIENTATION_TRANSPOSE -> 90
            ORIENTATION_ROTATE_180, ORIENTATION_FLIP_VERTICAL -> 180
            ORIENTATION_ROTATE_270, ORIENTATION_TRANSVERSE -> 270
            else -> 0
        }
    }

    private fun fetchExifTranslation(exifOrientation: Int): Int {
        return when(exifOrientation) {
            ORIENTATION_FLIP_HORIZONTAL, ORIENTATION_FLIP_VERTICAL, ORIENTATION_TRANSPOSE, ORIENTATION_TRANSVERSE -> -1
            else -> 1
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
            sleepAndLogTime()
            val source = fileToBitmap(original)
            val matrix = Matrix()
            matrix.postRotate(90f)
            val rotatedBitmap = Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
            source.recycle()
            callback(rotatedBitmap)
        }
    }

    private fun createRandomTime(): Long {
        return Random.nextLong(30000)
    }

    override fun invertImageColors(original: File?, callback: (Bitmap?) -> Unit) {
        ThreadHelper.getInstance()?.execute {
            sleepAndLogTime()
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
            sleepAndLogTime()
            val source = fileToBitmap(original)
            val matrix = Matrix()
            matrix.setScale(-1f, 1f)
            val reflected = Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
            source.recycle()
            callback(reflected)
        }
    }

    private fun sleepAndLogTime() {
        val randomTime = createRandomTime()
        Log.e(this::class.simpleName, "Random time -> $randomTime")
        Thread.sleep(randomTime)
    }

    companion object {
        private const val REQ_WIDTH = 100
        private const val REQ_HEIGHT = 100
    }
}