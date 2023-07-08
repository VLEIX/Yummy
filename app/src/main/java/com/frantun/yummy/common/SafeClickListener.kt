package com.frantun.yummy.common

import android.os.SystemClock
import android.view.View

class SafeClickListener(
    private var defaultInterval: Int = DEFAULT_INTERVAL,
    private val onSafeCLick: (View) -> Unit
) : View.OnClickListener {
    private var lastTimeClicked: Long = LAST_TIME_CLICKED_INITIAL
    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval) {
            return
        }
        lastTimeClicked = SystemClock.elapsedRealtime()
        onSafeCLick(v)
    }

    companion object {
        private const val DEFAULT_INTERVAL: Int = 1000
        private const val LAST_TIME_CLICKED_INITIAL: Long = 0
    }
}
