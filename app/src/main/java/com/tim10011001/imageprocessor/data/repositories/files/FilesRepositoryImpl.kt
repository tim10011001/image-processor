package com.tim10011001.imageprocessor.data.repositories.files

import android.graphics.Bitmap
import android.os.Environment
import android.support.media.ExifInterface
import android.util.Log
import com.tim10011001.imageprocessor.BuildConfig
import com.tim10011001.imageprocessor.core.threading.ThreadHelper
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.util.*
import javax.inject.Inject

class FilesRepositoryImpl @Inject constructor(): FilesRepository {
    private val filesExtensions = listOf("jpg", "jpeg", "png")
    private val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    private val picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
    private val dcimDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)

    override fun loadImages(onSuccess: (List<File>) -> Unit, onError: (Exception) -> Unit) {
        loadImagesFromDownloadDir(onSuccess, onError)
        loadImagesFromPicturesDir(onSuccess, onError)
        loadImagesFromDCIMDir(onSuccess, onError)
    }

    private fun loadImagesFromDownloadDir(onSuccess: (List<File>) -> Unit, onError: (Exception) -> Unit) {
        loadImagesFromDir(downloadsDir, onSuccess, onError)
    }

    private fun loadImagesFromPicturesDir(onSuccess: (List<File>) -> Unit, onError: (Exception) -> Unit) {
        loadImagesFromDir(picturesDir, onSuccess, onError)
    }

    private fun loadImagesFromDCIMDir(onSuccess: (List<File>) -> Unit, onError: (Exception) -> Unit) {
        loadImagesFromDir(dcimDir, onSuccess, onError)
    }

    private fun loadImagesFromDir(dir: File?, onSuccess: (List<File>) -> Unit, onError: (Exception) -> Unit) {
        val images = mutableListOf<File>()

        ThreadHelper.getInstance()?.execute {
            if(dir != null) {
                if(dir.isDirectory) {
                    val dirs = arrayListOf<File>()
                    val files = arrayListOf<File>()

                    dir.list().forEach {
                        val file = File("${dir.absolutePath}/$it")

                        if(file.isDirectory) {
                            dirs.add(file)
                        } else {
                            files.add(file)
                        }
                    }

                    searchImagesIn(files) {
                        images.addAll(it)
                    }

                    scanOtherDirs(dirs, onSuccess, onError)
                } else if (isFileImage(dir)) {
                    images.add(dir)
                }
            }

            onSuccess(images)
        }
    }

    private fun scanOtherDirs(dirs: List<File>, onSuccess: (List<File>) -> Unit, onError: (Exception) -> Unit) {
        dirs.forEach {
            loadImagesFromDir(it, onSuccess, onError)
        }
    }

    private fun searchImagesIn(files: List<File>, onSuccess: (List<File>) -> Unit) {
        val images = arrayListOf<File>()
        for (file in files) {
            if(isFileImage(file)) {
                images.add(file)
            }
        }

        onSuccess.invoke(images)
    }

    private fun isFileImage(file: File): Boolean {
        for (extension in filesExtensions) {
            if(file.name.endsWith(extension)) {
                return true
            }
        }

        return false
    }

    override fun loadImage(path: String, callback: (File?) -> Unit) {
        val file = File(path)
        if(file.exists()) {
            callback(file)
        }
    }

    override fun cacheBitmap(bitmap: Bitmap?, callback: (String) -> Unit){
        val file = createOrGetCacheDir()
        val bitmapFile = File(file, createBitmapName())
        val outputStream = FileOutputStream(bitmapFile)
        ThreadHelper.getInstance()?.execute {
            try {
                bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.flush()
            }catch (e: IOException) {
            } finally {
                outputStream.close()
                callback(bitmapFile.absolutePath)
            }
        }
    }

    private fun createBitmapName(): String {
        return "${System.currentTimeMillis()}.jpeg"
    }

    private fun createOrGetCacheDir(): File {
        val file = File(picturesDir.absolutePath + File.separator + CACHE_DIR_NAME)
        if(!file.exists()) {
            file.mkdirs()
        }

        return file
    }

    override fun loadSavedImages(callback: (List<File> )-> Unit) {
        val cacheDir = createOrGetCacheDir()
        loadImagesFromDir(cacheDir, {
            callback(it)
        }, {
            callback(Collections.emptyList())
        })
    }

    override fun removeFromStorage(cachedPath: String?) {
        val image = File(cachedPath)
        if(image.exists()) {
            image.delete()
        }
    }

    override fun isImageExist(path: String?): Boolean {
        if(path == null) return false
        val image = File(path)

        return image.exists()
    }

    override fun changeExifInfo(path: String) {
        try {
            val exifInterface = ExifInterface(path)
            exifInterface.setAttribute(ExifInterface.TAG_CAMARA_OWNER_NAME, BuildConfig.APPLICATION_ID)
            exifInterface.saveAttributes()
        } catch (e: IOException) {
            Log.e(this::class.simpleName, e.message)
        }
    }

    override fun loadExifInfo(cachedPath: String?): String? {
        if(cachedPath == null) {
            return null
        }

        var cameraOwner: String? = null
        try {
            val exifInterface = ExifInterface(cachedPath)
            cameraOwner = exifInterface.getAttribute(ExifInterface.TAG_CAMARA_OWNER_NAME)
        } catch (e: IOException) {
            Log.e(this::class.simpleName, e.message)
        }

        return cameraOwner
    }

    companion object {
        private const val CACHE_DIR_NAME = "ImageProcessor"
    }
}