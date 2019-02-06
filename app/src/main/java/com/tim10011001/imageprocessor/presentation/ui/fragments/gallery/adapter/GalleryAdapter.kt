package com.tim10011001.imageprocessor.presentation.ui.fragments.gallery.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tim10011001.imageprocessor.R
import com.tim10011001.imageprocessor.data.models.gallery.GalleryModel

class GalleryAdapter(private val galleryCallbacks: GalleryCallbacks): RecyclerView.Adapter<GalleryHolder>() {

    private val source = arrayListOf<GalleryModel>()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): GalleryHolder {
        val view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.gallery_holder, parent, false)

        return GalleryHolder(view)
    }

    override fun getItemCount(): Int = source.size

    override fun onBindViewHolder(holder: GalleryHolder?, position: Int) {
        val galleryModel = source[position]
        holder?.showImage(galleryModel.bitmap)
        holder?.showPath(galleryModel.imageFile.absolutePath)
        holder?.onItemClickListener(View.OnClickListener {
            galleryCallbacks.onItemClick(galleryModel)
        })
    }


    fun updateDataSource(updates: List<GalleryModel>) {
        source.clear()
        source.addAll(updates)
        notifyDataSetChanged()
    }

    interface GalleryCallbacks {
        fun onItemClick(galleryItem: GalleryModel)
    }
}