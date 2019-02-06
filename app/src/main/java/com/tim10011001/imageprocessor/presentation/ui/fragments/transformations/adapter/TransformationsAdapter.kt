package com.tim10011001.imageprocessor.presentation.ui.fragments.transformations.adapter

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tim10011001.imageprocessor.R
import com.tim10011001.imageprocessor.data.models.transformations.TransformationModel

class TransformationsAdapter(private val onItemClick: TransformationsAdapter.OnItemClick) : RecyclerView.Adapter<TransformationHolder>() {
    private val source = arrayListOf<TransformationModel?>()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TransformationHolder {
        val view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.transformations_holder, parent, false)

        return TransformationHolder(view)
    }

    override fun getItemCount(): Int = source.size

    override fun onBindViewHolder(holder: TransformationHolder?, position: Int) {
        val model = source[position]
        //holder?.showProgress(model?.progress)
        holder?.showTransformationView(model?.image)
        holder?.setOnHolderClickListener(View.OnClickListener {
            onItemClick.onItemClick(model)
        })

        holder?.handleBackground(model?.backgroundColor!!)
    }

    fun updateSource(newSource: List<TransformationModel?>) {
//        val diffCallback = TransformationDiffUtil(source, newSource)
//        val diffResult = DiffUtil.calculateDiff(diffCallback)

        source.clear()
        source.addAll(newSource)
        notifyDataSetChanged()
//        diffResult.dispatchUpdatesTo(this)
    }

    interface OnItemClick {
        fun onItemClick(model: TransformationModel?)
    }
}