package com.tim10011001.imageprocessor.presentation.ui.fragments.gallery.adapter

import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tim10011001.imageprocessor.R

class GalleryHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    private val imageHolder = itemView?.findViewById<ImageView>(R.id.galleryImageHolder)
    //private val pathView = itemView?.findViewById<TextView>(R.id.imagePath)

    fun showImage(bitmap: Bitmap) {
        imageHolder?.setImageBitmap(bitmap)
    }

    fun showPath(path: String) {
      //  pathView?.text = path
    }

    fun onItemClickListener(onClick: View.OnClickListener) {
        imageHolder?.setOnClickListener(onClick)
    }
}