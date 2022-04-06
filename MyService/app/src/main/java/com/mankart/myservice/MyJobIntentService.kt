package com.mankart.myservice

import android.content.Intent
import android.content.Context
import android.util.Log
import androidx.core.app.JobIntentService

class MyJobIntentService : JobIntentService() {

    override fun onHandleWork(intent: Intent) {
        Log.d(TAG, "onHandleWork started ...")
        val duration = intent.getLongExtra(EXTRA_DURATION, 0)

        try {
            Thread.sleep(duration)
            Log.d(TAG, "Work is done after $duration ms")
        } catch (e: InterruptedException) {
            e.printStackTrace()
            Thread.currentThread().interrupt()
            Log.e(TAG, "Work is interrupted", e)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

    companion object {
        private const val JOB_ID = 1000
        internal const val EXTRA_DURATION = "extra_duration"
        internal val TAG = MyJobIntentService::class.java.simpleName

        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(context, MyJobIntentService::class.java, JOB_ID, intent)
        }
    }

}