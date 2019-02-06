package com.tim10011001.imageprocessor.data.repositories.prefs

interface PreferenceRepository {
    fun loadPickedImage(): String?
    fun savePickedImage(path: String?)
}