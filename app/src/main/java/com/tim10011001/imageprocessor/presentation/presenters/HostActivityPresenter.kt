package com.tim10011001.imageprocessor.presentation.presenters

import android.util.Log
import android.widget.Toast
import com.tim10011001.imageprocessor.presentation.ui.HostActivityContract
import java.lang.ref.WeakReference
import javax.inject.Inject

class HostActivityPresenter @Inject constructor(): HostActivityContract.Presenter {
    private var viewRef: WeakReference<HostActivityContract.View>? = null

    private fun view(): HostActivityContract.View? {
        return viewRef?.get()
    }

    override fun requestStoragePermissions() {
        view()?.requestStoragePermissionsIfNeed()
    }

    override fun requestCameraPermission() {
        view()?.requestCameraPermissionsIfNeed()
    }

    override fun handleCameraResult() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun attachView(view: HostActivityContract.View) {
        viewRef = WeakReference(view)
        Log.e(this::class.simpleName, "Attached")
        requestStoragePermissions()
    }

    override fun detachView() {
        viewRef = null
    }

    override fun onCameraPermissionGranted() {
        view()?.startCaptureAction()
    }

    override fun onCameraPermissionDenied() {
        view()?.showCameraActivationToast()
    }

    override fun onStoragePermissionGranted() {
        openTransformationsScreen()
    }

    override fun openTransformationsScreen() {
        view()?.openTransformationsFragment()
    }

    override fun openGalleryScreen() {
        view()?.openGalleryFragment()
    }

    companion object {
        const val TAKE_PICTURE_REQUEST = 99
    }
}