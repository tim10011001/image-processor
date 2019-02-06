package com.tim10011001.imageprocessor.presentation.ui

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.tim10011001.imageprocessor.R
import com.tim10011001.imageprocessor.presentation.presenters.HostActivityPresenter
import com.tim10011001.imageprocessor.presentation.ui.fragments.gallery.GalleryFragment
import com.tim10011001.imageprocessor.presentation.ui.fragments.transformations.TransformationsFragment
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class HostActivity: AppCompatActivity(), HasSupportFragmentInjector, HostActivityContract.View{
    @Inject lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>
    @Inject lateinit var presenter: HostActivityContract.Presenter

    private val transformationsFragment = TransformationsFragment.newInstance()
    private val galleryFragment = GalleryFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_holder)
        presenter.attachView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    override fun openCameraForResult() {
        presenter.requestCameraPermission()
    }

    override fun startCaptureAction() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, HostActivityPresenter.TAKE_PICTURE_REQUEST)
            }
        }
    }

    override fun openTransformationsFragment() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, transformationsFragment, transformationsFragment.fragmentKey())
                .addToBackStack(transformationsFragment.fragmentKey())
                .commit()
    }

    override fun openGalleryFragment() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, galleryFragment, galleryFragment.fragmentKey())
                .addToBackStack(galleryFragment.fragmentKey())
                .commit()
    }

    override fun requestStoragePermissionsIfNeed() {
        if(!isStoragePermissionsGranted()) {
            TedPermission.with(this)
                    .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .setPermissionListener(object : PermissionListener {
                        override fun onPermissionGranted() {
                            presenter.onStoragePermissionGranted()
                        }

                        override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                            Toast.makeText(this@HostActivity, R.string.need_storage_permission, Toast.LENGTH_LONG).show()
                            requestStoragePermissionsIfNeed()
                        }

                    }).check()
        } else {
            presenter.onStoragePermissionGranted()
        }
    }

    private fun isStoragePermissionsGranted(): Boolean {
        return TedPermission.isGranted(this,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    override fun requestCameraPermissionsIfNeed() {
        if(!isCameraPermissionsGranted()) {
            TedPermission.with(this)
                    .setPermissions(Manifest.permission.CAMERA)
                    .setPermissionListener(object : PermissionListener {
                        override fun onPermissionGranted() {
                            presenter.onCameraPermissionGranted()
                        }

                        override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                            presenter.onCameraPermissionDenied()
                        }

                    }).check()
        } else {
            presenter.onCameraPermissionGranted()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        supportFragmentManager
                .findFragmentByTag(transformationsFragment.fragmentKey())
                .onActivityResult(requestCode, resultCode, data)
    }

    private fun isCameraPermissionsGranted(): Boolean {
        return TedPermission.isGranted(this,
                Manifest.permission.CAMERA)
    }


    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
       return dispatchingAndroidInjector
    }

    override fun showCameraActivationToast() {
        Toast.makeText(this, R.string.camera_activation_message, Toast.LENGTH_LONG).show()
    }
}