package com.tim10011001.imageprocessor.data.repositories.files

import android.graphics.Bitmap
import java.io.File
import java.lang.Exception

interface FilesRepository {
    fun loadImages(onSuccess: (List<File>) -> Unit, onError: (Exception) -> Unit)
    fun loadImage(path: String, callback: (File?) -> Unit)
    fun cacheBitmap(bitmap: Bitmap?, callback: (String) -> Unit)
    fun loadSavedImages(callback: (List<File>) -> Unit)
    fun removeFromStorage(cachedPath: String?)
    fun isImageExist(path: String?): Boolean
    fun changeExifInfo(path: String)
    fun loadExifInfo(cachedPath: String?): String?
}