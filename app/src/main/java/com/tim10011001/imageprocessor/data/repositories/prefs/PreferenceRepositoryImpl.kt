package com.tim10011001.imageprocessor.data.repositories.prefs

import android.content.SharedPreferences
import javax.inject.Inject

class PreferenceRepositoryImpl @Inject constructor(private val prefs: SharedPreferences): PreferenceRepository {

    override fun loadPickedImage(): String? {
        return prefs.getString(PICKED_IMAGE_PATH, null)
    }

    override fun savePickedImage(path: String?) {
        with(prefs.edit()) {
            putString(PICKED_IMAGE_PATH, path)
            apply()
        }
    }

    companion object {
        private const val PICKED_IMAGE_PATH = "PICKED_IMAGE_PATH"
    }
}