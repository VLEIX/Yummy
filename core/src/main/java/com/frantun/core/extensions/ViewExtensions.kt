package com.frantun.core.extensions

import android.view.View
import com.frantun.core.utils.SafeClickListener

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

fun View.setAsInvisible() {
    this.visibility = View.INVISIBLE
}

fun View.setAsEnabled() {
    this.isEnabled = true
}

fun View.setAsDisabled() {
    this.isEnabled = false
}
