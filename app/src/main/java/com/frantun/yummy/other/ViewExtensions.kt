package com.frantun.yummy.other

import android.view.View
import com.frantun.yummy.common.SafeClickListener

fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}

fun View.setAsGone() {
    this.visibility = View.GONE
}

fun View.setAsVisible() {
    this.visibility = View.VISIBLE
}

fun View.setAsInVisible() {
    this.visibility = View.INVISIBLE
}

fun View.setAsEnabled() {
    this.isEnabled = true
}

fun View.setAsDisabled() {
    this.isEnabled = false
}
