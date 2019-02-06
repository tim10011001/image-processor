package com.tim10011001.imageprocessor.core.threading

import android.os.Process
import java.io.IOException
import java.util.concurrent.ThreadFactory

class PriorityThreadFactory(private val priority: Int): ThreadFactory {

    override fun newThread(task: Runnable?): Thread {
        val taskWrapper = Runnable {
            try {
                Process.setThreadPriority(priority)
            }catch (e: IOException) {

            }

            task?.run()
        }

        return Thread(taskWrapper)
    }
}