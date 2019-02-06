package com.tim10011001.imageprocessor.core.threading

import android.os.Process
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class ThreadHelper private constructor() {
    private val backgroundExecutor: ThreadPoolExecutor

    init {
        val threadFactory = PriorityThreadFactory(Process.THREAD_PRIORITY_BACKGROUND)

        backgroundExecutor = ThreadPoolExecutor(
                NUMBER_OF_CORES * 2,
                NUMBER_OF_CORES * 2,
                60L,
                TimeUnit.SECONDS,
                LinkedBlockingQueue(),
                threadFactory)
    }

    companion object {
        private val NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors()
        private var instance: ThreadHelper? = null

        fun getInstance(): ThreadPoolExecutor? {
            if(instance == null) {
                synchronized(ThreadHelper::class) {
                    instance = ThreadHelper()
                }
            }

            return instance?.backgroundExecutor
        }
    }
}