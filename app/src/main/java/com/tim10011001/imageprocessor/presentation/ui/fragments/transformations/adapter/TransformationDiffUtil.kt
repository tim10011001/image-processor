package com.tim10011001.imageprocessor.presentation.ui.fragments.transformations.adapter

import android.support.v7.util.DiffUtil
import com.tim10011001.imageprocessor.data.models.transformations.TransformationModel

class TransformationDiffUtil(private val oldSource: List<TransformationModel?>,
                             private val newSource: List<TransformationModel?>) : DiffUtil.Callback() {


    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldSource[oldItemPosition]
        val newItem = newSource[newItemPosition]

        val oldPath = oldItem?.cachedPath
        val newPath = newItem?.cachedPath

        return if(oldPath != null && newPath != null) {
            oldPath == newPath
        } else {
            oldPath == null && newPath == null
        }
    }

    override fun getOldListSize(): Int = oldSource.size

    override fun getNewListSize(): Int = newSource.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldSource[oldItemPosition]
        val newItem = newSource[newItemPosition]

        val oldImage = oldItem?.image
        val newImage = newItem?.image

        return if(oldImage != null && newImage != null) {
            oldImage.sameAs(newImage)
        } else {
            oldImage == null && newImage == null
        }
    }

}