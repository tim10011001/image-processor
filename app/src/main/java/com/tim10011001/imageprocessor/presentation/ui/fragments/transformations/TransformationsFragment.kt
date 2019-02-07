package com.tim10011001.imageprocessor.presentation.ui.fragments.transformations

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.support.design.widget.TextInputEditText
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.tim10011001.imageprocessor.R
import com.tim10011001.imageprocessor.data.models.transformations.TransformationModel
import com.tim10011001.imageprocessor.di.utils.DependenciesConsumer
import com.tim10011001.imageprocessor.presentation.BaseFragment
import com.tim10011001.imageprocessor.presentation.presenters.HostActivityPresenter
import com.tim10011001.imageprocessor.presentation.ui.HostActivityContract
import com.tim10011001.imageprocessor.presentation.ui.fragments.transformations.adapter.TransformationsAdapter
import kotlinx.android.synthetic.main.image_layout.*
import kotlinx.android.synthetic.main.transformations_fragment.*
import kotlinx.android.synthetic.main.transformations_holder.*
import org.w3c.dom.Text
import javax.inject.Inject

class TransformationsFragment: BaseFragment(), TransformationsContract.View, DependenciesConsumer{

    @Inject lateinit var presenter: TransformationsContract.Presenter
    private val uiHandler = Handler(Looper.getMainLooper())
    private val adapter = TransformationsAdapter(object : TransformationsAdapter.OnItemClick {
        override fun onItemClick(model: TransformationModel?) {
            createRemoveOrUseDialog(model)
        }
    })

    override fun getLayoutId(): Int = R.layout.transformations_fragment

    override fun initUi() {
        transformationsResultRecycler?.layoutManager = LinearLayoutManager(context)
        transformationsResultRecycler?.adapter = adapter
        transformationsResultRecycler?.isNestedScrollingEnabled = false

        loadImageBtn?.setOnClickListener {
            presenter.openLoadVariants()
        }

        transformationView?.setOnClickListener {
            presenter.openLoadVariants()
        }

        rotateBtn?.setOnClickListener {
            presenter.rotateImage()
        }

        invertColorsBtn?.setOnClickListener {
            presenter.invertColors()
        }

        reflectImageBtn?.setOnClickListener {
            presenter.reflectImage()
        }

        exifInformationBtn?.setOnClickListener {
            presenter.loadExifInformation()
        }

    }


    override fun onResume() {
        super.onResume()
        presenter.attachView(this)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachView()
    }

    override fun showImage(currentImageSource: Bitmap?) {
        uiHandler.post {
            loadImageBtn?.visibility = View.GONE
            transformationView?.visibility = View.VISIBLE
            transformationView?.setImageBitmap(currentImageSource)
        }
    }

    override fun showProgress() {
        uiHandler.post {
            loadingStatus?.visibility = View.VISIBLE
            loadImageBtn?.visibility = View.GONE
            transformationView?.visibility = View.GONE
        }
    }

    override fun hideProgress() {
        uiHandler.post {
            loadingStatus?.visibility = View.GONE
            loadImageBtn.visibility = View.GONE
            transformationView?.visibility = View.VISIBLE
        }
    }

    override fun showLoadSourceBtn() {
        uiHandler.post {
            loadingStatus?.visibility = View.GONE
            transformationView?.visibility = View.GONE
            loadImageBtn?.visibility = View.VISIBLE
        }
    }

    override fun updateTransformationsView(transformationsResults: List<TransformationModel?>) {
        uiHandler.post {
            adapter.updateSource(transformationsResults)
        }
    }


    private fun createRemoveOrUseDialog(model: TransformationModel?) {
        val dialog = AlertDialog.Builder(context)
                .setMessage(R.string.delete_or_use)
                .setNeutralButton(R.string.delete) { dialog, _ ->
                    presenter.removeModel(model)
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.use_as_source) { dialog, _ ->
                    presenter.useAsSource(model)
                    dialog.dismiss()
                }
                .create()

        dialog.show()
    }

    override fun showSourceChoiceDialog(loadVariants: Array<String>) {
        val choiceDialog = AlertDialog.Builder(context)
                .setTitle(R.string.which_images_source_to_use)
                .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
                .setItems(loadVariants) { dialog, which ->
                    presenter.handleLoadVariant(which)
                    dialog.dismiss()
                }
                .create()

        choiceDialog.show()
    }

    override fun openGallery() {
        (activity as HostActivityContract.View)
                .openGalleryFragment()
    }

    override fun openCameraForCapture() {
        (activity as HostActivityContract.View)
                .openCameraForResult()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == HostActivityPresenter.TAKE_PICTURE_REQUEST && resultCode == Activity.RESULT_OK) {
            val image = data?.extras?.get("data") as Bitmap
            presenter.handleCapturedData(image)
        }
    }

    override fun showDownloadDialog() {
        val view = LayoutInflater.from(context).inflate(R.layout.download_image_dialog, null)
        val inputUrlView = view.findViewById<TextInputEditText>(R.id.urlInputView)
        val dialog = AlertDialog.Builder(context)
                .setView(view)
                .setNeutralButton(R.string.download) { dialog, _ ->
                    presenter.downloadImage(inputUrlView.text.toString())
                }.create()

        dialog.show()
    }

    override fun showDownloadError() {
        Toast.makeText(context, R.string.loading_error, Toast.LENGTH_LONG).show()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        if(newConfig?.orientation == ORIENTATION_LANDSCAPE) {
            transformationsResultRecycler?.layoutManager = GridLayoutManager(context, 2)
        } else {
            transformationsResultRecycler?.layoutManager = LinearLayoutManager(context)
        }
    }

    override fun showExifInfo(cameraOwner: String?) {
        val view = LayoutInflater.from(context).inflate(R.layout.exif_dialog, null)
        val exifInfoView = view.findViewById<TextView>(R.id.cameraOwnerText)
        val exifDialog = AlertDialog.Builder(context)
                .setView(view)
                .setNegativeButton(R.string.close) { dialog, _ ->
                    dialog.dismiss()
                }.create()

        exifDialog.show()
        exifInfoView.text = cameraOwner ?: context.getString(R.string.no_camera_owner_exist)
    }

    override fun fragmentKey(): String = this::class.simpleName!!

    companion object {

        fun newInstance(): TransformationsFragment {
            return TransformationsFragment()
        }
    }
}