package com.tim10011001.imageprocessor.presentation.ui

import com.tim10011001.imageprocessor.presentation.BaseContract

interface HostActivityContract {
    interface View: BaseContract.View {
        fun openCameraForResult()
        fun startCaptureAction()
        fun openTransformationsFragment()
        fun openGalleryFragment()
        fun requestStoragePermissionsIfNeed()
        fun requestCameraPermissionsIfNeed()
        fun showCameraActivationToast()
    }

    interface Presenter: BaseContract.Presenter<View> {
        fun requestStoragePermissions()
        fun requestCameraPermission()
        fun handleCameraResult()
        fun onCameraPermissionGranted()
        fun onCameraPermissionDenied()
        fun onStoragePermissionGranted()
        fun openTransformationsScreen()
        fun openGalleryScreen()
    }
}