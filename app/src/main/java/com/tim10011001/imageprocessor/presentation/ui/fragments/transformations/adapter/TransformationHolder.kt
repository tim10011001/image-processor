package com.tim10011001.imageprocessor.presentation.ui.fragments.transformations.adapter

import android.graphics.Bitmap
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.tim10011001.imageprocessor.R

class TransformationHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    private val transformationProgress = itemView?.findViewById<ProgressBar>(R.id.transformationProgress)
    private val transformationImage = itemView?.findViewById<ImageView>(R.id.transformationResult)
    private val rootView = itemView?.findViewById<View>(R.id.transformationRoot)

//    fun showProgress(progress: Int?) {
//        transformationProgress?.progress = progress!!
//        if(progress == transformationProgress?.max) {
//            transformationProgress.visibility = View.GONE
//        }
//    }

    fun showTransformationView(image: Bitmap?) {
        if(image == null) {
            transformationProgress?.visibility = View.VISIBLE
            transformationImage?.visibility = View.GONE
        } else {
            transformationImage?.setImageBitmap(image)
            transformationProgress?.visibility = View.GONE
        }
    }

    fun setOnHolderClickListener(onClickListener: View.OnClickListener) {
        transformationImage?.setOnClickListener(onClickListener)
    }

    fun handleBackground(color: Int) {
        rootView?.setBackgroundColor(ContextCompat.getColor(itemView?.context, color))
    }
}