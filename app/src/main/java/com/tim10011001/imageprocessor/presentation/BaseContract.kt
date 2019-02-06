package com.tim10011001.imageprocessor.presentation

interface BaseContract {
    interface View

    interface Presenter<V: View> {
        fun attachView(view: V)
        fun detachView()
    }
}