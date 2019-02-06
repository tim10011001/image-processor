package com.tim10011001.imageprocessor.presentation.ui.fragments.gallery

import android.os.Handler
import android.os.Looper
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.tim10011001.imageprocessor.R
import com.tim10011001.imageprocessor.data.models.gallery.GalleryModel
import com.tim10011001.imageprocessor.di.utils.DependenciesConsumer
import com.tim10011001.imageprocessor.presentation.BaseFragment
import com.tim10011001.imageprocessor.presentation.ui.fragments.gallery.adapter.GalleryAdapter
import kotlinx.android.synthetic.main.gallery_fragment.*
import javax.inject.Inject

class GalleryFragment: BaseFragment(), GalleryContract.View, DependenciesConsumer {

    @Inject lateinit var presenter: GalleryContract.Presenter
    private val adapter = GalleryAdapter(object : GalleryAdapter.GalleryCallbacks {
        override fun onItemClick(galleryItem: GalleryModel) {
            presenter.pickImage(galleryItem)
        }

    })
    private val uiHandler = Handler(Looper.getMainLooper())

    override fun getLayoutId(): Int = R.layout.gallery_fragment

    override fun initUi() {
        galleryRecycler.layoutManager = GridLayoutManager(context, 2)
        galleryRecycler.adapter = adapter
        galleryToolbar?.setNavigationIcon(R.drawable.ic_arrow_back)
        galleryToolbar?.setNavigationOnClickListener {
            presenter.closeScreen()
        }

        presenter.attachView(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachView()
    }

    override fun showGalleryModels(models: List<GalleryModel>) {
        uiHandler.post {
            adapter.updateDataSource(models)
        }
    }

    override fun closeScreen() {
        activity.supportFragmentManager.popBackStack()
    }

    override fun hideProgress() {
        if(progress?.visibility != View.GONE) {
            uiHandler.post {
                progress?.visibility = View.GONE
            }
        }
    }

    override fun fragmentKey(): String = this::class.simpleName!!

    companion object {
        fun newInstance(): GalleryFragment{
            return GalleryFragment()
        }
    }
}