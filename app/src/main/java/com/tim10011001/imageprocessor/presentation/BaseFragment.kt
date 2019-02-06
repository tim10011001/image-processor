package com.tim10011001.imageprocessor.presentation

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tim10011001.imageprocessor.di.utils.DependenciesConsumer
import dagger.android.support.AndroidSupportInjection

abstract class BaseFragment: Fragment() {
    abstract fun getLayoutId(): Int
    abstract fun initUi()
    abstract fun fragmentKey(): String

    override fun onAttach(context: Context?) {
        if(this is DependenciesConsumer) {
            AndroidSupportInjection.inject(this)
        }

        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(getLayoutId(), container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }
}